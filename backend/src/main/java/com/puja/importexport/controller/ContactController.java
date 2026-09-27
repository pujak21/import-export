package com.puja.importexport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puja.importexport.dto.ContactRequest;
import com.puja.importexport.dto.ContactResponse;
import com.puja.importexport.model.Contact;
import com.puja.importexport.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contact")
    public ResponseEntity<ContactResponse> saveContact(@Valid @RequestBody ContactRequest request) {
        Contact savedContact = contactService.submitInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ContactResponse(savedContact.getId(), "Inquiry received"));
    }
}
