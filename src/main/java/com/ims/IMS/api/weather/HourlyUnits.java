package com.ims.IMS.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HourlyUnits(
        String time,
        @JsonProperty("temperature_2m")
        String temperature2m,
        @JsonProperty("relative_humidity_2m")
        String relativeHumidity2m,
        @JsonProperty("wind_speed_10m")
        String windSpeed10m
) {}
