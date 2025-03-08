package com.ims.IMS.api.weather;

public record CurrentWeather(
        String time,
        int interval,
        double temperature,
        double windspeed,
        int winddirection,
        int weathercode
) {}