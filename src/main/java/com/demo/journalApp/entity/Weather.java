package com.demo.journalApp.entity;


import com.demo.journalApp.entity.weather.Current;
import com.demo.journalApp.entity.weather.Location;
import lombok.Data;

@Data
public class Weather {
    private Location location;
    private Current current;
}

