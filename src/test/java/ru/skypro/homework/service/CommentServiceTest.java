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
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для CommentServiceImpl.
 * Проверяют бизнес-логику работы с комментариями.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты CommentServiceImpl")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UserEntity testUser;
    private UserEntity adminUser;
    private AdEntity testAd;
    private CommentEntity testComment;
    private Comment testCommentDto;
    private CustomUserDetails customUserDetails;
    private CustomUserDetails adminUserDetails;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("user@example.com");
        testUser.setRole(UserEntity.Role.USER);

        adminUser = new UserEntity();
        adminUser.setId(2);
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(UserEntity.Role.ADMIN);

        testAd = new AdEntity();
        testAd.setId(1);
        testAd.setTitle("Продам гараж");
        testAd.setAuthor(testUser);

        testComment = new CommentEntity();
        testComment.setId(1);
        testComment.setText("Отличное предложение!");
        testComment.setAuthor(testUser);
        testComment.setAd(testAd);

        testCommentDto = new Comment();
        testCommentDto.setPk(1);
        testCommentDto.setText("Отличное предложение!");

        customUserDetails = new CustomUserDetails(testUser);
        adminUserDetails = new CustomUserDetails(adminUser);
    }

    @Nested
    @DisplayName("Тесты получения комментариев объявления")
    class GetCommentsTests {

        @Test
        @DisplayName("Должен вернуть все комментарии объявления")
        void shouldReturnAllCommentsForAd() {
            // Arrange
            CommentEntity comment1 = new CommentEntity();
            comment1.setId(1);
            CommentEntity comment2 = new CommentEntity();
            comment2.setId(2);

            Comment commentDto1 = new Comment();
            commentDto1.setPk(1);
            Comment commentDto2 = new Comment();
            commentDto2.setPk(2);

            when(commentRepository.findAllByAdId(1)).thenReturn(Arrays.asList(comment1, comment2));
            when(commentMapper.toDto(comment1)).thenReturn(commentDto1);
            when(commentMapper.toDto(comment2)).thenReturn(commentDto2);

            // Act
            Comments result = commentService.getComments(1);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(2);
            assertThat(result.getResults()).hasSize(2);
        }

        @Test
        @DisplayName("Должен вернуть пустой список если комментариев нет")
        void shouldReturnEmptyListWhenNoComments() {
            // Arrange
            when(commentRepository.findAllByAdId(1)).thenReturn(Arrays.asList());

            // Act
            Comments result = commentService.getComments(1);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(0);
            assertThat(result.getResults()).isEmpty();
        }

        @Test
        @DisplayName("Должен вернуть пустой список если объявление не найдено")
        void shouldReturnEmptyListWhenAdNotFound() {
            // Arrange
            when(commentRepository.findAllByAdId(999)).thenReturn(Arrays.asList());

            // Act
            Comments result = commentService.getComments(999);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(0);
            assertThat(result.getResults()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Тесты создания комментария")
    class AddCommentTests {

        private CreateOrUpdateComment createCommentDto;

        @BeforeEach
        void setUp() {
            createCommentDto = new CreateOrUpdateComment();
            createCommentDto.setText("Новый комментарий");
        }

        @Test
        @DisplayName("Должен успешно создать комментарий")
        void shouldCreateCommentSuccessfully() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentMapper.toEntityFromCreateOrUpdateComment(createCommentDto)).thenReturn(testComment);
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(testComment);
            when(commentMapper.toDto(testComment)).thenReturn(testCommentDto);

            // Act
            Comment result = commentService.addComment(1, createCommentDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(commentRepository).save(any(CommentEntity.class));
        }

        @Test
        @DisplayName("Должен выбросить исключение если объявление не найдено")
        void shouldThrowExceptionWhenAdNotFound() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> commentService.addComment(999, createCommentDto, authentication))
                    .isInstanceOf(javax.persistence.EntityNotFoundException.class)
                    .hasMessageContaining("Ad not found");
        }

        @Test
        @DisplayName("Должен выбросить исключение при создании без аутентификации")
        void shouldThrowExceptionWhenCreatingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> commentService.addComment(1, createCommentDto, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> commentService.addComment(1, createCommentDto, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты удаления комментария")
    class DeleteCommentTests {

        @Test
        @DisplayName("Должен успешно удалить комментарий владельцем")
        void shouldDeleteCommentByOwner() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));

            // Act
            commentService.deleteComment(1, 1, authentication);

            // Assert
            verify(commentRepository).deleteById(1);
        }

        @Test
        @DisplayName("Должен успешно удалить комментарий администратором")
        void shouldDeleteCommentByAdmin() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));

            // Act
            commentService.deleteComment(1, 1, authentication);

            // Assert
            verify(commentRepository).deleteById(1);
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не владелец и не админ")
        void shouldThrowExceptionWhenNotOwnerAndNotAdmin() {
            // Arrange
            UserEntity otherUser = new UserEntity();
            otherUser.setId(3);
            otherUser.setRole(UserEntity.Role.USER);
            CustomUserDetails otherUserDetails = new CustomUserDetails(otherUser);

            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(otherUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));

            // Act & Assert
            assertThatThrownBy(() -> commentService.deleteComment(1, 1, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("403 FORBIDDEN");
        }

        @Test
        @DisplayName("Должен выбросить исключение если объявление не найдено")
        void shouldThrowExceptionWhenAdNotFound() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> commentService.deleteComment(999, 1, authentication))
                    .isInstanceOf(javax.persistence.EntityNotFoundException.class)
                    .hasMessageContaining("Ad not found");
        }

        @Test
        @DisplayName("Должен выбросить исключение если комментарий не найден")
        void shouldThrowExceptionWhenCommentNotFound() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> commentService.deleteComment(1, 999, authentication))
                    .isInstanceOf(javax.persistence.EntityNotFoundException.class)
                    .hasMessageContaining("Comment not found");
        }

        @Test
        @DisplayName("Должен выбросить исключение при удалении без аутентификации")
        void shouldThrowExceptionWhenDeletingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> commentService.deleteComment(1, 1, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты обновления комментария")
    class UpdateCommentTests {

        private CreateOrUpdateComment updateCommentDto;

        @BeforeEach
        void setUp() {
            updateCommentDto = new CreateOrUpdateComment();
            updateCommentDto.setText("Обновленный комментарий");
        }

        @Test
        @DisplayName("Должен успешно обновить комментарий владельцем")
        void shouldUpdateCommentByOwner() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(testComment);
            when(commentMapper.toDto(testComment)).thenReturn(testCommentDto);

            // Act
            Comment result = commentService.updateComment(1, 1, updateCommentDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(commentRepository).save(testComment);
        }

        @Test
        @DisplayName("Должен успешно обновить комментарий администратором")
        void shouldUpdateCommentByAdmin() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(testComment);
            when(commentMapper.toDto(testComment)).thenReturn(testCommentDto);

            // Act
            Comment result = commentService.updateComment(1, 1, updateCommentDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(commentRepository).save(testComment);
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не владелец и не админ")
        void shouldThrowExceptionWhenNotOwnerAndNotAdmin() {
            // Arrange
            UserEntity otherUser = new UserEntity();
            otherUser.setId(3);
            otherUser.setRole(UserEntity.Role.USER);
            CustomUserDetails otherUserDetails = new CustomUserDetails(otherUser);

            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(otherUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));

            // Act & Assert
            assertThatThrownBy(() -> commentService.updateComment(1, 1, updateCommentDto, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("403 FORBIDDEN");
        }

        @Test
        @DisplayName("Должен выбросить исключение если объявление не найдено")
        void shouldThrowExceptionWhenAdNotFound() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> commentService.updateComment(999, 1, updateCommentDto, authentication))
                    .isInstanceOf(javax.persistence.EntityNotFoundException.class)
                    .hasMessageContaining("Ad not found");
        }

        @Test
        @DisplayName("Должен выбросить исключение если комментарий не найден")
        void shouldThrowExceptionWhenCommentNotFound() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(commentRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> commentService.updateComment(1, 999, updateCommentDto, authentication))
                    .isInstanceOf(javax.persistence.EntityNotFoundException.class)
                    .hasMessageContaining("Comment not found");
        }

        @Test
        @DisplayName("Должен выбросить исключение при обновлении без аутентификации")
        void shouldThrowExceptionWhenUpdatingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> commentService.updateComment(1, 1, updateCommentDto, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }
}
