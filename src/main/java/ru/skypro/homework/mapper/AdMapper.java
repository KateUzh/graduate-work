package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

/**
 * Mapper для преобразования между сущностью объявления (AdEntity)
 * и DTO (Ad, ExtendedAd, CreateOrUpdateAd).
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdMapper {

    /**
     * Преобразует сущность объявления в DTO.
     *
     * @param entity сущность объявления
     * @return DTO объявления
     */
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(target = "image", expression = "java(entity.getImage() != null ? \"/images/ads/\" + entity.getImage() : null)")
    Ad toDto(AdEntity entity);

    /**
     * Преобразует DTO объявления в сущность.
     *
     * @param dto DTO объявления
     * @return сущность объявления
     */
    @Mapping(source = "author", target = "author.id")
    AdEntity toEntity(Ad dto);

    /**
     * Преобразует DTO создания/обновления объявления в сущность.
     *
     * @param createOrUpdateAd DTO создания или обновления
     * @return сущность объявления
     */
    AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd);

    /**
     * Преобразует сущность объявления в расширенный DTO (с информацией об авторе).
     *
     * @param ad сущность объявления
     * @return расширенный DTO объявления
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/images/ads/\" + ad.getImage() : null)")
    ExtendedAd toExtendedAd(AdEntity ad);
}
