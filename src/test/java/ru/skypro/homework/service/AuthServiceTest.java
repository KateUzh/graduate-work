package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Тесты для AuthService.
 * Проверяют логику аутентификации и регистрации пользователей.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntity testUser;
    private Login loginDto;
    private Register registerDto;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@test.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhone("+79991234567");
        testUser.setRole(UserEntity.Role.USER);

        loginDto = new Login();
        loginDto.setUsername("test@test.com");
        loginDto.setPassword("password123");

        registerDto = new Register();
        registerDto.setUsername("newuser@test.com");
        registerDto.setPassword("newpassword");
        registerDto.setFirstName("Jane");
        registerDto.setLastName("Smith");
        registerDto.setPhone("+79997654321");
        registerDto.setRole(Register.RoleEnum.USER);
    }

    // ==================== Тесты login ====================

    @Test
    @DisplayName("login должен вернуть true при корректных credentials")
    void login_ShouldReturnTrue_WhenCredentialsAreCorrect() {
        when(userRepository.findByEmail(loginDto.getUsername()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginDto.getPassword(), testUser.getPassword()))
                .thenReturn(true);

        boolean result = authService.login(loginDto);

        assertThat(result).isTrue();
        verify(userRepository).findByEmail(loginDto.getUsername());
        verify(passwordEncoder).matches(loginDto.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("login должен вернуть false при неверном пароле")
    void login_ShouldReturnFalse_WhenPasswordIsIncorrect() {
        when(userRepository.findByEmail(loginDto.getUsername()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginDto.getPassword(), testUser.getPassword()))
                .thenReturn(false);

        boolean result = authService.login(loginDto);

        assertThat(result).isFalse();
        verify(passwordEncoder).matches(loginDto.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("login должен выбросить исключение если пользователь не найден")
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(loginDto.getUsername()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail(loginDto.getUsername());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("login должен обработать null username")
    void login_ShouldHandleNullUsername() {
        loginDto.setUsername(null);
        when(userRepository.findByEmail(null))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(ResponseStatusException.class);

        verify(userRepository).findByEmail(null);
    }

    // ==================== Тесты register ====================

    @Test
    @DisplayName("register должен успешно зарегистрировать нового пользователя")
    void register_ShouldSuccessfullyRegisterNewUser() {
        when(passwordEncoder.encode(registerDto.getPassword()))
                .thenReturn("encodedNewPassword");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = authService.register(registerDto);

        assertThat(result).isTrue();
        verify(passwordEncoder).encode(registerDto.getPassword());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("register должен закодировать пароль")
    void register_ShouldEncodePassword() {
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(registerDto.getPassword()))
                .thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity saved = invocation.getArgument(0);
                    assertThat(saved.getPassword()).isEqualTo(encodedPassword);
                    return saved;
                });

        authService.register(registerDto);

        verify(passwordEncoder).encode(registerDto.getPassword());
    }

    @Test
    @DisplayName("register должен сохранить все поля пользователя")
    void register_ShouldSaveAllUserFields() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity saved = invocation.getArgument(0);
                    assertThat(saved.getEmail()).isEqualTo(registerDto.getUsername());
                    assertThat(saved.getFirstName()).isEqualTo(registerDto.getFirstName());
                    assertThat(saved.getLastName()).isEqualTo(registerDto.getLastName());
                    assertThat(saved.getPhone()).isEqualTo(registerDto.getPhone());
                    assertThat(saved.getRole()).isEqualTo(UserEntity.Role.USER);
                    return saved;
                });

        authService.register(registerDto);

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("register должен установить роль USER")
    void register_ShouldSetUserRole() {
        registerDto.setRole(Register.RoleEnum.USER);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity saved = invocation.getArgument(0);
                    assertThat(saved.getRole()).isEqualTo(UserEntity.Role.USER);
                    return saved;
                });

        authService.register(registerDto);

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("register должен установить роль ADMIN")
    void register_ShouldSetAdminRole() {
        registerDto.setRole(Register.RoleEnum.ADMIN);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity saved = invocation.getArgument(0);
                    assertThat(saved.getRole()).isEqualTo(UserEntity.Role.ADMIN);
                    return saved;
                });

        authService.register(registerDto);

        verify(userRepository).save(any(UserEntity.class));
    }

    // ==================== Тесты граничных случаев ====================

    @Test
    @DisplayName("login должен обработать пустой пароль")
    void login_ShouldHandleEmptyPassword() {
        loginDto.setPassword("");
        when(userRepository.findByEmail(loginDto.getUsername()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("", testUser.getPassword()))
                .thenReturn(false);

        boolean result = authService.login(loginDto);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("register должен обработать минимальные данные")
    void register_ShouldHandleMinimalData() {
        Register minimal = new Register();
        minimal.setUsername("min@test.com");
        minimal.setPassword("pass");
        minimal.setRole(Register.RoleEnum.USER);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = authService.register(minimal);

        assertThat(result).isTrue();
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("login должен корректно работать с разными email")
    void login_ShouldWorkWithDifferentEmails() {
        String[] emails = {
                "test@test.com",
                "user@example.org",
                "admin@company.ru"
        };

        for (String email : emails) {
            loginDto.setUsername(email);
            testUser.setEmail(email);

            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches(anyString(), anyString()))
                    .thenReturn(true);

            boolean result = authService.login(loginDto);

            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("register должен вызвать save ровно один раз")
    void register_ShouldCallSaveExactlyOnce() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(registerDto);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("login должен вызвать findByEmail ровно один раз")
    void login_ShouldCallFindByEmailExactlyOnce() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        authService.login(loginDto);

        verify(userRepository, times(1)).findByEmail(loginDto.getUsername());
    }
}
