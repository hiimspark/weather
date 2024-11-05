package org.example.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {
    @GetMapping("/")
    public String main(Model model) {return "home";}
    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

}