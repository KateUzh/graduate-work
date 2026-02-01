package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;

public interface UserService {

    User getCurrentUser(Authentication auth);

    User updateUser(UpdateUser updateUser, Authentication auth);

    void changePassword(NewPassword newPassword, Authentication auth);

    void updateUserImage(MultipartFile image, Authentication auth);
}
