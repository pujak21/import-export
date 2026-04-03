package com.puja.importexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;
import com.puja.importexport.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://import-export-ruddy.vercel.app")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public Contact saveContact(@RequestBody Contact contact) {

        Contact savedContact = contactRepository.save(contact);

        emailService.sendAdminNotification(savedContact);
        emailService.sendCustomerConfirmation(savedContact);

        return savedContact;
    }
}