package ru.skypro.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для CommentRepository.
 * Проверяют работу с базой данных.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Тесты CommentRepository")
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private UserEntity testUser;
    private AdEntity testAd;
    private CommentEntity testComment;

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
        testAd.setDescription("Отличный гараж");
        testAd.setAuthor(testUser);
        entityManager.persist(testAd);

        testComment = new CommentEntity();
        testComment.setText("Отличное предложение!");
        testComment.setAuthor(testUser);
        testComment.setAd(testAd);
    }

    @Test
    @DisplayName("Должен найти все комментарии объявления по ID")
    void shouldFindAllCommentsByAdId() {
        // Arrange
        CommentEntity comment1 = new CommentEntity();
        comment1.setText("Комментарий 1");
        comment1.setAuthor(testUser);
        comment1.setAd(testAd);
        entityManager.persist(comment1);

        CommentEntity comment2 = new CommentEntity();
        comment2.setText("Комментарий 2");
        comment2.setAuthor(testUser);
        comment2.setAd(testAd);
        entityManager.persist(comment2);

        entityManager.flush();

        // Act
        List<CommentEntity> result = commentRepository.findAllByAdId(testAd.getId());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CommentEntity::getText)
                .containsExactlyInAnyOrder("Комментарий 1", "Комментарий 2");
    }

    @Test
    @DisplayName("Должен вернуть пустой список если у объявления нет комментариев")
    void shouldReturnEmptyListWhenAdHasNoComments() {
        // Act
        List<CommentEntity> result = commentRepository.findAllByAdId(testAd.getId());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен сохранить новый комментарий")
    void shouldSaveNewComment() {
        // Act
        CommentEntity saved = commentRepository.save(testComment);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getText()).isEqualTo("Отличное предложение!");
        assertThat(saved.getAuthor()).isEqualTo(testUser);
        assertThat(saved.getAd()).isEqualTo(testAd);

        CommentEntity found = entityManager.find(CommentEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getText()).isEqualTo("Отличное предложение!");
    }

    @Test
    @DisplayName("Должен обновить существующий комментарий")
    void shouldUpdateExistingComment() {
        // Arrange
        entityManager.persist(testComment);
        entityManager.flush();

        // Act
        testComment.setText("Обновленный комментарий");
        CommentEntity updated = commentRepository.save(testComment);

        // Assert
        assertThat(updated.getText()).isEqualTo("Обновленный комментарий");

        CommentEntity found = entityManager.find(CommentEntity.class, updated.getId());
        assertThat(found.getText()).isEqualTo("Обновленный комментарий");
    }

    @Test
    @DisplayName("Должен удалить комментарий")
    void shouldDeleteComment() {
        // Arrange
        entityManager.persist(testComment);
        entityManager.flush();
        Integer commentId = testComment.getId();

        // Act
        commentRepository.delete(testComment);

        // Assert
        CommentEntity found = entityManager.find(CommentEntity.class, commentId);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Должен найти комментарий по ID")
    void shouldFindCommentById() {
        // Arrange
        entityManager.persist(testComment);
        entityManager.flush();

        // Act
        Optional<CommentEntity> result = commentRepository.findById(testComment.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo("Отличное предложение!");
    }

    @Test
    @DisplayName("Должен найти все комментарии")
    void shouldFindAllComments() {
        // Arrange
        entityManager.persist(testComment);

        CommentEntity comment2 = new CommentEntity();
        comment2.setText("Еще один комментарий");
        comment2.setAuthor(testUser);
        comment2.setAd(testAd);
        entityManager.persist(comment2);

        entityManager.flush();

        // Act
        List<CommentEntity> result = commentRepository.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Должен найти комментарии только для конкретного объявления")
    void shouldFindCommentsOnlyForSpecificAd() {
        // Arrange
        AdEntity anotherAd = new AdEntity();
        anotherAd.setTitle("Другое объявление");
        anotherAd.setDescription("Второе объявление");
        anotherAd.setPrice(100000);
        anotherAd.setAuthor(testUser);
        entityManager.persist(anotherAd);

        CommentEntity commentForTestAd = new CommentEntity();
        commentForTestAd.setText("Комментарий к первому объявлению");
        commentForTestAd.setAuthor(testUser);
        commentForTestAd.setAd(testAd);
        entityManager.persist(commentForTestAd);

        CommentEntity commentForAnotherAd = new CommentEntity();
        commentForAnotherAd.setText("Комментарий ко второму объявлению");
        commentForAnotherAd.setAuthor(testUser);
        commentForAnotherAd.setAd(anotherAd);
        entityManager.persist(commentForAnotherAd);

        entityManager.flush();

        // Act
        List<CommentEntity> resultForTestAd = commentRepository.findAllByAdId(testAd.getId());
        List<CommentEntity> resultForAnotherAd = commentRepository.findAllByAdId(anotherAd.getId());

        // Assert
        assertThat(resultForTestAd).hasSize(1);
        assertThat(resultForTestAd.get(0).getText()).isEqualTo("Комментарий к первому объявлению");

        assertThat(resultForAnotherAd).hasSize(1);
        assertThat(resultForAnotherAd.get(0).getText()).isEqualTo("Комментарий ко второму объявлению");
    }
}
