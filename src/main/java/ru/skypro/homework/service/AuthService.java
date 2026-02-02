package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

/**
 * Сервис аутентификации и регистрации пользователей.
 */
public interface AuthService {

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param login данные для входа
     * @return {@code true}, если аутентификация прошла успешно
     */
    boolean login(Login login);

    /**
     * Регистрирует нового пользователя.
     *
     * @param register данные для регистрации
     * @return {@code true}, если регистрация прошла успешно
     */
    boolean register(Register register);
}
