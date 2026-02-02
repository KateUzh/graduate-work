package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Сервис аутентификации и регистрации пользователей.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param login данные для входа
     * @return {@code true}, если аутентификация прошла успешно
     */
    @Override
    public boolean login(Login login) {
        UserEntity user = userRepository.findByEmail(login.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        return encoder.matches(login.getPassword(), user.getPassword());
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register данные для регистрации
     * @return {@code true}, если регистрация прошла успешно
     */
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
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role: " + register.getRole());
        }

        userRepository.save(user);
        return true;
    }
}
