package ru.skypro.homework.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;

/**
 * Интеграционные тесты для WebSecurityConfig.
 * Проверяют работу безопасности в реальных сценариях использования.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("WebSecurityConfig Integration Tests")
class WebSecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== Тесты публичных эндпоинтов ====================

    @Nested
    @DisplayName("Публичные эндпоинты")
    class PublicEndpointsTests {

        @Test
        @DisplayName("GET /login должен быть доступен")
        void getLogin_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST /login должен быть доступен")
        void postLogin_ShouldBeAccessible() throws Exception {
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST /register должен быть доступен")
        void postRegister_ShouldBeAccessible() throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /swagger-ui.html должен быть доступен")
        void getSwaggerUi_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/swagger-ui.html"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /v3/api-docs должен быть доступен")
        void getApiDocs_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /images/any-image.jpg должен быть доступен")
        void getImages_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/images/test-image.jpg"))
                    .andExpect(status().isNotFound()); // 404, а не 401/403
        }

        @Test
        @DisplayName("GET /webjars/test.js должен быть доступен")
        void getWebjars_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/webjars/test.js"))
                    .andExpect(status().isNotFound()); // 404, а не 401/403
        }

        @Test
        @DisplayName("GET /swagger-resources/configuration/ui должен быть доступен")
        void getSwaggerResources_ShouldBeAccessible() throws Exception {
            mockMvc.perform(get("/swagger-resources/configuration/ui"))
                    .andExpect(status().isOk());
        }
    }

    // ==================== Тесты защищенных эндпоинтов ====================

    @Nested
    @DisplayName("Защищенные эндпоинты без аутентификации")
    class ProtectedEndpointsWithoutAuthTests {

        @Test
        @DisplayName("GET /ads должен требовать аутентификацию")
        void getAds_ShouldRequireAuth() throws Exception {
            mockMvc.perform(get("/ads"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().exists("WWW-Authenticate"));
        }

        @Test
        @DisplayName("GET /ads/1 должен требовать аутентификацию")
        void getAdById_ShouldRequireAuth() throws Exception {
            mockMvc.perform(get("/ads/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST /ads должен требовать аутентификацию")
        void postAds_ShouldRequireAuth() throws Exception {
            mockMvc.perform(post("/ads")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("PUT /ads/1 должен требовать аутентификацию")
        void putAd_ShouldRequireAuth() throws Exception {
            mockMvc.perform(put("/ads/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE /ads/1 должен требовать аутентификацию")
        void deleteAd_ShouldRequireAuth() throws Exception {
            mockMvc.perform(delete("/ads/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET /users/me должен требовать аутентификацию")
        void getUsersMe_ShouldRequireAuth() throws Exception {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("PATCH /users/me должен требовать аутентификацию")
        void patchUsersMe_ShouldRequireAuth() throws Exception {
            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST /users/set_password должен требовать аутентификацию")
        void postSetPassword_ShouldRequireAuth() throws Exception {
            mockMvc.perform(post("/users/set_password")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ==================== Тесты защищенных эндпоинтов с аутентификацией ====================

    @Nested
    @DisplayName("Защищенные эндпоинты с аутентификацией")
    class ProtectedEndpointsWithAuthTests {

        @Test
        @DisplayName("GET /ads должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void getAds_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(get("/ads"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /ads/1 должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void getAdById_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(get("/ads/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST /ads должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void postAds_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(post("/ads")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("PUT /ads/1 должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void putAd_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(put("/ads/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("DELETE /ads/1 должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void deleteAd_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(delete("/ads/1"))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("GET /users/me должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void getUsersMe_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("PATCH /users/me должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void patchUsersMe_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("POST /users/set_password должен быть доступен с аутентификацией")
        @WithMockUser(username = "test@test.com")
        void postSetPassword_ShouldBeAccessibleWithAuth() throws Exception {
            mockMvc.perform(post("/users/set_password")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }
    }

    // ==================== Тесты CORS ====================

    @Nested
    @DisplayName("CORS конфигурация")
    class CorsTests {

        @Test
        @DisplayName("OPTIONS запрос к /ads должен возвращать CORS заголовки")
        void optionsToAds_ShouldReturnCorsHeaders() throws Exception {
            mockMvc.perform(options("/ads")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Origin"))
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                    .andExpect(header().exists("Access-Control-Allow-Methods"));
        }

        @Test
        @DisplayName("OPTIONS запрос к /users/me должен возвращать CORS заголовки")
        void optionsToUsers_ShouldReturnCorsHeaders() throws Exception {
            mockMvc.perform(options("/users/me")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Origin"))
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
        }

        @Test
        @DisplayName("GET запрос с Origin должен возвращать CORS заголовки")
        @WithMockUser
        void getWithOrigin_ShouldReturnCorsHeaders() throws Exception {
            mockMvc.perform(get("/ads")
                            .header("Origin", "http://localhost:3000"))
                    .andExpect(header().exists("Access-Control-Allow-Origin"))
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
        }

        @Test
        @DisplayName("POST запрос с Origin должен возвращать CORS заголовки")
        @WithMockUser
        void postWithOrigin_ShouldReturnCorsHeaders() throws Exception {
            mockMvc.perform(post("/ads")
                            .header("Origin", "http://localhost:3000")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(header().exists("Access-Control-Allow-Origin"))
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
        }

        @Test
        @DisplayName("DELETE запрос с Origin должен возвращать CORS заголовки")
        @WithMockUser
        void deleteWithOrigin_ShouldReturnCorsHeaders() throws Exception {
            mockMvc.perform(delete("/ads/1")
                            .header("Origin", "http://localhost:3000"))
                    .andExpect(header().exists("Access-Control-Allow-Origin"))
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
        }

        @Test
        @DisplayName("OPTIONS запрос должен разрешать все необходимые методы")
        void optionsRequest_ShouldAllowAllNecessaryMethods() throws Exception {
            mockMvc.perform(options("/ads")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "POST"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Methods",
                            containsString("GET")))
                    .andExpect(header().string("Access-Control-Allow-Methods",
                            containsString("POST")))
                    .andExpect(header().string("Access-Control-Allow-Methods",
                            containsString("PUT")))
                    .andExpect(header().string("Access-Control-Allow-Methods",
                            containsString("PATCH")))
                    .andExpect(header().string("Access-Control-Allow-Methods",
                            containsString("DELETE")));
        }

        @Test
        @DisplayName("CORS должен разрешать credentials")
        void cors_ShouldAllowCredentials() throws Exception {
            mockMvc.perform(options("/ads")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
        }
    }

    // ==================== Тесты CSRF ====================

    @Nested
    @DisplayName("CSRF защита")
    class CsrfTests {

        @Test
        @DisplayName("POST без CSRF токена должен быть разрешен (CSRF отключен)")
        @WithMockUser
        void postWithoutCsrfToken_ShouldBeAllowed() throws Exception {
            mockMvc.perform(post("/ads")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("PUT без CSRF токена должен быть разрешен (CSRF отключен)")
        @WithMockUser
        void putWithoutCsrfToken_ShouldBeAllowed() throws Exception {
            mockMvc.perform(put("/ads/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("DELETE без CSRF токена должен быть разрешен (CSRF отключен)")
        @WithMockUser
        void deleteWithoutCsrfToken_ShouldBeAllowed() throws Exception {
            mockMvc.perform(delete("/ads/1"))
                    .andExpect(status().is(not(403)));
        }

        @Test
        @DisplayName("PATCH без CSRF токена должен быть разрешен (CSRF отключен)")
        @WithMockUser
        void patchWithoutCsrfToken_ShouldBeAllowed() throws Exception {
            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(403)));
        }
    }

    // ==================== Тесты различных Content-Type ====================

    @Nested
    @DisplayName("Различные Content-Type")
    class ContentTypeTests {

        @Test
        @DisplayName("POST с application/json должен быть принят")
        @WithMockUser
        void postWithJsonContentType_ShouldBeAccepted() throws Exception {
            mockMvc.perform(post("/ads")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().is(not(415))); // Not Unsupported Media Type
        }

        @Test
        @DisplayName("POST с multipart/form-data должен быть принят")
        @WithMockUser
        void postWithMultipartContentType_ShouldBeAccepted() throws Exception {
            mockMvc.perform(post("/ads")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().is(not(415)));
        }

        @Test
        @DisplayName("PATCH с application/json должен быть принят")
        @WithMockUser
        void patchWithJsonContentType_ShouldBeAccepted() throws Exception {
            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().is(not(415)));
        }
    }

    // ==================== Тесты комбинированных сценариев ====================

    @Nested
    @DisplayName("Комбинированные сценарии")
    class CombinedScenariosTests {

        @Test
        @DisplayName("Публичный эндпоинт с аутентификацией должен работать")
        @WithMockUser
        void publicEndpointWithAuth_ShouldWork() throws Exception {
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Защищенный эндпоинт с CORS и аутентификацией должен работать")
        @WithMockUser
        void protectedEndpointWithCorsAndAuth_ShouldWork() throws Exception {
            mockMvc.perform(get("/ads")
                            .header("Origin", "http://localhost:3000"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Origin"));
        }

        @Test
        @DisplayName("Множественные запросы с одной сессией должны работать")
        @WithMockUser
        void multipleRequestsWithSameSession_ShouldWork() throws Exception {
            mockMvc.perform(get("/ads"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/ads")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(not(401)));
        }

        @Test
        @DisplayName("Переключение между публичными и защищенными эндпоинтами должно работать")
        void switchingBetweenPublicAndProtected_ShouldWork() throws Exception {
            // Публичный эндпоинт
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());

            // Защищенный эндпоинт без аутентификации
            mockMvc.perform(get("/ads"))
                    .andExpect(status().isUnauthorized());

            // Снова публичный эндпоинт
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    // ==================== Тесты граничных случаев ====================

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCasesTests {

        @Test
        @DisplayName("Запрос к несуществующему эндпоинту должен вернуть 404")
        @WithMockUser
        void requestToNonExistentEndpoint_ShouldReturn404() throws Exception {
            mockMvc.perform(get("/nonexistent"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Запрос к несуществующему защищенному эндпоинту без аутентификации должен вернуть 401")
        void requestToNonExistentProtectedEndpoint_ShouldReturn401() throws Exception {
            mockMvc.perform(get("/ads/nonexistent/path"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Пустой путь к защищенному ресурсу должен требовать аутентификацию")
        void emptyPathToProtectedResource_ShouldRequireAuth() throws Exception {
            mockMvc.perform(get("/ads/"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Путь с trailing slash к публичному эндпоинту должен работать")
        void pathWithTrailingSlashToPublicEndpoint_ShouldWork() throws Exception {
            mockMvc.perform(get("/login/"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Запрос с очень длинным путем должен обрабатываться корректно")
        @WithMockUser
        void requestWithVeryLongPath_ShouldBeHandledCorrectly() throws Exception {
            String longPath = "/ads/" + "a".repeat(1000);
            mockMvc.perform(get(longPath))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }
    }
}
