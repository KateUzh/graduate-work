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

    @Mapping(
            target = "image",
            expression = "java(entity.getImage() == null ? null : \"/images/users/\" + entity.getImage())"
    )
    User toDto(UserEntity entity);

    @Mapping(
            target = "image", ignore = true)
    UserEntity toEntity(User dto);

    @Mapping(
            target = "image", ignore = true)
    void updateFromDto(UpdateUser dto, @MappingTarget UserEntity entity);

    @Mapping(
            target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity fromRegister(Register register);

    default UserEntity.Role map(User.RoleEnum roleEnum) {
        return roleEnum == null ? null : UserEntity.Role.valueOf(roleEnum.name());
    }

    default User.RoleEnum map(UserEntity.Role role) {
        return role == null ? null : User.RoleEnum.valueOf(role.name());
    }
}
