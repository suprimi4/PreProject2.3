package com.surpimi4.crud.service;

import com.surpimi4.crud.model.User;
import com.surpimi4.crud.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserValidateService validateService;

    public UserServiceImpl(UserRepository userRepository, UserValidateService validateService) {
        this.userRepository = userRepository;
        this.validateService = validateService;
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        validateUser(user);
        userRepository.save(user);

    }

    public void saveUser(User user) {
        checkIfUserExists(user);
        validateUser(user);
        userRepository.save(user);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    private void validateUser(User user) {
        if (!validateService.isAgeValid(user.getAge())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Возраст пользователя отрицательный");
        }
        if (!validateService.isEmailValid(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная почта пользователя");
        }
    }

    private void checkIfUserExists(User user) {
        if (validateService.isUserMailExits(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с данной почтой уже существует");
        }
        if (validateService.isUserNameExist(user.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с данным именем уже существует");
        }
    }

}




