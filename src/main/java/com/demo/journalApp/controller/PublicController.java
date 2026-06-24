package com.demo.journalApp.controller;


import com.demo.journalApp.entity.Email;
import com.demo.journalApp.entity.User;
import com.demo.journalApp.repository.UserRepositoryImpl;
import com.demo.journalApp.service.EmailService;
import com.demo.journalApp.service.UserDetailsServiceImpl;
import com.demo.journalApp.service.UserService;
import com.demo.journalApp.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl detailsService;

    @PostConstruct
    public void healthCheck() {
        log.info("Healthy...");
    }

    @GetMapping("/healthCheck")
    public String healthCheckViaRequest() {
        log.info("Inside healthCheckViaRequest");
        return "Ok";
    }


    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody User newUser) throws Exception {
        try {
            log.info("Inside signUp for newUser : {}", newUser);
            return new ResponseEntity<>(userService.addNewUser(newUser), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error in signUp due to : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@RequestBody User existingUser) throws Exception {
        try {
            log.info("Inside logIn for existingUser : {}", existingUser);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existingUser.getUserName(), existingUser.getPassword()));
            UserDetails userDetails = detailsService.loadUserByUsername(existingUser.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in logIn due to : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/userSentiment")
    public ResponseEntity<List<User>> getUserSA() {
        try {
            log.info("Inside getUserSA..");
            List<User> userList = userRepository.getUserForSA();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in getUserSA due to : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody Email email) {
        try {
            log.info("Inside sendMail for email : {}", email);
            emailService.sendMail(email.getTo(), email.getSubject(), email.getBody(), email.getCc(), email.getBcc());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in sendMail due to : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}