package ru.skypro.homework.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.service.ImageService;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateApiImpl implements UsersApiDelegate {

    private final UserServiceImpl userService;
    private final ImageService imageService;

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

    public ResponseEntity<byte[]> getUserImage(@PathVariable String filename) {
        byte[] imageBytes = imageService.loadUserImage(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
