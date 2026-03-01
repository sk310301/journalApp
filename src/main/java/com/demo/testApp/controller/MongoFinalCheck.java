package com.demo.testApp.controller;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoFinalCheck {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Environment env;

    @PostConstruct
    public void check() {
        System.out.println("Mongo DB in use = " + mongoTemplate.getDb().getName());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkDb() {
        System.out.println("CONNECTED TO DATABASE: " + mongoTemplate.getDb().getName());
    }
}

