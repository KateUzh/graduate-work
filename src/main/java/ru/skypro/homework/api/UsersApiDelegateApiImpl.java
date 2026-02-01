package ru.skypro.homework.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.impl.UserServiceImpl;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateApiImpl implements UsersApiDelegate {

    private final UserServiceImpl userService;

    @Override
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    public ResponseEntity<Void> setPassword(NewPassword newPassword, Authentication auth) {
        userService.changePassword(newPassword, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UpdateUser> updateUser(UpdateUser updateUser) {
        User updated = userService.updateUser(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @Override
    public ResponseEntity<Void> updateUserImage(MultipartFile image) {
        userService.updateImage(image);
        return ResponseEntity.ok().build();
    }
}
