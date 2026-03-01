package com.demo.journalApp.entity.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {
    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    @JsonProperty("tz_id")
    private String tzId;
    @JsonProperty("localtime_epoch")
    private int localtimeEpoch;
    private String localtime;
}
