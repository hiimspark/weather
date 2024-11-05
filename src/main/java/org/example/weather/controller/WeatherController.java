package org.example.weather.controller;

import lombok.AllArgsConstructor;
import org.example.weather.dto.WeatherDTO;
import org.example.weather.entity.FavouriteCityEntity;
import org.example.weather.entity.UserEntity;
import org.example.weather.repository.FavouriteCityRepository;
import org.example.weather.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.example.weather.service.WeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    private final FavouriteCityRepository favouriteCityRepository;
    private final UserRepository userRepository;

    @PostMapping("/search-city")
    public RedirectView searchCity(@RequestParam("city") String city) {
        String lowercaseCity = city.toLowerCase();
        return new RedirectView("/weather/" + lowercaseCity);
    }

    @GetMapping("/weather/{city}")
    public String getWeather(@PathVariable String city, Model model) {
        WeatherDTO weatherDTO = weatherService.getCurrentWeather(city);
        model.addAttribute("weather", weatherDTO);
        return "cityView";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/weather")
    public String getFavouriteCitiesWeather(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FavouriteCityEntity> favouriteCities = favouriteCityRepository.findByUser(user);
        List<String> cityNames = favouriteCities.stream()
                .map(FavouriteCityEntity::getCity)
                .collect(Collectors.toList());

        List<WeatherDTO> weatherList = weatherService.getWeatherForCities(cityNames);
        model.addAttribute("weatherList", weatherList);

        return "weatherView";
    }

    @PostMapping("/weather/favourite")
    public String markAsFavourite(@RequestParam String city, Principal principal, RedirectAttributes redirectAttributes) {
        UserEntity user = userRepository.findByName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (favouriteCityRepository.existsByCityAndUser(city, user)) {
            redirectAttributes.addFlashAttribute("message", "City is already marked as favorite.");
        } else {
            FavouriteCityEntity favouriteCity = FavouriteCityEntity.builder()
                    .city(city)
                    .user(user)
                    .build();
            favouriteCityRepository.save(favouriteCity);
            redirectAttributes.addFlashAttribute("message", "City marked as favorite.");
        }

        return "redirect:/weather/" + city.toLowerCase();
    }


}
