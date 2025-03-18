package com.surpimi4.crud.controller;

import com.surpimi4.crud.dto.UserDTO;
import com.surpimi4.crud.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")

public class AdminController {

    private final UserServiceImpl userService;


    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserList() {
        return "user";
    }


    @GetMapping("/add")
    public String addUserForm() {
        return "addUser";
    }


    @GetMapping("/delete/{id}")
    public String deleteCurrentUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateCurrentUserForm(@PathVariable Integer id,
                                        Model model) {
        UserDTO existingUser = userService.findUserById(id);
        model.addAttribute("user", existingUser);
        return "updateUser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute @Valid UserDTO user,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "updateUser";
        }
        userService.updateUser(user);

        return "redirect:/admin";
    }
}
