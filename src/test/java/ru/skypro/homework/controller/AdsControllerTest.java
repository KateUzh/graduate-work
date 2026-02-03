package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для AdsController.
 * Проверяют HTTP endpoints для работы с объявлениями.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Тесты AdsController")
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdServiceImpl adService;

    @MockBean
    private CommentServiceImpl commentService;

    private CreateOrUpdateAd createAdDto;
    private Ad adDto;
    private MockMultipartFile imageFile;
    private MockMultipartFile propertiesFile;

    @BeforeEach
    void setUp() {
        createAdDto = new CreateOrUpdateAd();
        createAdDto.setTitle("Продам гараж");
        createAdDto.setPrice(500000);
        createAdDto.setDescription("Отличный гараж в центре");

        adDto = new Ad();
        adDto.setPk(1);
        adDto.setTitle("Продам гараж");
        adDto.setPrice(500000);

        imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        String propertiesJson = "{\"title\":\"Продам гараж\",\"price\":500000,\"description\":\"Отличный гараж в центре\"}";
        propertiesFile = new MockMultipartFile(
                "properties",
                "",
                "application/json",
                propertiesJson.getBytes()
        );
    }

    @Nested
    @DisplayName("Тесты POST /v1/adss")
    class AddAdTests {

        @Test
        @WithMockUser
        @DisplayName("Должен успешно создать объявление с изображением")
        void shouldCreateAdWithImage() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Продам гараж")
                            .param("price", "500000")
                            .param("description", "Отличный гараж в центре")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pk").value(1))
                    .andExpect(jsonPath("$.title").value("Продам гараж"))
                    .andExpect(jsonPath("$.price").value(500000));
        }

        @Test
        @DisplayName("Должен вернуть 401 при создании без аутентификации")
        void shouldReturn401WhenCreatingWithoutAuth() throws Exception {
            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Продам гараж")
                            .param("price", "500000")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        @DisplayName("Должен создать объявление без изображения")
        void shouldCreateAdWithoutImage() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), eq(null), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .param("title", "Продам гараж")
                            .param("price", "500000")
                            .param("description", "Отличный гараж в центре")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Должен создать объявление с минимальными данными")
        void shouldCreateAdWithMinimalData() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Товар")
                            .param("price", "100")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Тесты PATCH /v1/ads/{id}/image")
    class UpdateImageTests {

        @Test
        @WithMockUser
        @DisplayName("Должен успешно обновить изображение объявления")
        void shouldUpdateAdImage() throws Exception {
            // Arrange
            when(adService.updateAdImage(eq(1), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/ads/1/image")
                            .file(imageFile)
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            })
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pk").value(1));
        }

        @Test
        @DisplayName("Должен вернуть 401 при обновлении без аутентификации")
        void shouldReturn401WhenUpdatingWithoutAuth() throws Exception {
            // Act & Assert
            mockMvc.perform(multipart("/v1/ads/1/image")
                            .file(imageFile)
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            })
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        @DisplayName("Должен обновить изображение для разных ID")
        void shouldUpdateImageForDifferentIds() throws Exception {
            // Arrange
            Integer[] ids = {1, 5, 10, 100};

            for (Integer id : ids) {
                Ad ad = new Ad();
                ad.setPk(id);
                when(adService.updateAdImage(eq(id), any(), any())).thenReturn(ad);

                // Act & Assert
                mockMvc.perform(multipart("/v1/ads/" + id + "/image")
                                .file(imageFile)
                                .with(request -> {
                                    request.setMethod("PATCH");
                                    return request;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.pk").value(id));
            }
        }

        @Test
        @WithMockUser
        @DisplayName("Должен поддерживать различные форматы изображений")
        void shouldSupportVariousImageFormats() throws Exception {
            // Arrange
            when(adService.updateAdImage(eq(1), any(), any())).thenReturn(adDto);

            String[] contentTypes = {
                    "image/jpeg",
                    "image/png",
                    "image/gif"
            };

            for (String contentType : contentTypes) {
                MockMultipartFile image = new MockMultipartFile(
                        "image",
                        "test." + contentType.split("/")[1],
                        contentType,
                        "test content".getBytes()
                );

                // Act & Assert
                mockMvc.perform(multipart("/v1/ads/1/image")
                                .file(image)
                                .with(request -> {
                                    request.setMethod("PATCH");
                                    return request;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("Тесты CORS")
    class CorsTests {

        @Test
        @WithMockUser
        @DisplayName("Должен поддерживать CORS для создания объявления")
        void shouldSupportCorsForAddAd() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Продам гараж")
                            .param("price", "500000")
                            .header("Origin", "http://localhost:3000")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Должен поддерживать CORS для обновления изображения")
        void shouldSupportCorsForUpdateImage() throws Exception {
            // Arrange
            when(adService.updateAdImage(eq(1), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/ads/1/image")
                            .file(imageFile)
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            })
                            .header("Origin", "http://localhost:3000")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Тесты валидации")
    class ValidationTests {

        @Test
        @WithMockUser
        @DisplayName("Должен принять объявление с большой ценой")
        void shouldAcceptAdWithLargePrice() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(adDto);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Дорогой товар")
                            .param("price", "999999999")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Должен принять объявление с длинным описанием")
        void shouldAcceptAdWithLongDescription() throws Exception {
            // Arrange
            when(adService.createAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(adDto);
            String longDescription = "Очень длинное описание ".repeat(50);

            // Act & Assert
            mockMvc.perform(multipart("/v1/adss")
                            .file(imageFile)
                            .param("title", "Товар")
                            .param("price", "1000")
                            .param("description", longDescription)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }
    }
}
