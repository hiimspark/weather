package org.example.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.weather.dto.WeatherDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WeatherService {

    private final String apiKey = "9085a426f85d0de940060875314f691d";
    private final String apiUrl = "http://api.weatherstack.com/current";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public WeatherDTO getCurrentWeather(String location) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access_key", apiKey)
                .queryParam("query", location)
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root.has("success") && !root.get("success").asBoolean()) {
                throw new RuntimeException("Error: " + root.get("error").get("info").asText());
            }

            JsonNode current = root.get("current");

            WeatherDTO weatherDTO = new WeatherDTO();
            weatherDTO.setCity(root.get("location").get("name").asText());
            weatherDTO.setTemperature(current.get("temperature").asText());
            weatherDTO.setFeelsLike(current.get("feelslike").asText());
            weatherDTO.setWeatherDescription(current.get("weather_descriptions").get(0).asText());
            weatherDTO.setWindSpeed(current.get("wind_speed").asText());
            weatherDTO.setHumidity(current.get("humidity").asText());

            return weatherDTO;

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching the weather data.");
        }
    }

    public List<WeatherDTO> getWeatherForCities(List<String> cities) {
        return cities.stream()
                .map(city -> {
                    WeatherDTO weatherDTO = getCurrentWeather(city);
                    return weatherDTO;
                })
                .collect(Collectors.toList());
    }

}
