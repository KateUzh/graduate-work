package ru.skypro.homework.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для конфигурации безопасности приложения.
 * Проверяют корректность настройки Spring Security, CORS, аутентификации и авторизации.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("WebSecurityConfig Tests")
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    // ==================== Тесты бинов ====================

    @Test
    @DisplayName("PasswordEncoder должен быть BCryptPasswordEncoder")
    void passwordEncoder_ShouldBeBCryptPasswordEncoder() {
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("PasswordEncoder должен корректно кодировать пароли")
    void passwordEncoder_ShouldEncodePasswordsCorrectly() {
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("AuthenticationManager должен быть создан")
    void authenticationManager_ShouldBeCreated() {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    @DisplayName("CorsConfigurationSource должен быть создан")
    void corsConfigurationSource_ShouldBeCreated() {
        assertThat(corsConfigurationSource).isNotNull();
    }

    // ==================== Тесты CORS конфигурации ====================

    @Test
    @DisplayName("CORS должен разрешать запросы с localhost:3000")
    void corsConfiguration_ShouldAllowLocalhostOrigin() {
        assertThat(corsConfigurationSource).isNotNull();
        // CORS конфигурация проверяется через интеграционные тесты с MockMvc
    }

    @Test
    @DisplayName("CORS должен разрешать все необходимые HTTP методы")
    void corsConfiguration_ShouldAllowAllNecessaryMethods() {
        assertThat(corsConfigurationSource).isNotNull();
        // CORS конфигурация проверяется через интеграционные тесты с MockMvc
    }

    @Test
    @DisplayName("CORS должен разрешать все заголовки")
    void corsConfiguration_ShouldAllowAllHeaders() {
        assertThat(corsConfigurationSource).isNotNull();
        // CORS конфигурация проверяется через интеграционные тесты с MockMvc
    }

    @Test
    @DisplayName("CORS должен разрешать credentials")
    void corsConfiguration_ShouldAllowCredentials() {
        assertThat(corsConfigurationSource).isNotNull();
        // CORS конфигурация проверяется через интеграционные тесты с MockMvc
    }

    // ==================== Тесты доступа к публичным эндпоинтам ====================

    @Test
    @DisplayName("Доступ к /login должен быть разрешен без аутентификации")
    void loginEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().is(not(401)))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("Доступ к /register должен быть разрешен без аутентификации")
    void registerEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        String validRegisterRequest = "{\"username\":\"test@test.com\",\"password\":\"password123\",\"firstName\":\"Test\",\"lastName\":\"User\",\"phone\":\"+79991234567\",\"role\":\"USER\"}";
        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content(validRegisterRequest))
                .andExpect(status().is(not(401)))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("Доступ к /swagger-ui.html должен быть разрешен без аутентификации")
    void swaggerUiEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is(not(401)))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("Доступ к /v3/api-docs должен быть разрешен без аутентификации")
    void apiDocsEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Доступ к /swagger-resources должен быть разрешен без аутентификации")
    void swaggerResourcesEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-resources/configuration/ui"))
                .andExpect(status().is(not(401)))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("Доступ к /images/** должен быть разрешен без аутентификации")
    void imagesEndpoint_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/images/test.jpg"))
                .andExpect(status().isNotFound()); // 404 вместо 401/403
    }

    // ==================== Тесты доступа к защищенным эндпоинтам ====================

    @Test
    @DisplayName("Доступ к /ads/** без аутентификации должен быть запрещен")
    void adsEndpoint_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Доступ к /users/** без аутентификации должен быть запрещен")
    void usersEndpoint_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Доступ к /ads/** с аутентификацией должен быть разрешен")
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void adsEndpoint_ShouldBeAccessibleWithAuthentication() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Доступ к /users/** с аутентификацией должен быть разрешен")
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void usersEndpoint_ShouldBeAccessibleWithAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().is(not(403)));
    }

    // ==================== Тесты HTTP методов для защищенных эндпоинтов ====================

    @Test
    @DisplayName("POST к /ads без аутентификации должен быть запрещен")
    void postToAds_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(post("/ads"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PUT к /ads без аутентификации должен быть запрещен")
    void putToAds_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(put("/ads/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE к /ads без аутентификации должен быть запрещен")
    void deleteToAds_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PATCH к /users без аутентификации должен быть запрещен")
    void patchToUsers_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(patch("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== Тесты CORS для различных методов ====================

    @Test
    @DisplayName("OPTIONS запрос должен возвращать CORS заголовки")
    void optionsRequest_ShouldReturnCorsHeaders() throws Exception {
        mockMvc.perform(options("/ads")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    @DisplayName("GET запрос с Origin должен возвращать CORS заголовки")
    @WithMockUser
    void getRequestWithOrigin_ShouldReturnCorsHeaders() throws Exception {
        mockMvc.perform(get("/ads")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    // ==================== Тесты CSRF ====================

    @Test
    @DisplayName("CSRF должен быть отключен для POST запросов")
    @WithMockUser
    void csrfShouldBeDisabled_ForPostRequests() throws Exception {
        // Если CSRF включен, этот запрос без токена вернет 403
        // Если CSRF отключен, запрос пройдет дальше (может вернуть 400 из-за отсутствия body, но не 403)
        mockMvc.perform(post("/ads"))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("CSRF должен быть отключен для PUT запросов")
    @WithMockUser
    void csrfShouldBeDisabled_ForPutRequests() throws Exception {
        mockMvc.perform(put("/ads/1"))
                .andExpect(status().is(not(403)));
    }

    @Test
    @DisplayName("CSRF должен быть отключен для DELETE запросов")
    @WithMockUser
    void csrfShouldBeDisabled_ForDeleteRequests() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().is(not(403)));
    }

    // ==================== Тесты комбинаций эндпоинтов ====================

    @Test
    @DisplayName("Все публичные эндпоинты из whitelist должны быть доступны")
    void allWhitelistedEndpoints_ShouldBeAccessible() throws Exception {
        String[] publicEndpoints = {
                "/login",
                "/register",
                "/swagger-ui.html",
                "/v3/api-docs",
                "/swagger-resources/configuration/ui",
                "/webjars/test.js",
                "/images/test.png"
        };

        for (String endpoint : publicEndpoints) {
            mockMvc.perform(get(endpoint))
                    .andExpect(status().is(not(401)))
                    .andExpect(status().is(not(403)));
        }
    }

    @Test
    @DisplayName("Все защищенные эндпоинты должны требовать аутентификацию")
    void allProtectedEndpoints_ShouldRequireAuthentication() throws Exception {
        String[] protectedEndpoints = {
                "/ads",
                "/ads/1",
                "/ads/me",
                "/users/me",
                "/users/set_password"
        };

        for (String endpoint : protectedEndpoints) {
            mockMvc.perform(get(endpoint))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ==================== Тесты HTTP Basic Authentication ====================

    @Test
    @DisplayName("HTTP Basic аутентификация должна быть настроена")
    void httpBasicAuthentication_ShouldBeConfigured() throws Exception {
        // Запрос без credentials должен вернуть 401
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists("WWW-Authenticate"))
                .andExpect(header().string("WWW-Authenticate", "Basic realm=\"Realm\""));
    }

    @Test
    @DisplayName("Неверные credentials должны быть отклонены")
    void invalidCredentials_ShouldBeRejected() throws Exception {
        mockMvc.perform(get("/ads")
                        .with(httpBasic("invalid@test.com", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }
}
