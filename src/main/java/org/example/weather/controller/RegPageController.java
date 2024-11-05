package org.example.weather.controller;

import lombok.AllArgsConstructor;
import org.example.weather.dto.UserDTO;
import org.example.weather.exceptions.UsernameAlreadyExistsException;
import org.example.weather.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class RegPageController {

    private final UserService service;

    @GetMapping("/registration_page")
    public String reg(Model model) {
        return "registration_page";
    }

    @PostMapping("/registration_page")
    public String regUser(@RequestParam("name") String name,
                          @RequestParam("password") String password,
                          Model model) {
        try {
            UserDTO userDTO = new UserDTO(name, password, "USER");
            service.addUser(userDTO);
            return "redirect:/home";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "registration_page";
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

}
