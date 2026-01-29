package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.model.Login;
import ru.skypro.homework.model.Register;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.MyUserDetailsService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(Login login) {
        UserEntity user = userRepository.findByEmail(login.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));
        return encoder.matches(login.getPassword(), user.getPassword());
    }

    @Override
    public boolean register(Register register) {
        UserEntity user = new UserEntity();
        user.setPassword(encoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setEmail(register.getUsername());
        user.setPhone(register.getPhone());
        try {
            user.setRole(UserEntity.Role.valueOf(register.getRole().name()));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Unknown role: " + register.getRole());
        }

        userRepository.save(user);
        return true;
    }
}
