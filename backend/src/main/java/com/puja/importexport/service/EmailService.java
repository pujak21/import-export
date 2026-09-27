package com.puja.importexport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.puja.importexport.model.Contact;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String adminRecipient;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username}") String fromAddress,
                        @Value("${mail.admin-recipient}") String adminRecipient) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.adminRecipient = adminRecipient;
    }

    public void sendAdminNotification(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(adminRecipient);
        message.setReplyTo(contact.getEmail());
        message.setSubject("New Export Inquiry from " + contact.getName());

        message.setText(
                "New Inquiry Details:\n\n" +
                "Name: " + valueOrDash(contact.getName()) + "\n" +
                "Email: " + valueOrDash(contact.getEmail()) + "\n" +
                "Country: " + valueOrDash(contact.getCountry()) + "\n" +
                "Service Needed: " + valueOrDash(contact.getServiceNeeded()) + "\n" +
                "Message: " + valueOrDash(contact.getMessage())
        );

        mailSender.send(message);
    }

    public void sendCustomerConfirmation(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(contact.getEmail());
        message.setReplyTo(adminRecipient);
        message.setSubject("Thank you for contacting Turmexa Organic 🌿");

        String product = contact.getServiceNeeded() == null || contact.getServiceNeeded().isBlank()
                ? "our products"
                : contact.getServiceNeeded();

        message.setText(
                "Dear " + contact.getName() + ",\n\n" +
                "Thank you for your export inquiry regarding " + product + ".\n\n" +
                "Our team will review your request and contact you shortly.\n\n" +
                "Best Regards,\n" +
                "Turmexa Organic\n" +
                "Kailash Overseas"
        );

        mailSender.send(message);
    }

    private static String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
