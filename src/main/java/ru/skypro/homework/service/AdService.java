package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

/**
 * Сервис для работы с объявлениями.
 * Содержит бизнес-логику создания и получения объявлений.
 */
public interface AdService {

    /**
     * Создаёт новое объявление.
     *
     * @param properties данные объявления
     * @param image изображение объявления
     * @param auth данные аутентификации пользователя
     * @return созданное объявление
     */
    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth);

    /**
     * Получает список всех объявлений.
     *
     * @return список объявлений
     */
    Ads getAllAds();

    /**
     * Получает объявление по идентификатору.
     *
     * @param id идентификатор объявления
     * @return расширенная информация об объявлении
     */
    ExtendedAd getAd(Integer id);
}
