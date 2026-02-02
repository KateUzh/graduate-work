package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

/**
 * Сервис для работы с пользователями.
 * Содержит бизнес-логику получения и обновления профиля пользователя,
 * смены пароля и обновления изображения профиля.
 */
public interface UserService {

    /**
     * Получает профиль текущего авторизованного пользователя.
     *
     * @param auth данные аутентификации пользователя
     * @return профиль пользователя
     */
    User getCurrentUser(Authentication auth);

    /**
     * Обновляет данные профиля пользователя.
     *
     * @param updateUser новые данные пользователя
     * @param auth данные аутентификации пользователя
     * @return обновлённый профиль пользователя
     */
    User updateUser(UpdateUser updateUser, Authentication auth);

    /**
     * Изменяет пароль текущего пользователя.
     *
     * @param newPassword данные для смены пароля
     * @param auth данные аутентификации пользователя
     */
    void changePassword(NewPassword newPassword, Authentication auth);

    /**
     * Обновляет изображение профиля пользователя.
     *
     * @param image новое изображение пользователя
     * @param auth данные аутентификации пользователя
     */
    void updateUserImage(MultipartFile image, Authentication auth);
}
