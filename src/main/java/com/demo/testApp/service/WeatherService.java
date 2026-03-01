package com.demo.testApp.service;

import com.demo.testApp.entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WeatherService {

    //    replacing this with appCache
    @Value("${weather.api.uri}")
    private String weatherUri;

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public Weather getWeatherDetails(String city) {
        try {
            log.info("Inside getWeatherDetails for city : {}", city);
//            String finalApi = weatherUri.replace("API_KEY", apiKey).replace("Location", city);
            String baseApi = AppCache.keysList.WEATHER_API.toString();
            String finalApi = appCache.keyValue.get(baseApi).replace("API_KEY", apiKey).replace("Location", city);
            ResponseEntity<Weather> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, Weather.class);
            return response.getBody();
        } catch (RuntimeException e) {
            log.error("Error in getWeatherDetails due to : {}", e.getMessage());
            return null;
        }
    }

    public Weather getWeatherDetailsRedis(String city) {
        try {

            Weather weatherResponse = redisService.getData("weather_of_" + city, Weather.class);
            if (weatherResponse != null) {
                return weatherResponse;
            } else {

                log.info("Inside getWeatherDetailsRedis for city : {}", city);
                String baseApi = AppCache.keysList.WEATHER_API.toString();
                String finalApi = appCache.keyValue.get(baseApi).replace("API_KEY", apiKey).replace("Location", city);
                ResponseEntity<Weather> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, Weather.class);
                Weather responseData = response.getBody();
                if (responseData != null) {
                    redisService.setData("weather_of_" + city, responseData, 300l);
                }
                return responseData;
            }
        } catch (RuntimeException e) {
            log.error("Error in getWeatherDetailsRedis due to : {}", e.getMessage());
            return null;
        }
    }

}
