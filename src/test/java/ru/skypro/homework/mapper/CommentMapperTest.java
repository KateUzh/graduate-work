package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для CommentMapper.
 * Проверяют корректность маппинга между Entity и DTO комментариев.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CommentMapper Tests")
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    private CommentEntity commentEntity;
    private UserEntity author;
    private AdEntity ad;

    @BeforeEach
    void setUp() {
        author = new UserEntity();
        author.setId(1);
        author.setEmail("author@test.com");
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setImage("avatar.jpg");

        ad = new AdEntity();
        ad.setId(1);
        ad.setTitle("Test Ad");

        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setText("Test Comment");
        commentEntity.setCreatedAt(1234567890L);
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
    }

    // ==================== Тесты toDto ====================

    @Test
    @DisplayName("toDto должен корректно преобразовать Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(commentEntity.getId());
        assertThat(result.getText()).isEqualTo(commentEntity.getText());
        assertThat(result.getCreatedAt()).isEqualTo(commentEntity.getCreatedAt());
        assertThat(result.getAuthor()).isEqualTo(author.getId());
        assertThat(result.getAuthorFirstName()).isEqualTo(author.getFirstName());
    }

    @Test
    @DisplayName("toDto должен добавить префикс к изображению автора")
    void toDto_ShouldAddAuthorImagePrefix() {
        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getAuthorImage()).isEqualTo("/images/users/avatar.jpg");
    }

    @Test
    @DisplayName("toDto должен вернуть null для authorImage если оно null")
    void toDto_ShouldReturnNullAuthorImageWhenEntityImageIsNull() {
        author.setImage(null);

        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getAuthorImage()).isNull();
    }

    @Test
    @DisplayName("toDto должен обработать null entity")
    void toDto_ShouldHandleNullEntity() {
        Comment result = commentMapper.toDto(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toEntityFromComment ====================

    @Test
    @DisplayName("toEntityFromComment должен преобразовать Comment DTO в Entity")
    void toEntityFromComment_ShouldMapCommentDtoToEntity() {
        Comment dto = new Comment();
        dto.setPk(1);
        dto.setText("Comment Text");
        dto.setCreatedAt(9876543210L);
        dto.setAuthor(1);
        dto.setAuthorFirstName("Jane");

        CommentEntity result = commentMapper.toEntityFromComment(dto);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("Comment Text");
        assertThat(result.getCreatedAt()).isEqualTo(9876543210L);
    }

    @Test
    @DisplayName("toEntityFromComment должен обработать null DTO")
    void toEntityFromComment_ShouldHandleNullDto() {
        CommentEntity result = commentMapper.toEntityFromComment(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toEntityFromCreateOrUpdateComment ====================

    @Test
    @DisplayName("toEntityFromCreateOrUpdateComment должен преобразовать DTO в Entity")
    void toEntityFromCreateOrUpdateComment_ShouldMapDtoToEntity() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("New Comment");

        CommentEntity result = commentMapper.toEntityFromCreateOrUpdateComment(dto);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("New Comment");
    }

    @Test
    @DisplayName("toEntityFromCreateOrUpdateComment должен обработать null DTO")
    void toEntityFromCreateOrUpdateComment_ShouldHandleNullDto() {
        CommentEntity result = commentMapper.toEntityFromCreateOrUpdateComment(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты граничных случаев ====================

    @Test
    @DisplayName("toDto должен обработать Entity с минимальными данными")
    void toDto_ShouldHandleEntityWithMinimalData() {
        UserEntity minimalAuthor = new UserEntity();
        minimalAuthor.setId(1);
        minimalAuthor.setFirstName("Min");

        CommentEntity minimal = new CommentEntity();
        minimal.setId(1);
        minimal.setText("Text");
        minimal.setCreatedAt(1000L);
        minimal.setAuthor(minimalAuthor);

        Comment result = commentMapper.toDto(minimal);

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(1);
        assertThat(result.getText()).isEqualTo("Text");
        assertThat(result.getAuthor()).isEqualTo(1);
        assertThat(result.getAuthorFirstName()).isEqualTo("Min");
        assertThat(result.getAuthorImage()).isNull();
    }

    @Test
    @DisplayName("toDto должен корректно маппить pk из id")
    void toDto_ShouldMapPkFromId() {
        commentEntity.setId(777);

        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getPk()).isEqualTo(777);
    }

    @Test
    @DisplayName("toDto должен корректно маппить author id")
    void toDto_ShouldMapAuthorId() {
        author.setId(555);

        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getAuthor()).isEqualTo(555);
    }

    @Test
    @DisplayName("toDto должен корректно маппить authorFirstName")
    void toDto_ShouldMapAuthorFirstName() {
        author.setFirstName("TestName");

        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getAuthorFirstName()).isEqualTo("TestName");
    }

    @Test
    @DisplayName("toEntityFromCreateOrUpdateComment должен обработать пустой текст")
    void toEntityFromCreateOrUpdateComment_ShouldHandleEmptyText() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("");

        CommentEntity result = commentMapper.toEntityFromCreateOrUpdateComment(dto);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEmpty();
    }

    @Test
    @DisplayName("toDto должен обработать длинный текст комментария")
    void toDto_ShouldHandleLongCommentText() {
        String longText = "A".repeat(1000);
        commentEntity.setText(longText);

        Comment result = commentMapper.toDto(commentEntity);

        assertThat(result.getText()).isEqualTo(longText);
        assertThat(result.getText()).hasSize(1000);
    }
}
