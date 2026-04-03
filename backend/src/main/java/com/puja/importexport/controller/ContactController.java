package com.puja.importexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;
import com.puja.importexport.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // temporary for debugging (later restrict)
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public Contact saveContact(@RequestBody Contact contact) {

        // Save to DB
        Contact savedContact = contactRepository.save(contact);

        try {
            // Send emails
            emailService.sendAdminNotification(savedContact);
            emailService.sendCustomerConfirmation(savedContact);
        } catch (Exception e) {
            // Prevent API from failing if email fails
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return savedContact;
    }
}