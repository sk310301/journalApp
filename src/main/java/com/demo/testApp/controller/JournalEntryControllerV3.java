package com.demo.testApp.controller;


import com.demo.testApp.entity.JournalEntry;
import com.demo.testApp.entity.User;
import com.demo.testApp.service.JournalEntryService;
import com.demo.testApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal/v3")
@Slf4j
public class JournalEntryControllerV3 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    //    for @GetMapping("/all/{userName}")
    @GetMapping("/all")
    public ResponseEntity<List<JournalEntry>> getAllByUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Inside getAllByUser for user : {}", userName);
            User user = userService.findByUserName(userName);
            List<JournalEntry> journalEntryList = user.getJournalEntryList();
            if (journalEntryList != null && !journalEntryList.isEmpty()) {
                return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error in getAllByUser due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        log.info("Inside getJournalEntryById for id : {}", myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDB = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = userInDB.getJournalEntryList().stream().filter(x -> x.getId().equals(myId)).toList();
        if (!journalEntryList.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.viewById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //    for @PostMapping("/create/{userName}")
    @PostMapping("/create")
    public ResponseEntity<?> createEntryForUser(@RequestBody JournalEntry journalEntry) {
        log.info("Inside createEntryForUser with journalEntry : {}", journalEntry);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntryForUser(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //    for @PutMapping("/id/{id}") and @PutMapping("/id/{id}/{userName}")
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntryByIdUser(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        try {
            log.info("Inside updateJournalEntryByIdUserName for id : {}", id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            return new ResponseEntity<>(journalEntryService.updateEntryUserName(id, userName, entry), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in updateJournalEntryByIdUser due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    //    for @DeleteMapping("/id/{id}") and @DeleteMapping("/id/{id}/{userName}")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteJournalEntryWithAuth(@PathVariable ObjectId id) {
        log.info("Inside deleteJournalEntryWithAuth for id : {}", id);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.deleteEntryWithUser(id, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error in deleteJournalEntryWithAuth due to : {}", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}





