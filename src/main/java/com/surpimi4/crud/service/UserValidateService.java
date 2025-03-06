package com.surpimi4.crud.service;

import com.surpimi4.crud.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService {
    private final UserRepository userRepository;

    public UserValidateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserMailExits(String email) {
        return userRepository.existsByEmail(email);
    }

    public  boolean isUserNameExist(String name) {
        return  userRepository.existsByName(name);
    }
}
