package ru.skypro.homework.api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateApiImpl implements UsersApiDelegate {

    private final UserService userService;

    @Override
    public ResponseEntity<User> getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.getCurrentUser(auth));
    }

    @Override
    public ResponseEntity<Void> setPassword(NewPassword newPassword, Authentication authentication) {
        userService.changePassword(newPassword, authentication);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UpdateUser> updateUser(UpdateUser updateUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User updated = userService.updateUser(updateUser, auth);
        UpdateUser response = new UpdateUser()
                .firstName(updated.getFirstName())
                .lastName(updated.getLastName())
                .phone(updated.getPhone());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> updateUserImage(MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.updateUserImage(image, auth);
        return ResponseEntity.ok().build();
    }
}

