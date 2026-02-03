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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для AdServiceImpl.
 * Проверяют бизнес-логику работы с объявлениями.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты AdServiceImpl")
class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdServiceImpl adService;

    private UserEntity testUser;
    private UserEntity adminUser;
    private AdEntity testAd;
    private Ad testAdDto;
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
        testAd.setPrice(500000);
        testAd.setDescription("Отличный гараж в центре");
        testAd.setAuthor(testUser);

        testAdDto = new Ad();
        testAdDto.setPk(1);
        testAdDto.setTitle("Продам гараж");
        testAdDto.setPrice(500000);

        customUserDetails = new CustomUserDetails(testUser);
        adminUserDetails = new CustomUserDetails(adminUser);
    }

    @Nested
    @DisplayName("Тесты создания объявления")
    class CreateAdTests {

        private CreateOrUpdateAd createAdDto;

        @Mock
        private MultipartFile mockImage;

        @BeforeEach
        void setUp() {
            createAdDto = new CreateOrUpdateAd();
            createAdDto.setTitle("Продам квартиру");
            createAdDto.setPrice(3000000);
            createAdDto.setDescription("Двухкомнатная квартира");
        }

        @Test
        @DisplayName("Должен успешно создать объявление с изображением")
        void shouldCreateAdWithImage() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adMapper.toAdEntity(createAdDto)).thenReturn(testAd);
            when(mockImage.isEmpty()).thenReturn(false);
            when(imageService.saveAdImage(mockImage)).thenReturn("ad-image.jpg");
            when(adRepository.save(any(AdEntity.class))).thenReturn(testAd);
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ad result = adService.createAd(createAdDto, mockImage, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(imageService).saveAdImage(mockImage);
            verify(adRepository).save(any(AdEntity.class));
        }

        @Test
        @DisplayName("Должен успешно создать объявление без изображения")
        void shouldCreateAdWithoutImage() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adMapper.toAdEntity(createAdDto)).thenReturn(testAd);
            when(adRepository.save(any(AdEntity.class))).thenReturn(testAd);
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ad result = adService.createAd(createAdDto, null, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(imageService, never()).saveAdImage(any());
            verify(adRepository).save(any(AdEntity.class));
        }

        @Test
        @DisplayName("Должен выбросить исключение при создании без аутентификации")
        void shouldThrowExceptionWhenCreatingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> adService.createAd(createAdDto, mockImage, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }

        @Test
        @DisplayName("Должен выбросить исключение если пользователь не аутентифицирован")
        void shouldThrowExceptionWhenNotAuthenticated() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> adService.createAd(createAdDto, mockImage, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты получения всех объявлений")
    class GetAllAdsTests {

        @Test
        @DisplayName("Должен вернуть все объявления")
        void shouldReturnAllAds() {
            // Arrange
            AdEntity ad1 = new AdEntity();
            ad1.setId(1);
            AdEntity ad2 = new AdEntity();
            ad2.setId(2);

            Ad adDto1 = new Ad();
            adDto1.setPk(1);
            Ad adDto2 = new Ad();
            adDto2.setPk(2);

            when(adRepository.findAll()).thenReturn(Arrays.asList(ad1, ad2));
            when(adMapper.toDto(ad1)).thenReturn(adDto1);
            when(adMapper.toDto(ad2)).thenReturn(adDto2);

            // Act
            Ads result = adService.getAllAds();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(2);
            assertThat(result.getResults()).hasSize(2);
        }

        @Test
        @DisplayName("Должен вернуть пустой список если объявлений нет")
        void shouldReturnEmptyListWhenNoAds() {
            // Arrange
            when(adRepository.findAll()).thenReturn(Arrays.asList());

            // Act
            Ads result = adService.getAllAds();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(0);
            assertThat(result.getResults()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Тесты получения объявления по ID")
    class GetAdTests {

        @Test
        @DisplayName("Должен вернуть объявление по ID")
        void shouldReturnAdById() {
            // Arrange
            ExtendedAd extendedAdDto = new ExtendedAd();
            extendedAdDto.setPk(1);
            extendedAdDto.setTitle("Продам гараж");

            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(adMapper.toExtendedAd(testAd)).thenReturn(extendedAdDto);

            // Act
            ExtendedAd result = adService.getAd(1);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getPk()).isEqualTo(1);
        }

        @Test
        @DisplayName("Должен выбросить исключение если объявление не найдено")
        void shouldThrowExceptionWhenAdNotFound() {
            // Arrange
            when(adRepository.findById(999)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> adService.getAd(999))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("404 NOT_FOUND");
        }
    }

    @Nested
    @DisplayName("Тесты получения объявлений пользователя")
    class GetMyAdsTests {

        @Test
        @DisplayName("Должен вернуть объявления текущего пользователя")
        void shouldReturnCurrentUserAds() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findAllByAuthorId(1)).thenReturn(Arrays.asList(testAd));
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ads result = adService.getMyAds(authentication);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCount()).isEqualTo(1);
            assertThat(result.getResults()).hasSize(1);
        }

        @Test
        @DisplayName("Должен выбросить исключение при получении без аутентификации")
        void shouldThrowExceptionWhenGettingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> adService.getMyAds(null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты удаления объявления")
    class RemoveAdTests {

        @Test
        @DisplayName("Должен успешно удалить объявление владельцем")
        void shouldRemoveAdByOwner() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));

            // Act
            adService.removeAd(1, authentication);

            // Assert
            verify(adRepository).delete(testAd);
        }

        @Test
        @DisplayName("Должен успешно удалить объявление администратором")
        void shouldRemoveAdByAdmin() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));

            // Act
            adService.removeAd(1, authentication);

            // Assert
            verify(adRepository).delete(testAd);
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

            // Act & Assert
            assertThatThrownBy(() -> adService.removeAd(1, authentication))
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
            assertThatThrownBy(() -> adService.removeAd(999, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("404 NOT_FOUND");
        }

        @Test
        @DisplayName("Должен выбросить исключение при удалении без аутентификации")
        void shouldThrowExceptionWhenRemovingWithoutAuth() {
            // Act & Assert
            assertThatThrownBy(() -> adService.removeAd(1, null))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED");
        }
    }

    @Nested
    @DisplayName("Тесты обновления объявления")
    class UpdateAdsTests {

        private CreateOrUpdateAd updateAdDto;

        @BeforeEach
        void setUp() {
            updateAdDto = new CreateOrUpdateAd();
            updateAdDto.setTitle("Обновленное название");
            updateAdDto.setPrice(600000);
            updateAdDto.setDescription("Обновленное описание");
        }

        @Test
        @DisplayName("Должен успешно обновить объявление владельцем")
        void shouldUpdateAdByOwner() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(adRepository.save(any(AdEntity.class))).thenReturn(testAd);
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ad result = adService.updateAds(1, updateAdDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(adRepository).save(testAd);
        }

        @Test
        @DisplayName("Должен успешно обновить объявление администратором")
        void shouldUpdateAdByAdmin() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(adRepository.save(any(AdEntity.class))).thenReturn(testAd);
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ad result = adService.updateAds(1, updateAdDto, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(adRepository).save(testAd);
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

            // Act & Assert
            assertThatThrownBy(() -> adService.updateAds(1, updateAdDto, authentication))
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
            assertThatThrownBy(() -> adService.updateAds(999, updateAdDto, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("404 NOT_FOUND");
        }
    }

    @Nested
    @DisplayName("Тесты обновления изображения объявления")
    class UpdateAdImageTests {

        @Mock
        private MultipartFile mockImage;

        @Test
        @DisplayName("Должен успешно обновить изображение объявления владельцем")
        void shouldUpdateAdImageByOwner() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(mockImage.isEmpty()).thenReturn(false);
            when(imageService.saveAdImage(mockImage)).thenReturn("new-ad-image.jpg");
            when(adRepository.save(any(AdEntity.class))).thenReturn(testAd);
            when(adMapper.toDto(testAd)).thenReturn(testAdDto);

            // Act
            Ad result = adService.updateAdImage(1, mockImage, authentication);

            // Assert
            assertThat(result).isNotNull();
            verify(imageService).saveAdImage(mockImage);
            verify(adRepository).save(testAd);
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение null")
        void shouldThrowExceptionWhenImageIsNull() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));

            // Act & Assert
            assertThatThrownBy(() -> adService.updateAdImage(1, null, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("400 BAD_REQUEST");
        }

        @Test
        @DisplayName("Должен выбросить исключение если изображение пустое")
        void shouldThrowExceptionWhenImageIsEmpty() {
            // Arrange
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(adRepository.findById(1)).thenReturn(Optional.of(testAd));
            when(mockImage.isEmpty()).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> adService.updateAdImage(1, mockImage, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("400 BAD_REQUEST");
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

            // Act & Assert
            assertThatThrownBy(() -> adService.updateAdImage(1, mockImage, authentication))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("403 FORBIDDEN");
        }
    }
}
