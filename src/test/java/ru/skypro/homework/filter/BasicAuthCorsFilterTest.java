package ru.skypro.homework.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Юнит-тесты для BasicAuthCorsFilter.
 * Проверяют добавление CORS заголовков.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты BasicAuthCorsFilter")
class BasicAuthCorsFilterTest {

    private BasicAuthCorsFilter filter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new BasicAuthCorsFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Должен добавить заголовок Access-Control-Allow-Credentials")
    void shouldAddAccessControlAllowCredentialsHeader() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен вызвать следующий фильтр в цепочке")
    void shouldCallNextFilterInChain() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Должен добавить заголовок для GET запроса")
    void shouldAddHeaderForGetRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("GET");
        request.setRequestURI("/api/ads");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен добавить заголовок для POST запроса")
    void shouldAddHeaderForPostRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("POST");
        request.setRequestURI("/api/ads");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен добавить заголовок для PUT запроса")
    void shouldAddHeaderForPutRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("PUT");
        request.setRequestURI("/api/ads/1");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен добавить заголовок для DELETE запроса")
    void shouldAddHeaderForDeleteRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("DELETE");
        request.setRequestURI("/api/ads/1");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен добавить заголовок для PATCH запроса")
    void shouldAddHeaderForPatchRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("PATCH");
        request.setRequestURI("/api/ads/1");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен добавить заголовок для OPTIONS запроса")
    void shouldAddHeaderForOptionsRequest() throws ServletException, IOException {
        // Arrange
        request.setMethod("OPTIONS");
        request.setRequestURI("/api/ads");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен работать для различных URL")
    void shouldWorkForVariousUrls() throws ServletException, IOException {
        // Arrange
        String[] urls = {
                "/api/ads",
                "/api/users/me",
                "/login",
                "/register",
                "/images/users/test.jpg",
                "/images/ads/test.jpg"
        };

        for (String url : urls) {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            request.setRequestURI(url);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            assertThat(response.getHeader("Access-Control-Allow-Credentials"))
                    .as("URL: " + url)
                    .isEqualTo("true");
        }
    }

    @Test
    @DisplayName("Должен сохранять существующие заголовки")
    void shouldPreserveExistingHeaders() throws ServletException, IOException {
        // Arrange
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Custom-Header", "custom-value");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getHeader("Content-Type")).isEqualTo("application/json");
        assertThat(response.getHeader("Custom-Header")).isEqualTo("custom-value");
        assertThat(response.getHeader("Access-Control-Allow-Credentials")).isEqualTo("true");
    }

    @Test
    @DisplayName("Должен работать при множественных вызовах")
    void shouldWorkOnMultipleCalls() throws ServletException, IOException {
        // Act - первый вызов
        filter.doFilterInternal(request, response, filterChain);
        String firstValue = response.getHeader("Access-Control-Allow-Credentials");

        // Act - второй вызов
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filter.doFilterInternal(request, response, filterChain);
        String secondValue = response.getHeader("Access-Control-Allow-Credentials");

        // Assert
        assertThat(firstValue).isEqualTo("true");
        assertThat(secondValue).isEqualTo("true");
    }
}
