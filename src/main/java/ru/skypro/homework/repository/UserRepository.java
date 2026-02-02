package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Предоставляет CRUD операции и методы поиска пользователей по email.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с пользователем, если найден
     */
    Optional<UserEntity> findByEmail(String email);
}
