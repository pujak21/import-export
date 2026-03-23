package com.puja.importexport.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(
            "mongodb+srv://pujaojha7187:Kristyp21@cluster0.wiwtl6f.mongodb.net/import_export_db?retryWrites=true&w=majority"
        );
    }
}