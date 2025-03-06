package com.surpimi4.crud.controller;

import com.surpimi4.crud.model.User;
import com.surpimi4.crud.repository.UserRepository;
import com.surpimi4.crud.service.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;


    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getUserList(Model model) {
        List<User> user = userService.findAllUsers();
        model.addAttribute("users", user);

        return "user";
    }

    @GetMapping("/add")
    public String addUserForm() {
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
            return "redirect:/users";
        }

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurrentUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String updateCurrentUserForm(@PathVariable Integer id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute User user) {
        userService.updatedUser(id, user);
        return "redirect:/users";
    }
}
