package com.ims.IMS.api.weather;

public record CurrentWeatherUnits(
        String time,
        String interval,
        String temperature,
        String windspeed,
        String winddirection,
        String weathercode
) {}
