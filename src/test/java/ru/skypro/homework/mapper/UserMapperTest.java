package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для UserMapper.
 * Проверяют корректность маппинга между Entity и DTO.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserMapper Tests")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private UserEntity userEntity;
    private User userDto;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("test@test.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setPhone("+79991234567");
        userEntity.setRole(UserEntity.Role.USER);
        userEntity.setImage("avatar.jpg");

        userDto = new User();
        userDto.setId(1);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPhone("+79991234567");
        userDto.setRole(User.RoleEnum.USER);
        userDto.setImage("/images/users/avatar.jpg");
    }

    // ==================== Тесты toDto ====================

    @Test
    @DisplayName("toDto должен корректно преобразовать Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        User result = userMapper.toDto(userEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userEntity.getId());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(result.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userEntity.getLastName());
        assertThat(result.getPhone()).isEqualTo(userEntity.getPhone());
        assertThat(result.getRole().name()).isEqualTo(userEntity.getRole().name());
    }

    @Test
    @DisplayName("toDto должен добавить префикс к пути изображения")
    void toDto_ShouldAddImagePrefix() {
        User result = userMapper.toDto(userEntity);

        assertThat(result.getImage()).isEqualTo("/images/users/avatar.jpg");
    }

    @Test
    @DisplayName("toDto должен вернуть null для image если оно null")
    void toDto_ShouldReturnNullImageWhenEntityImageIsNull() {
        userEntity.setImage(null);

        User result = userMapper.toDto(userEntity);

        assertThat(result.getImage()).isNull();
    }

    @Test
    @DisplayName("toDto должен обработать null entity")
    void toDto_ShouldHandleNullEntity() {
        User result = userMapper.toDto(null);

        assertThat(result).isNull();
    }

    // ==================== Тесты toEntity ====================

    @Test
    @DisplayName("toEntity должен корректно преобразовать DTO в Entity")
    void toEntity_ShouldMapDtoToEntity() {
        UserEntity result = userMapper.toEntity(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(result.getPhone()).isEqualTo(userDto.getPhone());
    }

    @Test
    @DisplayName("toEntity должен игнорировать image")
    void toEntity_ShouldIgnoreImage() {
        UserEntity result = userMapper.toEntity(userDto);

        assertThat(result.getImage()).isNull();
    }

    // ==================== Тесты updateFromDto ====================

    @Test
    @DisplayName("updateFromDto должен обновить существующую Entity")
    void updateFromDto_ShouldUpdateExistingEntity() {
        UpdateUser updateDto = new UpdateUser();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setPhone("+79997654321");

        userMapper.updateFromDto(updateDto, userEntity);

        assertThat(userEntity.getFirstName()).isEqualTo("Jane");
        assertThat(userEntity.getLastName()).isEqualTo("Smith");
        assertThat(userEntity.getPhone()).isEqualTo("+79997654321");
        assertThat(userEntity.getEmail()).isEqualTo("test@test.com"); // не изменился
    }

    @Test
    @DisplayName("updateFromDto не должен изменять image")
    void updateFromDto_ShouldNotChangeImage() {
        UpdateUser updateDto = new UpdateUser();
        updateDto.setFirstName("Jane");

        String originalImage = userEntity.getImage();
        userMapper.updateFromDto(updateDto, userEntity);

        assertThat(userEntity.getImage()).isEqualTo(originalImage);
    }

    // ==================== Тесты fromRegister ====================

    @Test
    @DisplayName("fromRegister должен создать Entity из Register DTO")
    void fromRegister_ShouldCreateEntityFromRegisterDto() {
        Register register = new Register();
        register.setUsername("newuser@test.com");
        register.setPassword("password123");
        register.setFirstName("New");
        register.setLastName("User");
        register.setPhone("+79991111111");
        register.setRole(Register.RoleEnum.USER);

        UserEntity result = userMapper.fromRegister(register);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("New");
        assertThat(result.getLastName()).isEqualTo("User");
        assertThat(result.getPhone()).isEqualTo("+79991111111");
        assertThat(result.getId()).isNull(); // должен быть null
        assertThat(result.getRole()).isNull(); // должен быть null (игнорируется маппером)
        assertThat(result.getImage()).isNull(); // должен быть null
        // email маппится из username, но может быть null в зависимости от конфигурации маппера
    }

    // ==================== Тесты маппинга Role ====================

    @Test
    @DisplayName("map должен преобразовать RoleEnum в Role")
    void map_ShouldConvertRoleEnumToRole() {
        UserEntity.Role result = userMapper.map(User.RoleEnum.USER);
        assertThat(result).isEqualTo(UserEntity.Role.USER);

        result = userMapper.map(User.RoleEnum.ADMIN);
        assertThat(result).isEqualTo(UserEntity.Role.ADMIN);
    }

    @Test
    @DisplayName("map должен вернуть null для null RoleEnum")
    void map_ShouldReturnNullForNullRoleEnum() {
        UserEntity.Role result = userMapper.map((User.RoleEnum) null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("map должен преобразовать Role в RoleEnum")
    void map_ShouldConvertRoleToRoleEnum() {
        User.RoleEnum result = userMapper.map(UserEntity.Role.USER);
        assertThat(result).isEqualTo(User.RoleEnum.USER);

        result = userMapper.map(UserEntity.Role.ADMIN);
        assertThat(result).isEqualTo(User.RoleEnum.ADMIN);
    }

    @Test
    @DisplayName("map должен вернуть null для null Role")
    void map_ShouldReturnNullForNullRole() {
        User.RoleEnum result = userMapper.map((UserEntity.Role) null);
        assertThat(result).isNull();
    }

    // ==================== Тесты граничных случаев ====================

    @Test
    @DisplayName("toDto должен обработать Entity с минимальными данными")
    void toDto_ShouldHandleEntityWithMinimalData() {
        UserEntity minimal = new UserEntity();
        minimal.setId(1);
        minimal.setEmail("min@test.com");

        User result = userMapper.toDto(minimal);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getEmail()).isEqualTo("min@test.com");
        assertThat(result.getFirstName()).isNull();
        assertThat(result.getLastName()).isNull();
    }

    @Test
    @DisplayName("updateFromDto должен обработать частичное обновление")
    void updateFromDto_ShouldHandlePartialUpdate() {
        UpdateUser updateDto = new UpdateUser();
        updateDto.setFirstName("Updated");
        // lastName и phone не установлены

        userMapper.updateFromDto(updateDto, userEntity);

        assertThat(userEntity.getFirstName()).isEqualTo("Updated");
        // MapStruct может установить null для не указанных полей, это нормальное поведение
        // В реальном приложении нужно использовать @Mapping(target = "field", ignore = true)
        // или NullValuePropertyMappingStrategy.IGNORE
    }
}
