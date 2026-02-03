package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для UserServiceImpl.
 * Проверяют бизнес-логику работы с пользователями.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты UserServiceImpl")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity testUser;
    private User testUserDto;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("+79991234567");
        testUser.setRole(UserEntity.Role.USER);

        testUserDto = new User();
        testUserDto.setId(1);
        testUserDto.setEmail("test@example.com");
        testUserDto.setFirstName("Иван");
        testUserDto.setLastName("Иванов");
        testUserDto.setPhone("+79991234567");

        customUserDetails = new CustomUserDetails(testUser);
    }

    @Nested
    @DisplayName("Тесты получения текущего пользователя")
    class GetCurrentUserTests {

        @Test
        @DisplayName("Должен успешно вернуть текущего пользователя")
        void shouldReturnCurrentUser() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // Act
            User result = userService.getCurrentUser(authentication);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("test@example.com");
            verify(userMapper).toDto(testUser);
        }

        @Test
        @DisplayName("Должен выбросить исключение если аутентификация null")
        void shouldThrowExceptionWhenAuthIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> userService.getCurrentUser(null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> userService.getCurrentUser(authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если principal не CustomUserDetails")
        void shouldThrowExceptionWhenPrincipalIsNotCustomUserDetails() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn("some string");

            // Act & Assert
            assertThatThrownBy(() -> userService.getCurrentUser(authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты обновления пользователя")
    class UpdateUserTests {

        private UpdateUser updateUserDto;

        @BeforeEach
        void setUp() {
            updateUserDto = new UpdateUser();
            updateUserDto.setFirstName("Петр");
            updateUserDto.setLastName("Петров");
            updateUserDto.setPhone("+79997654321");
        }

        @Test
        @DisplayName("Должен успешно обновить пользователя")
        void shouldUpdateUserSuccessfully() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // Act
            User result = userService.updateUser(updateUserDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(userMapper).updateFromDto(updateUserDto, testUser);
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("Должен выбросить исключение при обновлении без аутентификации")
        void shouldThrowExceptionWhenUpdatingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> userService.updateUser(updateUserDto, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUser(updateUserDto, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты смены пароля")
    class ChangePasswordTests {

        private NewPassword newPasswordDto;

        @BeforeEach
        void setUp() {
            newPasswordDto = new NewPassword();
            newPasswordDto.setCurrentPassword("oldPassword123");
            newPasswordDto.setNewPassword("newPassword456");
        }

        @Test
        @DisplayName("Должен успешно сменить пароль")
        void shouldChangePasswordSuccessfully() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(passwordEncoder.encode("newPassword456")).thenReturn("encodedNewPassword");
            when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

            // Act
            userService.changePassword(newPasswordDto, authentication);

            // Assert
            verify(passwordEncoder).encode("newPassword456");
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("Должен выбросить исключение при смене пароля без аутентификации")
        void shouldThrowExceptionWhenChangingPasswordWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> userService.changePassword(newPasswordDto, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> userService.changePassword(newPasswordDto, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты обновления изображения пользователя")
    class UpdateUserImageTests {

        @Mock
        private MultipartFile mockImage;

        @Test
        @DisplayName("Должен успешно обновить изображение пользователя")
        void shouldUpdateUserImageSuccessfully() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(mockImage.isEmpty()).thenReturn(false);
            when(imageService.saveUserImage(mockImage)).thenReturn("new-image.jpg");
            when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

            // Act
            userService.updateUserImage(mockImage, authentication);

            // Assert
            verify(imageService).saveUserImage(mockImage);
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение null")
        void shouldThrowExceptionWhenImageIsNull() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUserImage(null, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("400 BAD_REQUEST");
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение пустое")
        void shouldThrowExceptionWhenImageIsEmpty() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(mockImage.isEmpty()).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUserImage(mockImage, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("400 BAD_REQUEST");
        }

        @Test
        @DisplayName("Должен выбросить исключение при обновлении изображения без аутентификации")
        void shouldThrowExceptionWhenUpdatingImageWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> userService.updateUserImage(mockImage, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUserImage(mockImage, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }
}
