package com.example.smart_attendance.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        String uri = "mongodb+srv://Janhavipatil:jkp%2347@janhavi.9gh4pem.mongodb.net/";
        MongoClient mongoClient = MongoClients.create(uri);
        return new MongoTemplate(mongoClient, "SmartAttend");
    }
}
