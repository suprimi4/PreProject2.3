package com.surpimi4.crud.controller;

import com.surpimi4.crud.model.User;
import com.surpimi4.crud.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute @Valid User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }

        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurrentUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String updateCurrentUserForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "updateUser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateUser";
        }
        userService.updateUser(user);

        return "redirect:/users";
    }
}
