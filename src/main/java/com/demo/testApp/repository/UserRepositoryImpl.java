package com.demo.testApp.repository;

import com.demo.testApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;

@Slf4j
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        try {
            log.info("Inside getUserForSA..");
            Query query = new Query();
            query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"));
            query.addCriteria(Criteria.where("sentimentalAnalysis").is(true));
            return mongoTemplate.find(query, User.class);
        } catch (RuntimeException e) {
            log.error("Error in getUserForSA due to : {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
