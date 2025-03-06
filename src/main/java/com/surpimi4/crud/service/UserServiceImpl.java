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

    public void updatedUser(Integer id, User user) {
        User dbUser = findUserById(id);
        if (!user.getName().isEmpty()) {
            dbUser.setName(user.getName());
        }
        if (user.getAge() != null) {
            dbUser.setAge(user.getAge());
        }
        if (!user.getEmail().isEmpty()) {
            dbUser.setEmail(user.getEmail());
        }
        userRepository.save(dbUser);

    }

    public void saveUser(User user) {
        if (validateService.isUserMailExits(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с данной почтой уже существует");
        }
        if (validateService.isUserNameExist(user.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с данным именем уже существует");
        }
        userRepository.save(user);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}




