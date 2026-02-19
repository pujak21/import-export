package com.puja.importexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;
import com.puja.importexport.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public Contact saveContact(@RequestBody Contact contact) {

        Contact savedContact = contactRepository.save(contact);

        // Send email to Admin
        emailService.sendAdminNotification(savedContact);

        // Send confirmation to Customer
        emailService.sendCustomerConfirmation(savedContact);

        return savedContact;
    }
}
