package com.puja.importexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.puja.importexport.model.Contact;
import com.puja.importexport.repository.ContactRepository;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("/contact")
    public Contact saveContact(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }
}
