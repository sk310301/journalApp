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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal/v2/")
@Slf4j
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<JournalEntry> getAllJournals() {
        log.info("Inside getAllJournals");
        return journalEntryService.viewAll();
    }

    @GetMapping("/all/{userName}")
    public ResponseEntity<List<JournalEntry>> getAllByUserName(@PathVariable String userName) {
        log.info("Inside getAllByUserName for user : {}", userName);
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntryList();
        if (journalEntryList != null && !journalEntryList.isEmpty()) {
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        log.info("Inside getJournalEntryById for id : {}", id);
        Optional<JournalEntry> journalEntry = journalEntryService.viewById(id);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create")
    public boolean createEntry(@RequestBody JournalEntry journalEntry) {
        log.info("Inside createEntry for journalEntry : {}", journalEntry);
        journalEntryService.saveEntry(journalEntry);
        return true;
    }

    @PostMapping("/create/{userName}")
    public ResponseEntity<?> createEntryForUSer(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        log.info("Inside createEntryForUser with journalEntry : {} and userName : {}", journalEntry, userName);
        try {
            journalEntryService.saveEntryForUser(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalEntryService.updateEntry(id, entry);
    }

    @PutMapping("/id/{id}/{userName}")
    public ResponseEntity<JournalEntry> updateJournalEntryByIdUserName(@PathVariable ObjectId id, @RequestBody JournalEntry entry, @PathVariable String userName) {
        try {
            log.info("Inside updateJournalEntryByIdUserName for id : {} and userName : {}", id, userName);
            return new ResponseEntity<>(journalEntryService.updateEntryUserName(id, userName, entry), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in updateJournalEntryByIdUserName due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteJournalEntryById(@PathVariable ObjectId id) {
        log.info("Inside deleteJournalEntryById for id:{}", id);
        journalEntryService.deleteEntry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/id/{id}/{userName}")
    public ResponseEntity<Object> deleteJournalEntryByIdUserName(@PathVariable String userName, @PathVariable ObjectId id) {
        log.info("Inside deleteJournalEntryByIdUserName for id : {} and userName : {}", id, userName);
        try {
            journalEntryService.deleteEntryWithUser(id, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error in deleteJournalEntryByIdUserName due to : {}", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}





