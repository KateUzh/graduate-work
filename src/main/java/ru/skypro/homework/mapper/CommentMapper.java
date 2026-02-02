package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Mapper для преобразования между сущностью комментария (CommentEntity)
 * и DTO (Comment, CreateOrUpdateComment).
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserMapper.class
)
public interface CommentMapper {

    /**
     * Преобразует сущность комментария в DTO.
     *
     * @param entity сущность комментария
     * @return DTO комментария
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage")
    Comment toDto(CommentEntity entity);

    /**
     * Преобразует DTO комментария в сущность.
     *
     * @param dto DTO комментария
     * @return сущность комментария
     */
    @Mapping(source = "author", target = "author.id")
    CommentEntity toEntityFromComment(Comment dto);

    /**
     * Преобразует DTO создания или обновления комментария в сущность.
     *
     * @param dto DTO для создания или обновления
     * @return сущность комментария
     */
    CommentEntity toEntityFromCreateOrUpdateComment(CreateOrUpdateComment dto);
}
