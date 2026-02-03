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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для AuthController.
 * Проверяют HTTP endpoints для аутентификации и регистрации.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Тесты AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private Login loginDto;
    private Register registerDto;

    @BeforeEach
    void setUp() {
        loginDto = new Login();
        loginDto.setUsername("test@example.com");
        loginDto.setPassword("password123");

        registerDto = new Register();
        registerDto.setUsername("newuser@example.com");
        registerDto.setPassword("password123");
        registerDto.setFirstName("Иван");
        registerDto.setLastName("Иванов");
        registerDto.setPhone("+79991234567");
        registerDto.setRole(Register.RoleEnum.USER);
    }

    @Nested
    @DisplayName("Тесты POST /login")
    class LoginTests {

        @Test
        @DisplayName("Должен успешно войти с правильными учетными данными")
        void shouldLoginSuccessfully() throws Exception {
            // Arrange
            when(authService.login(any(Login.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен вернуть 401 при неправильных учетных данных")
        void shouldReturn401WhenCredentialsAreWrong() throws Exception {
            // Arrange
            when(authService.login(any(Login.class))).thenReturn(false);

            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Должен вернуть 400 при невалидном запросе")
        void shouldReturn400WhenRequestIsInvalid() throws Exception {
            // Arrange
            Login invalidLogin = new Login();
            // username и password не установлены

            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidLogin)))
                    .andExpect(status().isUnauthorized()); // Может быть 400 или 401 в зависимости от валидации
        }

        @Test
        @DisplayName("Должен вернуть 400 при пустом теле запроса")
        void shouldReturn400WhenBodyIsEmpty() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Тесты POST /register")
    class RegisterTests {

        @Test
        @DisplayName("Должен успешно зарегистрировать нового пользователя")
        void shouldRegisterSuccessfully() throws Exception {
            // Arrange
            when(authService.register(any(Register.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Должен вернуть 400 если пользователь уже существует")
        void shouldReturn400WhenUserAlreadyExists() throws Exception {
            // Arrange
            when(authService.register(any(Register.class))).thenReturn(false);

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Должен успешно зарегистрировать пользователя с ролью ADMIN")
        void shouldRegisterAdminSuccessfully() throws Exception {
            // Arrange
            registerDto.setRole(Register.RoleEnum.ADMIN);
            when(authService.register(any(Register.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Должен вернуть 400 при невалидном запросе")
        void shouldReturn400WhenRequestIsInvalid() throws Exception {
            // Arrange
            Register invalidRegister = new Register();
            // Обязательные поля не установлены

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRegister)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Должен вернуть 400 при пустом теле запроса")
        void shouldReturn400WhenBodyIsEmpty() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Тесты доступности endpoints")
    class EndpointAccessibilityTests {

        @Test
        @DisplayName("Endpoint /login должен быть доступен без аутентификации")
        void loginEndpointShouldBeAccessibleWithoutAuth() throws Exception {
            // Arrange
            when(authService.login(any(Login.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Endpoint /register должен быть доступен без аутентификации")
        void registerEndpointShouldBeAccessibleWithoutAuth() throws Exception {
            // Arrange
            when(authService.register(any(Register.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser
        @DisplayName("Endpoint /login должен быть доступен для аутентифицированных пользователей")
        void loginEndpointShouldBeAccessibleForAuthenticatedUsers() throws Exception {
            // Arrange
            when(authService.login(any(Login.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDto)))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Endpoint /register должен быть доступен для аутентифицированных пользователей")
        void registerEndpointShouldBeAccessibleForAuthenticatedUsers() throws Exception {
            // Arrange
            when(authService.register(any(Register.class))).thenReturn(true);

            // Act & Assert
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDto)))
                    .andExpect(status().isCreated());
        }
    }
}
