package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 * Содержит бизнес-логику создания, получения, обновления и удаления объявлений.
 * Реализует проверку прав доступа пользователей.
 */
@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    public AdServiceImpl(AdRepository adRepository,
                         AdMapper adMapper,
                         ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    /**
     * Создает новое объявление от имени авторизованного пользователя.
     *
     * @param properties данные объявления
     * @param image      изображение объявления
     * @param auth       данные аутентификации пользователя
     * @return созданное объявление в виде DTO
     */
    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity ad = adMapper.toAdEntity(properties);
        ad.setAuthor(user);

        if (image != null && !image.isEmpty()) {
            String filename = imageService.saveAdImage(image);
            ad.setImage(filename);
        }

        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    /**
     * Получает все объявления.
     *
     * @return объект Ads с полным списком объявлений
     */
    @Override
    public Ads getAllAds() {
        List<Ad> results = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    /**
     * Получает расширенную информацию об объявлении по ID.
     *
     * @param id идентификатор объявления
     * @return расширенное объявление
     */
    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return adMapper.toExtendedAd(ad);
    }

    /**
     * Получает объявления текущего пользователя.
     *
     * @param auth данные аутентификации
     * @return объект Ads с объявлениями пользователя
     */
    public Ads getMyAds(Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Ad> results = adRepository.findAllByAuthorId(user.getId())
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    /**
     * Удаляет объявление по ID.
     * Разрешено владельцу объявления или пользователю с ролью ADMIN.
     *
     * @param id   идентификатор объявления
     * @param auth данные аутентификации
     */
    public void removeAd(Integer id, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!UserEntity.Role.ADMIN.equals(user.getRole())
                && !ad.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        adRepository.delete(ad);
    }

    /**
     * Обновляет объявление.
     * Разрешено владельцу объявления или пользователю с ролью ADMIN.
     *
     * @param id  идентификатор объявления
     * @param dto новые данные объявления
     * @param auth данные аутентификации
     * @return обновленное объявление в виде DTO
     */
    public Ad updateAds(Integer id, CreateOrUpdateAd dto, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!UserEntity.Role.ADMIN.equals(user.getRole())
                && !ad.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());

        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id    идентификатор объявления
     * @param image новое изображение
     * @param auth  данные аутентификации
     * @return обновленное объявление в виде DTO
     */
    public Ad updateAdImage(Integer id, MultipartFile image, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!UserEntity.Role.ADMIN.equals(user.getRole())
                && !ad.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String filename = imageService.saveAdImage(image);
        ad.setImage(filename);

        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    /**
     * Извлекает пользователя из объекта Authentication.
     *
     * @param auth объект аутентификации
     * @return пользователь или null, если не авторизован
     */
    private UserEntity getUserFromAuth(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }
}
