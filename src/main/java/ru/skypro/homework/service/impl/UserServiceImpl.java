package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

/**
 * Сервис для работы с пользователями.
 * Содержит бизнес-логику получения и обновления профиля, смены пароля и обновления изображения профиля.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           ImageService imageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    /**
     * Получает профиль текущего пользователя.
     *
     * @param auth данные аутентификации
     * @return DTO пользователя
     */
    @Override
    public User getCurrentUser(Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return userMapper.toDto(user);
    }

    /**
     * Обновляет профиль текущего пользователя.
     *
     * @param updateUser новые данные
     * @param auth       данные аутентификации
     * @return обновленный профиль
     */
    @Override
    public User updateUser(UpdateUser updateUser, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        userMapper.updateFromDto(updateUser, user);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Меняет пароль текущего пользователя.
     *
     * @param newPassword новые данные пароля
     * @param auth        данные аутентификации
     */
    @Override
    public void changePassword(NewPassword newPassword, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Обновляет изображение профиля пользователя.
     *
     * @param image файл изображения
     * @param auth  данные аутентификации
     */
    @Override
    public void updateUserImage(MultipartFile image, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is empty");
        }

        String filename = imageService.saveUserImage(image);
        user.setImage(filename);

        userRepository.save(user);
    }

    private UserEntity getUserFromAuth(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }
}
