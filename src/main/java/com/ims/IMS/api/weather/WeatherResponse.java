package com.ims.IMS.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
        double latitude,
        double longitude,
        String timezone,
        String timezoneAbbreviation,
        @JsonProperty("current_weather_units")
        CurrentWeatherUnits currentWeatherUnits,
        @JsonProperty("current_weather")
        CurrentWeather currentWeather,
        @JsonProperty("hourly_units")
        HourlyUnits hourlyUnits,
        Hourly hourly
) {}
