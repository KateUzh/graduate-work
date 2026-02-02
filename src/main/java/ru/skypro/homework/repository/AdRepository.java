package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с объявлениями.
 * Предоставляет CRUD операции и методы поиска объявлений по автору.
 */
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Находит все объявления конкретного пользователя.
     *
     * @param authorId ID автора
     * @return список объявлений пользователя
     */
    List<AdEntity> findAllByAuthorId(Integer authorId);
}
