package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Интеграционные тесты для ImageServiceImpl.
 * Проверяют бизнес-логику работы с изображениями.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Тесты ImageServiceImpl")
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        // Сервис инжектируется Spring
    }

    @Nested
    @DisplayName("Тесты сохранения изображения пользователя")
    class SaveUserImageTests {

        @Test
        @DisplayName("Должен успешно сохранить изображение пользователя")
        void shouldSaveUserImageSuccessfully() {
            // Arrange
            byte[] imageContent = "test image content".getBytes();
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    imageContent
            );

            // Act
            String savedFileName = imageService.saveUserImage(image);

            // Assert
            assertThat(savedFileName).isNotNull();
            assertThat(savedFileName).contains("test.jpg");
        }

        @Test
        @DisplayName("Должен сохранить изображение с расширением png")
        void shouldSaveImageWithPngExtension() {
            // Arrange
            byte[] imageContent = "test png image".getBytes();
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "test.png",
                    "image/png",
                    imageContent
            );

            // Act
            String savedFileName = imageService.saveUserImage(image);

            // Assert
            assertThat(savedFileName).contains("test.png");
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение null")
        void shouldThrowExceptionWhenImageIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> imageService.saveUserImage(null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Должен сохранить даже пустое изображение")
        void shouldSaveEvenEmptyImage() {
            // Arrange
            MultipartFile emptyImage = new MockMultipartFile(
                    "image",
                    "empty.jpg",
                    "image/jpeg",
                    new byte[0]
            );

            // Act
            String savedFileName = imageService.saveUserImage(emptyImage);

            // Assert
            assertThat(savedFileName).isNotNull();
            assertThat(savedFileName).contains("empty.jpg");
        }
    }

    @Nested
    @DisplayName("Тесты сохранения изображения объявления")
    class SaveAdImageTests {

        @Test
        @DisplayName("Должен успешно сохранить изображение объявления")
        void shouldSaveAdImageSuccessfully() {
            // Arrange
            byte[] imageContent = "ad image content".getBytes();
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "ad.jpg",
                    "image/jpeg",
                    imageContent
            );

            // Act
            String savedFileName = imageService.saveAdImage(image);

            // Assert
            assertThat(savedFileName).isNotNull();
            assertThat(savedFileName).contains("ad.jpg");
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение null")
        void shouldThrowExceptionWhenImageIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> imageService.saveAdImage(null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Должен сохранить даже пустое изображение")
        void shouldSaveEvenEmptyImage() {
            // Arrange
            MultipartFile emptyImage = new MockMultipartFile(
                    "image",
                    "empty-ad.jpg",
                    "image/jpeg",
                    new byte[0]
            );

            // Act
            String savedFileName = imageService.saveAdImage(emptyImage);

            // Assert
            assertThat(savedFileName).isNotNull();
            assertThat(savedFileName).contains("empty-ad.jpg");
        }
    }

    @Nested
    @DisplayName("Тесты загрузки изображения пользователя")
    class LoadUserImageTests {

        @Test
        @DisplayName("Должен успешно загрузить изображение пользователя")
        void shouldLoadUserImageSuccessfully() {
            // Arrange
            byte[] imageContent = "test user image".getBytes();
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "user-test.jpg",
                    "image/jpeg",
                    imageContent
            );
            String savedFileName = imageService.saveUserImage(image);

            // Act
            byte[] result = imageService.loadUserImage(savedFileName);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.length).isGreaterThan(0);
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение не найдено")
        void shouldThrowExceptionWhenImageNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> imageService.loadUserImage("non-existent.jpg"))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Тесты загрузки изображения объявления")
    class LoadAdImageTests {

        @Test
        @DisplayName("Должен успешно загрузить изображение объявления")
        void shouldLoadAdImageSuccessfully() {
            // Arrange
            byte[] imageContent = "test ad image".getBytes();
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "ad-test.jpg",
                    "image/jpeg",
                    imageContent
            );
            String savedFileName = imageService.saveAdImage(image);

            // Act
            byte[] result = imageService.loadAdImage(savedFileName);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.length).isGreaterThan(0);
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение не найдено")
        void shouldThrowExceptionWhenImageNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> imageService.loadAdImage("non-existent.jpg"))
                    .isInstanceOf(Exception.class);
        }
    }
}
