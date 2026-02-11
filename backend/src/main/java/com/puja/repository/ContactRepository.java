package com.puja.importexport.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.puja.importexport.model.Contact;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
