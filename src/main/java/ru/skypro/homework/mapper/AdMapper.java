package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.CreateOrUpdateAd;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdMapper {

    @Mapping(source = "author.id", target = "author")
    Ad toDto(AdEntity entity);

    @Mapping(source = "author", target = "author.id")
    AdEntity toEntity(Ad dto);

    AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd);
}


