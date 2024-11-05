package org.example.weather.controller;

import lombok.AllArgsConstructor;
import org.example.weather.entity.UserEntity;
import org.example.weather.exceptions.UsernameAlreadyExistsException;
import org.example.weather.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@AllArgsConstructor
public class ProfileController {
    private final UserService service;

    @PostMapping("/profile/change_username")
    public String changeUsernameProfile(@RequestParam("name") String name,
                                        Model model, Principal principal) {
        try {
            UserEntity updatedUser = service.changeName(principal, name);

            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = (UserDetails) currentAuth.getPrincipal();

            UserDetails updatedUserDetails = User.builder()
                    .username(updatedUser.getName())
                    .password(userDetails.getPassword())
                    .authorities(userDetails.getAuthorities())
                    .build();

            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);

            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return "redirect:/profile";

        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("errorName", e.getMessage());
            return "redirect:/profile?error";
        }
    }

    @PostMapping("/profile/change_password")
    public String changePasswordProfile(@RequestParam("password") String password,
                                        Model model, Principal principal) {
        service.changePass(principal, password);
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }
}
