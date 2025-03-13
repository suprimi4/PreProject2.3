package com.surpimi4.crud.controller;

import com.surpimi4.crud.dto.UserDTO;
import com.surpimi4.crud.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class DefaultUserController {

    private final UserServiceImpl userService;


    public DefaultUserController(UserServiceImpl userService) {
        this.userService = userService;

    }

    @GetMapping
    public String getCurrentUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDTO user = userService.findUserByName(username);
        model.addAttribute("user", user);

        return "currentUser";
    }

    @GetMapping("/register")
    public String getRegisterForm() {
        return "registerForm";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid UserDTO user, Model model) {

        try {
            userService.registerUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registerForm";
        }

        return "redirect:/login";
    }
}
