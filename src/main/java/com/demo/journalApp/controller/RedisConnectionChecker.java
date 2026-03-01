package com.demo.journalApp.controller;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConnectionChecker {

    @Bean
    CommandLineRunner checkRedisConnection(RedisConnectionFactory factory) {
        return args -> {
            try {
                String pong = factory.getConnection().ping();
                System.out.println("Redis connection successful: " + pong);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Redis connection failed: " + e.getMessage());
            }
        };
    }
}
