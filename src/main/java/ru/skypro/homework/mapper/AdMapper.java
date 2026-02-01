package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdMapper {

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    Ad toDto(AdEntity entity);

    @Mapping(source = "author", target = "author.id")
    AdEntity toEntity(Ad dto);

    AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    ExtendedAd toExtendedAd(AdEntity ad);
}


