package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.service.ImageService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для ImageController.
 * Проверяют HTTP endpoints для получения изображений.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Тесты ImageController")
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    private byte[] testImageBytes;

    @BeforeEach
    void setUp() {
        testImageBytes = "test image content".getBytes();
    }

    @Nested
    @DisplayName("Тесты GET /images/users/{filename}")
    class GetUserImageTests {

        @Test
        @DisplayName("Должен успешно вернуть изображение пользователя")
        void shouldReturnUserImage() throws Exception {
            // Arrange
            String filename = "user-image.jpg";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(testImageBytes));
        }

        @Test
        @DisplayName("Должен поддерживать JPEG формат")
        void shouldSupportJpegFormat() throws Exception {
            // Arrange
            String filename = "image.jpg";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Content-Type"));
        }

        @Test
        @DisplayName("Должен поддерживать PNG формат")
        void shouldSupportPngFormat() throws Exception {
            // Arrange
            String filename = "image.png";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен поддерживать GIF формат")
        void shouldSupportGifFormat() throws Exception {
            // Arrange
            String filename = "image.gif";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Тесты GET /images/ads/{filename}")
    class GetAdImageTests {

        @Test
        @DisplayName("Должен успешно вернуть изображение объявления")
        void shouldReturnAdImage() throws Exception {
            // Arrange
            String filename = "ad-image.jpg";
            when(imageService.loadAdImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/ads/{filename}", filename))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(testImageBytes));
        }

        @Test
        @DisplayName("Должен поддерживать различные форматы изображений")
        void shouldSupportVariousImageFormats() throws Exception {
            // Arrange
            String[] filenames = {"image.jpg", "image.png", "image.gif"};

            for (String filename : filenames) {
                when(imageService.loadAdImage(filename)).thenReturn(testImageBytes);

                // Act & Assert
                mockMvc.perform(get("/images/ads/{filename}", filename))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Должен корректно обрабатывать имена файлов с UUID")
        void shouldHandleUuidFilenames() throws Exception {
            // Arrange
            String filename = "550e8400-e29b-41d4-a716-446655440000_image.jpg";
            when(imageService.loadAdImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/ads/{filename}", filename))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(testImageBytes));
        }
    }

    @Nested
    @DisplayName("Тесты доступности endpoints")
    class EndpointAccessibilityTests {

        @Test
        @DisplayName("Endpoint /images/users/{filename} должен быть доступен без аутентификации")
        void userImageEndpointShouldBeAccessibleWithoutAuth() throws Exception {
            // Arrange
            String filename = "public-user-image.jpg";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Endpoint /images/ads/{filename} должен быть доступен без аутентификации")
        void adImageEndpointShouldBeAccessibleWithoutAuth() throws Exception {
            // Arrange
            String filename = "public-ad-image.jpg";
            when(imageService.loadAdImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/ads/{filename}", filename))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Тесты CORS")
    class CorsTests {

        @Test
        @DisplayName("Должен поддерживать CORS для изображений пользователей")
        void shouldSupportCorsForUserImages() throws Exception {
            // Arrange
            String filename = "user-image.jpg";
            when(imageService.loadUserImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/users/{filename}", filename)
                            .header("Origin", "http://localhost:3000"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Credentials"));
        }

        @Test
        @DisplayName("Должен поддерживать CORS для изображений объявлений")
        void shouldSupportCorsForAdImages() throws Exception {
            // Arrange
            String filename = "ad-image.jpg";
            when(imageService.loadAdImage(filename)).thenReturn(testImageBytes);

            // Act & Assert
            mockMvc.perform(get("/images/ads/{filename}", filename)
                            .header("Origin", "http://localhost:3000"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Credentials"));
        }
    }
}
