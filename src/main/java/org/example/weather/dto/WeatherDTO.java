package org.example.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {
    private String city;
    private String temperature;
    private String feelsLike;
    private String weatherDescription;
    private String windSpeed;
    private String humidity;
}
