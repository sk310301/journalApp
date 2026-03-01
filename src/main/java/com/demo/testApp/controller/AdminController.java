package com.demo.testApp.controller;

import com.demo.testApp.entity.User;
import com.demo.testApp.service.AppCache;
import com.demo.testApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            log.info("Inside getAllUsers");
            List<User> allUsers = userService.getAll();
            if (allUsers != null && !allUsers.isEmpty()) {
                return new ResponseEntity<>(allUsers, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            log.error("Error in getAllUsers due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAdmin(@RequestBody User adminUser) {
        try {
            log.info("Inside addAdmin for user : {}", adminUser);
            return new ResponseEntity<>(userService.saveAdmin(adminUser), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error in addAdmin due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/clear/appCache")
    public ResponseEntity<Boolean> clearAppCache() {
        try {
            log.info("Inside clearAppCache..");
            appCache.init();
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in clearAppCache due to : {}", e.getMessage());
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        }
    }
}
