package com.demo.testApp.scheduler;

import com.demo.testApp.entity.JournalEntry;
import com.demo.testApp.entity.User;
import com.demo.testApp.enums.Sentiment;
import com.demo.testApp.repository.UserRepositoryImpl;
import com.demo.testApp.service.AppCache;
import com.demo.testApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;


    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendMail() {
        List<User> users = userRepository.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntryList = user.getJournalEntryList();
            List<Sentiment> sentiments = journalEntryList.stream().filter(x -> x.getDate().isAfter(LocalDate.now().minusDays(7))).map(x -> x.getSentiment()).toList();
            Map<Sentiment, Integer> sentimentCount = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSent = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSent = entry.getKey();
                }
            }
            if (mostFrequentSent != null) {
                emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSent.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
