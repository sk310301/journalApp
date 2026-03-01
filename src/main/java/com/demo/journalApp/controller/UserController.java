package com.demo.journalApp.controller;


import com.demo.journalApp.entity.User;
import com.demo.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAll();
    }


    @PostMapping("/create")
    public void createUser(@RequestBody User newUser) {
        log.info("Inside createUser for newUser : {}", newUser);
        userService.addUser(newUser);
    }


    @PutMapping("/update/{userName}")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser, @PathVariable String userName) {
        User userInDB = userService.findByUserName(userName);
        if (userInDB != null) {
            userInDB.setUserName(updatedUser.getUserName());
            userInDB.setPassword(updatedUser.getPassword());
            userService.addUser(userInDB);
            return new ResponseEntity<User>(userInDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserWithAuth(@RequestBody User updatedUser) {
        try {
            log.info("Inside updateUserWithAuth with updatedUser : {}", updatedUser);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            return new ResponseEntity<>(userService.editUser(userName, updatedUser), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in updateUserWithAuth due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserWithUserPass() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Inside deleteUserWithUserPass for user : {}", userName);
            return new ResponseEntity<>(userService.removeUser(userName), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error in deleteUserWithUserPass due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/weather/{location}")
    public ResponseEntity<?> getWeather(@PathVariable String location) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Inside getWeather for user : {} and location : {}", userName, location);
            return new ResponseEntity<>(userService.getWeather(userName, location), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in getWeather due to : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/weather/redis/{location}")
    public ResponseEntity<?> getWeatherRedis(@PathVariable String location) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Inside getWeatherRedis for user : {} and location : {}", userName, location);
            return new ResponseEntity<>(userService.getWeatherByRedis(userName, location), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in getWeatherRedis due to : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

