package org.example.weather.service;

import lombok.AllArgsConstructor;
import org.example.weather.dto.UserDTO;
import org.example.weather.entity.UserEntity;
import org.example.weather.exceptions.UsernameAlreadyExistsException;
import org.example.weather.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserEntity addUser(UserDTO dto){
        if (userRepository.existsByName(dto.getName())) {
            throw new UsernameAlreadyExistsException("Username is already taken.");
        }

        UserEntity user = UserEntity.builder()
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(dto.getRoles())
                .build();
        return userRepository.save(user);
    }

    public List<UserEntity> readAll() {
        return userRepository.findAll();
    }

    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity changeName(Principal principal, String username){
        if (userRepository.existsByName(username)) {
            throw new UsernameAlreadyExistsException("Username is already taken.");
        }
        getUserByPrincipal(principal).setName(username);
        return userRepository.save(getUserByPrincipal(principal));

    }

    public UserEntity changePass(Principal principal, String password){
        getUserByPrincipal(principal).setPassword(passwordEncoder.encode(password));
        return userRepository.save(getUserByPrincipal(principal));

    }

    /*public UserEntity update(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getName().equals(dto.getName()) && userRepository.existsByName(dto.getName())) {
            throw new UsernameAlreadyExistsException("Username is already taken.");
        }

        existingUser.setName(dto.getName());
        existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        existingUser.setRoles(dto.getRoles());
        return userRepository.save(existingUser);
    }*/

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity getUserByPrincipal(Principal principal) {
        if (principal == null) return null;
        return userRepository.findByName(principal.getName()).get();
    }
}
