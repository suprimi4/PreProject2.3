package com.surpimi4.crud.service;

import com.surpimi4.crud.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidateService {
    private final UserRepository userRepository;

    public UserValidateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAgeValid(Integer age) {
        return age > 0;
    }

    public boolean isEmailValid(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$", email);
    }


    public boolean isUserMailExits(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUserNameExist(String name) {
        return userRepository.existsByName(name);
    }
}
