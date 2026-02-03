package ru.skypro.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для AdRepository.
 * Проверяют работу с базой данных.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Тесты AdRepository")
class AdRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdRepository adRepository;

    private UserEntity testUser;
    private AdEntity testAd;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("+79991234567");
        testUser.setRole(UserEntity.Role.USER);
        entityManager.persist(testUser);

        testAd = new AdEntity();
        testAd.setTitle("Продам гараж");
        testAd.setPrice(500000);
        testAd.setDescription("Отличный гараж в центре");
        testAd.setAuthor(testUser);
    }

    @Test
    @DisplayName("Должен найти все объявления пользователя по ID")
    void shouldFindAllAdsByAuthorId() {
        // Arrange
        AdEntity ad1 = new AdEntity();
        ad1.setTitle("Объявление 1");
        ad1.setPrice(100000);
        ad1.setAuthor(testUser);
        entityManager.persist(ad1);

        AdEntity ad2 = new AdEntity();
        ad2.setTitle("Объявление 2");
        ad2.setPrice(200000);
        ad2.setAuthor(testUser);
        entityManager.persist(ad2);

        entityManager.flush();

        // Act
        List<AdEntity> result = adRepository.findAllByAuthorId(testUser.getId());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(AdEntity::getTitle)
                .containsExactlyInAnyOrder("Объявление 1", "Объявление 2");
    }

    @Test
    @DisplayName("Должен вернуть пустой список если у пользователя нет объявлений")
    void shouldReturnEmptyListWhenUserHasNoAds() {
        // Act
        List<AdEntity> result = adRepository.findAllByAuthorId(testUser.getId());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен сохранить новое объявление")
    void shouldSaveNewAd() {
        // Act
        AdEntity saved = adRepository.save(testAd);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Продам гараж");
        assertThat(saved.getAuthor()).isEqualTo(testUser);

        AdEntity found = entityManager.find(AdEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Продам гараж");
    }

    @Test
    @DisplayName("Должен обновить существующее объявление")
    void shouldUpdateExistingAd() {
        // Arrange
        entityManager.persist(testAd);
        entityManager.flush();

        // Act
        testAd.setTitle("Обновленное название");
        testAd.setPrice(600000);
        AdEntity updated = adRepository.save(testAd);

        // Assert
        assertThat(updated.getTitle()).isEqualTo("Обновленное название");
        assertThat(updated.getPrice()).isEqualTo(600000);

        AdEntity found = entityManager.find(AdEntity.class, updated.getId());
        assertThat(found.getTitle()).isEqualTo("Обновленное название");
    }

    @Test
    @DisplayName("Должен удалить объявление")
    void shouldDeleteAd() {
        // Arrange
        entityManager.persist(testAd);
        entityManager.flush();
        Integer adId = testAd.getId();

        // Act
        adRepository.delete(testAd);

        // Assert
        AdEntity found = entityManager.find(AdEntity.class, adId);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Должен найти объявление по ID")
    void shouldFindAdById() {
        // Arrange
        entityManager.persist(testAd);
        entityManager.flush();

        // Act
        Optional<AdEntity> result = adRepository.findById(testAd.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Продам гараж");
    }

    @Test
    @DisplayName("Должен найти все объявления")
    void shouldFindAllAds() {
        // Arrange
        entityManager.persist(testAd);

        AdEntity ad2 = new AdEntity();
        ad2.setTitle("Продам квартиру");
        ad2.setPrice(3000000);
        ad2.setAuthor(testUser);
        entityManager.persist(ad2);

        entityManager.flush();

        // Act
        List<AdEntity> result = adRepository.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }
}
