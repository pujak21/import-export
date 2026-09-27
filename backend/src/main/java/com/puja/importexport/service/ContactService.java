package com.puja.importexport.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import com.puja.importexport.dto.ContactRequest;
import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final ContactNotificationService notificationService;

    public ContactService(ContactRepository contactRepository, ContactNotificationService notificationService) {
        this.contactRepository = contactRepository;
        this.notificationService = notificationService;
    }

    public Contact submitInquiry(ContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.name().trim());
        contact.setEmail(request.email().trim());
        contact.setCountry(trimToNull(request.country()));
        contact.setServiceNeeded(trimToNull(request.serviceNeeded()));
        contact.setMessage(trimToNull(request.message()));
        contact.setCreatedAt(Instant.now());

        Contact savedContact = contactRepository.save(contact);

        try {
            notificationService.sendNotifications(savedContact);
        } catch (TaskRejectedException e) {
            log.error("Email queue is full; notifications not sent for inquiry {}", savedContact.getId(), e);
        }

        return savedContact;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
