package com.nhom03.mockproject.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class GetWeather {
    private final String API_KEY = "c6fcab79ab7a6ac41eba1ed28b4663cb";
    @GetMapping("/api/weather/danang")
    public Object getDaNangTemperature() {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("q", "Da Nang")
                .queryParam("appid", API_KEY)
                .queryParam("units", "metric")
                .toUriString();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> main = (Map<String, Object>) response.get("main");
            return Map.of(
                    "status", "success",
                    "city", "Đà Nẵng",
                    "temp", main.get("temp")
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            return Map.of(
                    "status", "error",
                    "message", "API Key của bạn không hợp lệ."
            );
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }
}
