package com.demo.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    public <T> T getData(String key, Class<T> responseClass) {
        try {
            log.info("Inside getData for key : {}", key);
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) return null;
            String data = o.toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), responseClass);
        } catch (Exception e) {
            log.error("Error in getData due to : {}", e.getMessage());
            return null;
        }
    }


    public void setData(String key, Object o, Long ttl) {
        try {
            log.info("Inside setData for key : {}", key);
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonData, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error in setData due to : {}", e.getMessage());
        }
    }
}
