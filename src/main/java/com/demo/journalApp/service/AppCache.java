package com.demo.journalApp.service;

import com.demo.journalApp.entity.Config;
import com.demo.journalApp.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AppCache {


    Map<String, String> keyValue;
    @Autowired
    private ConfigRepository configRepository;

    @PostConstruct
    public void init() {
        try {
            keyValue = new HashMap<>();
            List<Config> allEntry = configRepository.findAll();
            for (Config entry : allEntry) {
                keyValue.put(entry.getKey(), entry.getValue());
            }
        } catch (RuntimeException e) {
            log.error("Error in init due to : {}", e.getMessage());
        }

    }

    public enum keysList {
        WEATHER_API
    }
}
