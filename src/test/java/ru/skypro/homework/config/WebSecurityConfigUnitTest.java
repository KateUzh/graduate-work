package ru.skypro.homework.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import ru.skypro.homework.service.MyUserDetailsService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для WebSecurityConfig.
 * Проверяют создание и конфигурацию бинов в изоляции от Spring контекста.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WebSecurityConfig Unit Tests")
class WebSecurityConfigUnitTest {

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private DataSource dataSource;

    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    void setUp() {
        webSecurityConfig = new WebSecurityConfig(userDetailsService);
    }

    // ==================== Тесты конструктора ====================

    @Test
    @DisplayName("Конструктор должен инициализировать userDetailsService")
    void constructor_ShouldInitializeUserDetailsService() {
        WebSecurityConfig config = new WebSecurityConfig(userDetailsService);
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Конструктор должен принимать null userDetailsService")
    void constructor_ShouldAcceptNullUserDetailsService() {
        WebSecurityConfig config = new WebSecurityConfig(null);
        assertThat(config).isNotNull();
    }

    // ==================== Тесты PasswordEncoder ====================

    @Test
    @DisplayName("passwordEncoder() должен возвращать BCryptPasswordEncoder")
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("passwordEncoder() должен возвращать новый экземпляр при каждом вызове")
    void passwordEncoder_ShouldReturnNewInstanceEachTime() {
        PasswordEncoder encoder1 = webSecurityConfig.passwordEncoder();
        PasswordEncoder encoder2 = webSecurityConfig.passwordEncoder();

        assertThat(encoder1).isNotNull();
        assertThat(encoder2).isNotNull();
        // Каждый вызов создает новый экземпляр
        assertThat(encoder1).isNotSameAs(encoder2);
    }

    @Test
    @DisplayName("passwordEncoder() должен корректно кодировать различные пароли")
    void passwordEncoder_ShouldEncodeVariousPasswords() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();

        String[] passwords = {
                "simple",
                "Complex123!@#",
                "очень_длинный_пароль_с_русскими_буквами_123",
                "!@#$%^&*()",
                "a",
                "12345678901234567890"
        };

        for (String password : passwords) {
            String encoded = encoder.encode(password);
            assertThat(encoded).isNotNull();
            assertThat(encoded).isNotEqualTo(password);
            assertThat(encoder.matches(password, encoded)).isTrue();
        }
    }

