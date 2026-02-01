package ru.skypro.homework.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class RegisterApiDelegateImpl implements RegisterApiDelegate {

    private final AuthService authService;

    @Override
    public ResponseEntity<Void> register(Register register) {
        authService.register(register);
        return ResponseEntity.ok().build();
    }
}
