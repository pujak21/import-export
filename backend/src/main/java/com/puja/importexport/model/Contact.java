package com.puja.importexport.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "contacts")
public class Contact {

    @Id
    private String id;

    private String name;
    private String email;
    private String country;
    private String serviceNeeded;
    private String message;
}
