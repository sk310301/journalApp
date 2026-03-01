package com.demo.testApp.controller;

import com.demo.testApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV1 {
    private Map<ObjectId, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping("/all")
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping("/create")
    public boolean createEntry(@RequestBody JournalEntry journalEntry) {
        ObjectId objectId = new ObjectId();
        journalEntries.put(objectId, journalEntry);
        return true;
    }

    @GetMapping("/id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable String id) {
        return journalEntries.get(id);
    }

    @DeleteMapping("/id/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable String id) {
        return journalEntries.remove(id);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalEntries.put(id, entry);
    }

}




