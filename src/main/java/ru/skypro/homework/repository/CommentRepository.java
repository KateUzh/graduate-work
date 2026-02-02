package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Репозиторий для работы с комментариями к объявлениям.
 * Предоставляет CRUD операции и методы поиска комментариев по объявлению.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Находит все комментарии для конкретного объявления.
     *
     * @param adId ID объявления
     * @return список комментариев
     */
    List<CommentEntity> findAllByAdId(Integer adId);
}
