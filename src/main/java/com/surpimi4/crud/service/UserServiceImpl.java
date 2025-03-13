package com.surpimi4.crud.service;

import com.surpimi4.crud.dto.UserDTO;
import com.surpimi4.crud.model.Role;
import com.surpimi4.crud.model.User;
import com.surpimi4.crud.repository.RoleRepository;
import com.surpimi4.crud.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getPassword(), user.getRoles()))
                .collect(Collectors.toList());
    }


    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void updateUser(UserDTO userDTO) {

        Set<Role> roles = userDTO.getRole().stream()
                .map(roleName -> roleRepository.findByRole(roleName)
                        .orElse(null))
                .collect(Collectors.toSet());
        userDTO.setRoles(roles);

        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashedPassword);

        Optional<User> dbUser = userRepository.findById(userDTO.getId());
        if (dbUser.isPresent()) {
            User user = dbUser.get();
            user.setName(userDTO.getName());
            user.setAge(userDTO.getAge());
            user.setEmail(userDTO.getEmail());
            user.setPassword(hashedPassword);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    public void addUser(UserDTO userDTO) {
        Set<Role> roles = userDTO.getRole().stream()
                .map(roleName -> roleRepository.findByRole(roleName)
                        .orElse(null))
                .collect(Collectors.toSet());
        userDTO.setRoles(roles);
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getName(), hashedPassword, userDTO.getAge(), userDTO.getEmail(), roles);
        userRepository.save(user);
    }

    public UserDTO findUserByName(String name) {
        return userRepository.findByName(name).map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail())).orElse(null);
    }

    public UserDTO findUserById(Integer id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail()))
                .orElse(null);
    }

    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email уже используется.");
        }
        if (userRepository.existsByName(userDTO.getName())) {
            throw new IllegalArgumentException("Имя уже занято.");
        }
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashedPassword);

        Optional<Role> userOptionalRole = roleRepository.findByRole("USER");
        if (userOptionalRole.isPresent()) {
            Role userRole = userOptionalRole.get();
            Set<Role> roles = Set.of(userRole);
            userDTO.setRoles(roles);

        }

        User user = new User(userDTO.getName(), userDTO.getPassword(), userDTO.getAge(), userDTO.getEmail(), userDTO.getRoles());
        userRepository.save(user);

    }

}




