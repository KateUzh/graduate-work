package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для MyUserDetailsService.
 * Проверяют загрузку пользователя для Spring Security.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты MyUserDetailsService")
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setRole(UserEntity.Role.USER);
    }

    @Test
    @DisplayName("Должен успешно загрузить пользователя по email")
    void shouldLoadUserByUsername() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails result = myUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("test@example.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Должен загрузить пользователя с ролью ADMIN")
    void shouldLoadAdminUser() {
        // Arrange
        testUser.setRole(UserEntity.Role.ADMIN);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails result = myUserDetailsService.loadUserByUsername("admin@example.com");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Должен выбросить исключение если пользователь не найден")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("Должен выбросить исключение если email null")
    void shouldThrowExceptionWhenEmailIsNull() {
        // Arrange
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение если email пустой")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        // Arrange
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
