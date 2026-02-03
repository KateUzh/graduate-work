package ru.skypro.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для UserRepository.
 * Проверяют работу с базой данных.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Тесты UserRepository")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("+79991234567");
        testUser.setRole(UserEntity.Role.USER);
    }

    @Test
    @DisplayName("Должен найти пользователя по email")
    void shouldFindUserByEmail() {
        // Arrange
        entityManager.persist(testUser);
        entityManager.flush();

        // Act
        Optional<UserEntity> result = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getFirstName()).isEqualTo("Иван");
    }

    @Test
    @DisplayName("Должен вернуть пустой Optional если пользователь не найден")
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        // Act
        Optional<UserEntity> result = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен сохранить нового пользователя")
    void shouldSaveNewUser() {
        // Act
        UserEntity saved = userRepository.save(testUser);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");

        UserEntity found = entityManager.find(UserEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Должен обновить существующего пользователя")
    void shouldUpdateExistingUser() {
        // Arrange
        entityManager.persist(testUser);
        entityManager.flush();

        // Act
        testUser.setFirstName("Петр");
        testUser.setLastName("Петров");
        UserEntity updated = userRepository.save(testUser);

        // Assert
        assertThat(updated.getFirstName()).isEqualTo("Петр");
        assertThat(updated.getLastName()).isEqualTo("Петров");

        UserEntity found = entityManager.find(UserEntity.class, updated.getId());
        assertThat(found.getFirstName()).isEqualTo("Петр");
    }

    @Test
    @DisplayName("Должен удалить пользователя")
    void shouldDeleteUser() {
        // Arrange
        entityManager.persist(testUser);
        entityManager.flush();
        Integer userId = testUser.getId();

        // Act
        userRepository.delete(testUser);

        // Assert
        UserEntity found = entityManager.find(UserEntity.class, userId);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Должен найти пользователя с ролью ADMIN")
    void shouldFindAdminUser() {
        // Arrange
        testUser.setRole(UserEntity.Role.ADMIN);
        entityManager.persist(testUser);
        entityManager.flush();

        // Act
        Optional<UserEntity> result = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(UserEntity.Role.ADMIN);
    }
}
