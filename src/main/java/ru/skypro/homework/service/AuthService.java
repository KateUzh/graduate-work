package ru.skypro.homework.service;

import ru.skypro.homework.model.Login;
import ru.skypro.homework.model.Register;

public interface AuthService {
    boolean login(Login login);

    boolean register(Register register);
}
