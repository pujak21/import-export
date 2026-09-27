package com.puja.importexport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;

@Service
public class ContactNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ContactNotificationService.class);

    private final EmailService emailService;
    private final ContactRepository contactRepository;

    public ContactNotificationService(EmailService emailService, ContactRepository contactRepository) {
        this.emailService = emailService;
        this.contactRepository = contactRepository;
    }

    @Async
    public void sendNotifications(Contact contact) {
        // Each email is attempted independently so one failure does not skip the other.
        contact.setAdminNotified(send("admin notification", contact, () -> emailService.sendAdminNotification(contact)));
        contact.setCustomerNotified(send("customer confirmation", contact, () -> emailService.sendCustomerConfirmation(contact)));

        try {
            contactRepository.save(contact);
        } catch (RuntimeException e) {
            log.error("Failed to record email delivery status for inquiry {}", contact.getId(), e);
        }
    }

    private boolean send(String type, Contact contact, Runnable sender) {
        try {
            sender.run();
            log.info("Sent {} for inquiry {}", type, contact.getId());
            return true;
        } catch (RuntimeException e) {
            log.error("Failed to send {} for inquiry {}", type, contact.getId(), e);
            return false;
        }
    }
}
