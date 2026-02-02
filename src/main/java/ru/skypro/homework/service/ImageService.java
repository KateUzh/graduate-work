package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для работы с изображениями пользователей и объявлений.
 * Отвечает за сохранение и загрузку изображений.
 */
public interface ImageService {

    /**
     * Сохраняет изображение пользователя.
     *
     * @param file файл изображения
     * @return имя сохранённого файла
     */
    String saveUserImage(MultipartFile file);

    /**
     * Сохраняет изображение объявления.
     *
     * @param file файл изображения
     * @return имя сохранённого файла
     */
    String saveAdImage(MultipartFile file);

    /**
     * Загружает изображение пользователя.
     *
     * @param filename имя файла изображения
     * @return массив байт изображения
     */
    byte[] loadUserImage(String filename);

    /**
     * Загружает изображение объявления.
     *
     * @param filename имя файла изображения
     * @return массив байт изображения
     */
    byte[] loadAdImage(String filename);
}
