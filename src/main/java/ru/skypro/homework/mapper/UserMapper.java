package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity fromRegister(Register register);

    void updateFromDto(UpdateUser dto, @MappingTarget UserEntity entity);

    @Mapping(target = "image", expression = "java(mapImage(entity.getImage()))")
    @Mapping(source = "role", target = "role")
    User toDto(UserEntity entity);

    @Mapping(source = "role", target = "role")
    UserEntity toEntity(User dto);

    default String mapImage(String image) {
        return image == null ? null : "/images/users/" + image;
    }

    default UserEntity.Role map(User.RoleEnum roleEnum) {
        return roleEnum == null ? null : UserEntity.Role.valueOf(roleEnum.name());
    }

    default User.RoleEnum map(UserEntity.Role role) {
        return role == null ? null : User.RoleEnum.valueOf(role.name());
    }
}
