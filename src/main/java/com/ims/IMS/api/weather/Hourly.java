package com.ims.IMS.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Hourly(
        List<String> time,
        @JsonProperty("temperature_2m")
        List<Double> temperature2m,
        @JsonProperty("relative_humidity_2m")
        List<Integer> relativeHumidity2m,
        @JsonProperty("wind_speed_10m")
        List<Double> windSpeed10m
) {}
