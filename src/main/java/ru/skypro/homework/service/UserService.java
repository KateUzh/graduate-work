package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {

    User getCurrentUser(Authentication auth);

    User updateUser(UpdateUser updateUser, Authentication auth);

    void changePassword(NewPassword newPassword, Authentication auth);

    void updateUserImage(MultipartFile image, Authentication auth);
}
