package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comments;

/**
 * Сервис для работы с комментариями к объявлениям.
 */
public interface CommentService {

    /**
     * Получает список комментариев для объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев
     */
    Comments getComments(Integer adId);
}
