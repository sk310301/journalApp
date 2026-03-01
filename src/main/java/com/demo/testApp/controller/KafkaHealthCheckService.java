package com.demo.testApp.controller;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaHealthCheckService {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @EventListener(ApplicationReadyEvent.class)
    public void checkConnection() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> names = topics.names().get(5, TimeUnit.SECONDS);

            System.out.println("Connected to Kafka. Topics: " + names);
        } catch (Exception e) {
            System.out.println("Kafka connection failed: " + e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkKafka() {
        InputStream is = KafkaHealthCheckService.class.getClassLoader().getResourceAsStream("client.truststore.jks");
        System.out.println(is != null ? "Found!" : "Not found!");
    }
}



