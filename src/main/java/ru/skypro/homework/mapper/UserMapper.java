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
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    /**
     * Преобразует DTO регистрации в сущность пользователя.
     * Игнорирует поля id, role и image.
     *
     * @param register DTO регистрации
     * @return сущность пользователя
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity fromRegister(Register register);

    /**
     * Обновляет сущность пользователя на основе DTO.
     *
     * @param dto DTO обновления пользователя
     * @param entity сущность пользователя для обновления
     */
    void updateFromDto(UpdateUser dto, @MappingTarget UserEntity entity);

    /**
     * Преобразует сущность пользователя в DTO.
     *
     * @param entity сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(target = "image", expression = "java(toUserImage(entity.getImage()))")
    User toDto(UserEntity entity);

    /**
     * Преобразует DTO пользователя в сущность.
     * Поле image игнорируется.
     *
     * @param dto DTO пользователя
     * @return сущность пользователя
     */
    @Mapping(target = "image", ignore = true)
    UserEntity toEntity(User dto);

    /**
     * Формирует путь к изображению пользователя.
     *
     * @param filename имя файла
     * @return путь к изображению или null
     */
    default String toUserImage(String filename) {
        return filename == null ? null : "/images/users/" + filename;
    }

    /**
     * Конвертирует RoleEnum в Role.
     *
     * @param roleEnum роль из DTO
     * @return роль сущности
     */
    default UserEntity.Role map(User.RoleEnum roleEnum) {
        return roleEnum == null ? null : UserEntity.Role.valueOf(roleEnum.name());
    }

    /**
     * Конвертирует Role в RoleEnum.
     *
     * @param role роль сущности
     * @return роль DTO
     */
    default User.RoleEnum map(UserEntity.Role role) {
        return role == null ? null : User.RoleEnum.valueOf(role.name());
    }
}
