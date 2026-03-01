package com.demo.testApp.service;

import com.demo.testApp.entity.User;
import com.demo.testApp.entity.Weather;
import com.demo.testApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {

    //    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    public List<User> getAll() {
        log.info("Inside getAll");
        return userRepository.findAll();
    }

    public User findByUserName(String userName) {
        log.info("Inside findByUserName for userName : {}", userName);
        return userRepository.findByUserName(userName);
    }

    public void addUser(User newUser) {
        try {
            log.info("Inside addUser for newUser : {}", newUser);
            userRepository.save(newUser);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String encodePassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addNewUser(User newUser) {
        try {
            log.info("Inside addNewUser with user : {}", newUser);
            newUser.setPassword(encodePassword(newUser.getPassword()));
            if (newUser.getRoles() == null || newUser.getRoles().isEmpty()) {
                newUser.setRoles(List.of("USER"));
            }
            addUser(newUser);
            return "Added new user successfully.";
        } catch (Exception e) {
            log.error("Error in addNewUser due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public User editUser(String userName, User updatedUser) {
        try {
            log.info("Inside editUser for userName : {} and updatedUser : {}", userName, updatedUser);
            User userInDB = findByUserName(userName);
            if (userInDB != null) {
                if (!updatedUser.getUserName().isEmpty()) {
                    userInDB.setUserName(updatedUser.getUserName());
                }
                if (!updatedUser.getPassword().isEmpty()) {
                    userInDB.setPassword(encodePassword(updatedUser.getPassword()));
                }
                if (!updatedUser.getRoles().isEmpty()) {
                    userInDB.setRoles(updatedUser.getRoles());
                }
                addUser(userInDB);
                return userInDB;
            }
            return null;
        } catch (Exception e) {
            log.error("Error in editUser due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String removeUser(String userName) {
        try {
            log.info("Inside removeUser for userName : {}", userName);
            User userInDB = findByUserName(userName);
            userRepository.delete(userInDB);
            return "User deleted successfully.";
        } catch (Exception e) {
            log.error("Error in removeUser due to : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String saveAdmin(User adminUser) {
        try {
            log.info("Inside saveAdmin for user : {}", adminUser);
            adminUser.setRoles(Arrays.asList("ADMIN", "USER"));
            addNewUser(adminUser);
            return "New admin user added successfully.";
        } catch (RuntimeException e) {
            log.error("Error in saveAdmin due to  : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Weather getWeather(String username, String location) {
        try {
            log.info("Inside getWeather for location : {}", location);
            Weather weatherDetails = weatherService.getWeatherDetails(location);
            log.info("Weather for user : {} is {}", username, weatherDetails);
            return weatherDetails;
        } catch (RuntimeException e) {
            log.error("Error in getWeather due to  : {}", e.getMessage());
            return null;
        }
    }

    public Weather getWeatherByRedis(String username, String location) {
        try {
            log.info("Inside getWeatherByRedis for location : {}", location);
            Weather weatherDetails = weatherService.getWeatherDetailsRedis(location);
            log.info("WeatherRedis for user : {} is {}", username, weatherDetails);
            return weatherDetails;
        } catch (RuntimeException e) {
            log.error("Error in getWeatherByRedis due to  : {}", e.getMessage());
            return null;
        }
    }
}
