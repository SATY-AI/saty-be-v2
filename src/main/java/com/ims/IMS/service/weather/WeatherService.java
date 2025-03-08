package com.ims.IMS.service.weather;

import com.ims.IMS.api.weather.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WeatherService {
    private final WebClient webClient;
    private static final String DEFAULT_CURRENT = "temperature_2m,relative_humidity_2m,wind_speed_10m";

    private String apiUrl = "https://api.open-meteo.com/v1/forecast";

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    // Fetch weather using Open Meteo API by latitude and longitude
    public Mono<WeatherResponse> getWeatherByCoordinates(double latitude, double longitude, String timezone) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current_weather", true)
                        .queryParam("hourly", DEFAULT_CURRENT)
                        .queryParam("timezone", timezone)
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .doOnNext(response -> {
                    // Log or print the result
                    log.info("Weather API response: {}", response);  // Logging the response
                    System.out.println("Weather API response: " + response);  // Printing the response
                });
    }
}
