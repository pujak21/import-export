package com.puja.importexport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.puja.importexport.model.Contact;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAdminNotification(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("pujaojha7187@gmail.com"); // your email
        message.setSubject("New Export Inquiry from " + contact.getName());

        message.setText(
                "New Inquiry Details:\n\n" +
                "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Country: " + contact.getCountry() + "\n" +
                "Service Needed: " + contact.getServiceNeeded() + "\n" +
                "Message: " + contact.getMessage()
        );

        mailSender.send(message);
    }

    public void sendCustomerConfirmation(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(contact.getEmail());
        message.setSubject("Thank you for contacting Turmexa Organic 🌿");

        message.setText(
                "Dear " + contact.getName() + ",\n\n" +
                "Thank you for your export inquiry regarding " + contact.getServiceNeeded() + ".\n\n" +
                "Our team will review your request and contact you shortly.\n\n" +
                "Best Regards,\n" +
                "Turmexa Organic\n" +
                "Kailash Overseas"
        );

        mailSender.send(message);
    }
}
