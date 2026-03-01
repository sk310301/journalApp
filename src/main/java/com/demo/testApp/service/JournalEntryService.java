package com.demo.testApp.service;

import com.demo.testApp.entity.JournalEntry;
import com.demo.testApp.entity.User;
import com.demo.testApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    public List<JournalEntry> viewAll() {
        return journalEntryRepository.findAll();
    }


    public Optional<JournalEntry> viewById(ObjectId id) {
        try {
            log.info("Inside viewById for id : {}", id);
            return journalEntryRepository.findById(id);
        } catch (RuntimeException e) {
            log.error("Error in viewById due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void saveEntry(JournalEntry journalEntry) {
        try {
            log.info("Inside saveEntry for journalEntry : {}", journalEntry);
            journalEntry.setDate(LocalDate.now());
            journalEntryRepository.save(journalEntry);
        } catch (Exception e) {
            log.error("Error in saveEntry due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    //    @Transactional
    //    just for making this method transactional, shifted local mongo compass to mongo atlas
    public void saveEntryForUser(JournalEntry journalEntry, String userName) {
        log.info("Inside saveEntryForUser with journalEntry : {}", journalEntry);
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDate.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntryList().add(saved);
            userService.addUser(user);
        } catch (Exception e) {
            log.error("Error in saveEntryForUser due to : {}", e.getMessage());
            throw new RuntimeException("Error in saveEntryForUser due to : " + e);
        }
    }


    public JournalEntry updateEntry(ObjectId id, JournalEntry entry) {
        try {
            log.info("Inside updateEntry for id : {}, with entry : {}", id, entry);
            JournalEntry oldEntry = journalEntryRepository.findById(id).orElse(null);
            JournalEntry oldEntry1 = viewById(id).orElse(null);
            log.info("check id oldEntry==oldEntry1 --> {}", Objects.equals(oldEntry, oldEntry1));
            log.info(String.valueOf(oldEntry));
            if (oldEntry != null) {
                oldEntry.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ? entry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : oldEntry.getContent());
            }
            log.info(String.valueOf(oldEntry));
            journalEntryRepository.save(oldEntry);
            return oldEntry;
        } catch (RuntimeException e) {
            log.error("Error in updateEntry due to  : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteEntry(ObjectId id) {
        try {
            log.info("Inside deleteEntry for id : {}", id);
            journalEntryRepository.deleteById(id);
        } catch (RuntimeException e) {
            log.error("Error in deleteEntry due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public JournalEntry updateEntryUserName(ObjectId id, String userName, JournalEntry entry) {
        try {
            log.info("Inside updateEntryUserName for id : {}, userName : {}, entry : {}", id, userName, entry);
            JournalEntry old = viewById(id).orElse(null);
            if (old != null) {
                old.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ? entry.getTitle() : old.getTitle());
                old.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : old.getContent());
                saveEntry(old);
                return old;
            }
            return null;
        } catch (RuntimeException e) {
            log.error("Error in updateEntryUserName due to  : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //    @Transactional
    //    make it transactional once mongo-atlas is connected
    public void deleteEntryWithUser(ObjectId id, String userName) {
        try {
            log.info("Inside deleteEntryWithUser for id : {} and userName : {}", id, userName);
            User userInDB = userService.findByUserName(userName);
            boolean removed = userInDB.getJournalEntryList().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.addUser(userInDB);
                journalEntryRepository.deleteById(id);
            }
        } catch (RuntimeException e) {
            log.error("Error in deleteEntryWithUser due to  : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

