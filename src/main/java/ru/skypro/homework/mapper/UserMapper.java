package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Mapper для преобразования между сущностью пользователя (UserEntity)
 * и DTO (User, Register, UpdateUser).
 */
@Mapper
        (componentModel = "spring",
                unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface UserMapper {

    /**
     * Преобразует сущность пользователя в DTO.
     * Добавляет префикс пути к изображению.
     *
     * @param entity сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(
            target = "image",
            expression = "java(entity.getImage() == null ? null : \"/images/users/\" + entity.getImage())"
    )
    User toDto(UserEntity entity);

    /**
     * Преобразует DTO пользователя в сущность.
     *
     * @param dto DTO пользователя
     * @return сущность пользователя
     */
    @Mapping(
            target = "image", ignore = true)
    UserEntity toEntity(User dto);

    /**
     * Обновляет существующую сущность пользователя данными из DTO.
     *
     * @param dto DTO с новыми данными
     * @param entity сущность для обновления
     */
    @Mapping(
            target = "image", ignore = true)
    void updateFromDto(UpdateUser dto, @MappingTarget UserEntity entity);

    /**
     * Создает сущность пользователя из DTO регистрации.
     *
     * @param register DTO регистрации
     * @return новая сущность пользователя
     */
    @Mapping(
            target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity fromRegister(Register register);

    /**
     * Преобразует enum роли из DTO в enum роли сущности.
     *
     * @param roleEnum роль из DTO
     * @return роль сущности
     */
    default UserEntity.Role map(User.RoleEnum roleEnum) {
        return roleEnum == null ? null : UserEntity.Role.valueOf(roleEnum.name());
    }

    /**
     * Преобразует enum роли сущности в enum роли DTO.
     *
     * @param role роль сущности
     * @return роль DTO
     */
    default User.RoleEnum map(UserEntity.Role role) {
        return role == null ? null : User.RoleEnum.valueOf(role.name());
    }
}
