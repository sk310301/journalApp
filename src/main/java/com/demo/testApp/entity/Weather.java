package com.demo.testApp.entity;


import com.demo.testApp.entity.weather.Current;
import com.demo.testApp.entity.weather.Location;
import lombok.Data;

@Data
public class Weather {
    private Location location;
    private Current current;
}

