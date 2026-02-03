package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

/**
 * REST контроллер для аутентификации и регистрации пользователей.
 * Предоставляет публичные endpoints для входа и регистрации.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    /**
     * Аутентифицирует пользователя в системе.
     *
     * @param login данные для входа (email и пароль)
     * @return 200 OK если аутентификация успешна, 401 UNAUTHORIZED если данные неверны
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Login login) {
        if (authService.login(login)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register данные для регистрации (email, пароль, имя, фамилия, телефон, роль)
     * @return 201 CREATED если регистрация успешна, 400 BAD_REQUEST если пользователь уже существует
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}