package ru.skypro.homework.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Login;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class LoginApiDelegateImpl implements LoginApiDelegate {

    private final AuthService authService;

    @Override
    public ResponseEntity<Void> login(Login login){
        return ResponseEntity.ok().build();
    }
}
