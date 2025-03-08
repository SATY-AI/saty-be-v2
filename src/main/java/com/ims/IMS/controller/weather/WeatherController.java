package com.ims.IMS.controller.weather;


import com.ims.IMS.api.weather.WeatherResponse;
import com.ims.IMS.exception.InvalidTokenException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ims")
public class WeatherController {
    private final WeatherService weatherService;
    @Autowired
    private CustomerService customerService; // Service for validating the token

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Mono<ResponseApi<WeatherResponse>> getWeatherByCoordinates(
            @RequestHeader("Authorization") String token,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false, defaultValue = "auto") String timezone) {
        return Mono.fromCallable(() -> customerService.validateTokenAndGetCustomer(token))
                .flatMap(customer -> weatherService.getWeatherByCoordinates(lat, lon, timezone)
                        .map(weatherResponse -> ResponseApi.success(weatherResponse))
                        .defaultIfEmpty(ResponseApi.error("404", "Weather data not found")))
                .onErrorResume(InvalidTokenException.class, e -> Mono.just(ResponseApi.error("401", "Invalid or expired token")))
                .onErrorResume(Exception.class, e -> Mono.just(ResponseApi.error("500", "Internal server error")));
    }
}
