package com.surpimi4.crud.advice;

import com.surpimi4.crud.model.User;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
public class ModelAdvice {
    @ModelAttribute
    public void addRolesToModel(@AuthenticationPrincipal User user, Model model, Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            Set<String> roles = authentication
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            model.addAttribute("loggedUser",user);
            model.addAttribute("roles", roles);
        }
    }

}
