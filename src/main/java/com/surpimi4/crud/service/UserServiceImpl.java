package com.surpimi4.crud.service;

import com.surpimi4.crud.model.User;
import com.surpimi4.crud.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserServiceImpl {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {

        userRepository.save(user);

    }

    public void saveUser(User user) {

        userRepository.save(user);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }


}




