package com.surpimi4.crud.service;

import com.surpimi4.crud.dto.UserDTO;
import com.surpimi4.crud.model.User;
import com.surpimi4.crud.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getPassword()))
                .collect(Collectors.toList());
    }


    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void updateUser(UserDTO userDTO) {
        Optional<User> dbUser = userRepository.findById(userDTO.getId());
        if (dbUser.isPresent()) {
            User user = dbUser.get();
            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }

            if (userDTO.getAge() != null) {
                user.setAge(userDTO.getAge());
            }

            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
            }

            userRepository.save(user);
        }


    }

    public void saveUser(UserDTO userDTO) {
        Optional<User> dbUser = userRepository.findById(userDTO.getId());
        User user = new User(userDTO.getId(), userDTO.getName(), userDTO.getAge(), userDTO.getEmail());
        userRepository.save(user);
    }

    public UserDTO findByName(String name) {
        return userRepository.findByName(name).map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail())).orElse(null);
    }

    public UserDTO findUserById(Integer id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getPassword()))
                .orElse(null);
    }


}