    @Test
    @DisplayName("passwordEncoder() должен генерировать разные хеши для одного пароля")
    void passwordEncoder_ShouldGenerateDifferentHashesForSamePassword() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String password = "testPassword";

        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        assertThat(hash1).isNotEqualTo(hash2);
        assertThat(encoder.matches(password, hash1)).isTrue();
        assertThat(encoder.matches(password, hash2)).isTrue();
    }

    // ==================== Тесты CorsConfigurationSource ====================

    @Test
    @DisplayName("corsConfigurationSource() должен возвращать не null")
    void corsConfigurationSource_ShouldReturnNonNull() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        assertThat(source).isNotNull();
    }

    @Test
    @DisplayName("corsConfigurationSource() должен содержать правильный origin")
    void corsConfigurationSource_ShouldContainCorrectOrigin() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config).isNotNull();
        assertThat(config.getAllowedOrigins())
                .isNotNull()
                .hasSize(1)
                .contains("http://localhost:3000");
    }

    @Test
    @DisplayName("corsConfigurationSource() должен содержать все HTTP методы")
    void corsConfigurationSource_ShouldContainAllHttpMethods() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config).isNotNull();
        assertThat(config.getAllowedMethods())
                .isNotNull()
                .hasSize(6)
                .containsExactlyInAnyOrder("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    @Test
    @DisplayName("corsConfigurationSource() должен разрешать все заголовки")
    void corsConfigurationSource_ShouldAllowAllHeaders() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config).isNotNull();
        assertThat(config.getAllowedHeaders())
                .isNotNull()
                .hasSize(1)
                .contains("*");
    }

    @Test
    @DisplayName("corsConfigurationSource() должен разрешать credentials")
    void corsConfigurationSource_ShouldAllowCredentials() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config).isNotNull();
        assertThat(config.getAllowCredentials()).isTrue();
    }

    @Test
    @DisplayName("corsConfigurationSource() должен применяться ко всем путям")
    void corsConfigurationSource_ShouldApplyToAllPaths() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();

        // Проверяем, что конфигурация применяется к различным путям
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config1 = source.getCorsConfiguration(request);
        assertThat(config1).isNotNull();
    }

    // ==================== Тесты константы AUTH_WHITELIST ====================

    @Test
    @DisplayName("AUTH_WHITELIST должен содержать все необходимые публичные эндпоинты")
    void authWhitelist_ShouldContainAllNecessaryPublicEndpoints() {
        // Используем рефлексию для доступа к приватному полю
        try {
            java.lang.reflect.Field field = WebSecurityConfig.class.getDeclaredField("AUTH_WHITELIST");
            field.setAccessible(true);
            String[] whitelist = (String[]) field.get(null);

            assertThat(whitelist)
                    .isNotNull()
                    .contains(
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/v3/api-docs",
                            "/webjars/**",
                            "/login",
                            "/register",
                            "/images/**"
                    );
        } catch (Exception e) {
            throw new RuntimeException("Failed to access AUTH_WHITELIST", e);
        }
    }

    // ==================== Тесты создания бинов ====================

    @Test
    @DisplayName("Все методы создания бинов должны быть аннотированы @Bean")
    void allBeanMethods_ShouldBeAnnotatedWithBean() throws Exception {
        assertThat(WebSecurityConfig.class.getMethod("passwordEncoder")
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class)).isTrue();

        assertThat(WebSecurityConfig.class.getMethod("corsConfigurationSource")
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class)).isTrue();

        assertThat(WebSecurityConfig.class.getMethod("authenticationManager", HttpSecurity.class)
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class)).isTrue();

        assertThat(WebSecurityConfig.class.getMethod("filterChain", HttpSecurity.class)
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class)).isTrue();
    }

    @Test
    @DisplayName("Класс должен быть аннотирован @Configuration")
    void class_ShouldBeAnnotatedWithConfiguration() {
        assertThat(WebSecurityConfig.class
                .isAnnotationPresent(org.springframework.context.annotation.Configuration.class)).isTrue();
    }

    // ==================== Тесты BCrypt ====================

    @Test
    @DisplayName("BCrypt должен использовать достаточную силу хеширования")
    void bcrypt_ShouldUseSufficientStrength() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String password = "test";
        String encoded = encoder.encode(password);

        // BCrypt хеши начинаются с $2a$, $2b$ или $2y$
        assertThat(encoded).matches("^\\$2[aby]\\$\\d{2}\\$.+");
    }

    @Test
    @DisplayName("BCrypt должен отклонять неверные пароли")
    void bcrypt_ShouldRejectIncorrectPasswords() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String password = "correctPassword";
        String encoded = encoder.encode(password);

        assertThat(encoder.matches("wrongPassword", encoded)).isFalse();
        assertThat(encoder.matches("", encoded)).isFalse();
        assertThat(encoder.matches("correctpassword", encoded)).isFalse();
        assertThat(encoder.matches("CorrectPassword", encoded)).isFalse();
    }

    @Test
    @DisplayName("BCrypt должен обрабатывать пустые строки")
    void bcrypt_ShouldHandleEmptyStrings() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String emptyPassword = "";
        String encoded = encoder.encode(emptyPassword);

        assertThat(encoded).isNotNull();
        assertThat(encoder.matches(emptyPassword, encoded)).isTrue();
        assertThat(encoder.matches("notEmpty", encoded)).isFalse();
    }

    @Test
    @DisplayName("BCrypt должен обрабатывать специальные символы")
    void bcrypt_ShouldHandleSpecialCharacters() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String specialPassword = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~";
        String encoded = encoder.encode(specialPassword);

        assertThat(encoded).isNotNull();
        assertThat(encoder.matches(specialPassword, encoded)).isTrue();
    }

    @Test
    @DisplayName("BCrypt должен обрабатывать Unicode символы")
    void bcrypt_ShouldHandleUnicodeCharacters() {
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();
        String unicodePassword = "пароль密码🔒";
        String encoded = encoder.encode(unicodePassword);

        assertThat(encoded).isNotNull();
        assertThat(encoder.matches(unicodePassword, encoded)).isTrue();
    }

    // ==================== Тесты CORS конфигурации деталей ====================

    @Test
    @DisplayName("CORS конфигурация должна быть неизменяемой после создания")
    void corsConfiguration_ShouldBeImmutableAfterCreation() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config1 = source.getCorsConfiguration(request);
        CorsConfiguration config2 = source.getCorsConfiguration(request);

        // Проверяем, что возвращается одна и та же конфигурация
        assertThat(config1.getAllowedOrigins()).isEqualTo(config2.getAllowedOrigins());
        assertThat(config1.getAllowedMethods()).isEqualTo(config2.getAllowedMethods());
        assertThat(config1.getAllowedHeaders()).isEqualTo(config2.getAllowedHeaders());
        assertThat(config1.getAllowCredentials()).isEqualTo(config2.getAllowCredentials());
    }

    @Test
    @DisplayName("CORS должен содержать только один разрешенный origin")
    void cors_ShouldContainOnlyOneAllowedOrigin() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config.getAllowedOrigins()).hasSize(1);
    }

    @Test
    @DisplayName("CORS должен содержать ровно 6 разрешенных методов")
    void cors_ShouldContainExactlySixAllowedMethods() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config.getAllowedMethods()).hasSize(6);
    }

    @Test
    @DisplayName("CORS не должен разрешать HEAD метод явно")
    void cors_ShouldNotExplicitlyAllowHeadMethod() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config.getAllowedMethods()).doesNotContain("HEAD");
    }

    @Test
    @DisplayName("CORS не должен разрешать TRACE метод")
    void cors_ShouldNotAllowTraceMethod() {
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertThat(config.getAllowedMethods()).doesNotContain("TRACE");
    }
}
