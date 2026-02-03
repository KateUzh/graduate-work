package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для AdMapper.
 * Проверяют корректность маппинга между Entity и DTO объявлений.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("AdMapper Tests")
class AdMapperTest {

    @Autowired
    private AdMapper adMapper;

    private AdEntity adEntity;
    private UserEntity author;

    @BeforeEach
    void setUp() {
        author = new UserEntity();
        author.setId(1);
        author.setEmail("author@test.com");
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setPhone("+79991234567");

        adEntity = new AdEntity();
        adEntity.setId(1);
        adEntity.setTitle("Test Ad");
        adEntity.setPrice(1000);
        adEntity.setDescription("Test Description");
        adEntity.setImage("ad-image.jpg");
        adEntity.setAuthor(author);
    }

    // ==================== Тесты toDto ====================

    @Test
    @DisplayName("toDto должен корректно преобразовать Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        Ad result = adMapper.toDto(adEntity);

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(adEntity.getId());
        assertThat(result.getTitle()).isEqualTo(adEntity.getTitle());
        assertThat(result.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(result.getAuthor()).isEqualTo(author.getId());
    }

    @Test
    @DisplayName("toDto должен добавить префикс к пути изображения")
    void toDto_ShouldAddImagePrefix() {
        Ad result = adMapper.toDto(adEntity);

        assertThat(result.getImage()).isEqualTo("/images/ads/ad-image.jpg");
    }

    @Test
    @DisplayName("toDto должен вернуть null для image если оно null")
    void toDto_ShouldReturnNullImageWhenEntityImageIsNull() {
        adEntity.setImage(null);

        Ad result = adMapper.toDto(adEntity);

        assertThat(result.getImage()).isNull();
    }

    @Test
    @DisplayName("toDto должен обработать null entity")
    void toDto_ShouldHandleNullEntity() {
        Ad result = adMapper.toDto(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toExtendedAd ====================

    @Test
    @DisplayName("toExtendedAd должен преобразовать Entity в ExtendedAd")
    void toExtendedAd_ShouldMapEntityToExtendedAd() {
        ExtendedAd result = adMapper.toExtendedAd(adEntity);

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(adEntity.getId());
        assertThat(result.getTitle()).isEqualTo(adEntity.getTitle());
        assertThat(result.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(result.getDescription()).isEqualTo(adEntity.getDescription());
        assertThat(result.getAuthorFirstName()).isEqualTo(author.getFirstName());
        assertThat(result.getAuthorLastName()).isEqualTo(author.getLastName());
        assertThat(result.getEmail()).isEqualTo(author.getEmail());
        assertThat(result.getPhone()).isEqualTo(author.getPhone());
    }

    @Test
    @DisplayName("toExtendedAd должен добавить префикс к изображению")
    void toExtendedAd_ShouldAddImagePrefix() {
        ExtendedAd result = adMapper.toExtendedAd(adEntity);

        assertThat(result.getImage()).isEqualTo("/images/ads/ad-image.jpg");
    }

    @Test
    @DisplayName("toExtendedAd должен вернуть null для image если оно null")
    void toExtendedAd_ShouldReturnNullImageWhenEntityImageIsNull() {
        adEntity.setImage(null);

        ExtendedAd result = adMapper.toExtendedAd(adEntity);

        assertThat(result.getImage()).isNull();
    }

    @Test
    @DisplayName("toExtendedAd должен обработать null entity")
    void toExtendedAd_ShouldHandleNullEntity() {
        ExtendedAd result = adMapper.toExtendedAd(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toAdEntity ====================

    @Test
    @DisplayName("toAdEntity должен преобразовать CreateOrUpdateAd в Entity")
    void toAdEntity_ShouldMapCreateOrUpdateAdToEntity() {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("New Ad");
        dto.setPrice(2000);
        dto.setDescription("New Description");

        AdEntity result = adMapper.toAdEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Ad");
        assertThat(result.getPrice()).isEqualTo(2000);
        assertThat(result.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("toAdEntity должен обработать null DTO")
    void toAdEntity_ShouldHandleNullDto() {
        AdEntity result = adMapper.toAdEntity(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toEntity ====================

    @Test
    @DisplayName("toEntity должен преобразовать Ad DTO в Entity")
    void toEntity_ShouldMapAdDtoToEntity() {
        Ad dto = new Ad();
        dto.setPk(1);
        dto.setTitle("Test");
        dto.setPrice(1500);
        dto.setAuthor(1);
        dto.setImage("/images/ads/test.jpg");

        AdEntity result = adMapper.toEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test");
        assertThat(result.getPrice()).isEqualTo(1500);
    }

    // ==================== Тесты граничных случаев ====================

    @Test
    @DisplayName("toDto должен обработать Entity с минимальными данными")
    void toDto_ShouldHandleEntityWithMinimalData() {
        AdEntity minimal = new AdEntity();
        minimal.setId(1);
        minimal.setTitle("Min");
        minimal.setPrice(100);
        minimal.setDescription("Desc");
        minimal.setAuthor(author);

        Ad result = adMapper.toDto(minimal);

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("Min");
        assertThat(result.getPrice()).isEqualTo(100);
        assertThat(result.getImage()).isNull();
    }

    @Test
    @DisplayName("toExtendedAd должен обработать автора с null полями")
    void toExtendedAd_ShouldHandleAuthorWithNullFields() {
        UserEntity minimalAuthor = new UserEntity();
        minimalAuthor.setId(1);
        minimalAuthor.setEmail("min@test.com");

        adEntity.setAuthor(minimalAuthor);

        ExtendedAd result = adMapper.toExtendedAd(adEntity);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("min@test.com");
        assertThat(result.getAuthorFirstName()).isNull();
        assertThat(result.getAuthorLastName()).isNull();
        assertThat(result.getPhone()).isNull();
    }

    @Test
    @DisplayName("toAdEntity должен обработать DTO с null полями")
    void toAdEntity_ShouldHandleDtoWithNullFields() {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Only Title");

        AdEntity result = adMapper.toAdEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Only Title");
        assertThat(result.getPrice()).isNull();
        assertThat(result.getDescription()).isNull();
    }

    @Test
    @DisplayName("toDto должен корректно маппить pk из id")
    void toDto_ShouldMapPkFromId() {
        adEntity.setId(999);

        Ad result = adMapper.toDto(adEntity);

        assertThat(result.getPk()).isEqualTo(999);
    }

    @Test
    @DisplayName("toExtendedAd должен корректно маппить pk из id")
    void toExtendedAd_ShouldMapPkFromId() {
        adEntity.setId(888);

        ExtendedAd result = adMapper.toExtendedAd(adEntity);

        assertThat(result.getPk()).isEqualTo(888);
    }
}
