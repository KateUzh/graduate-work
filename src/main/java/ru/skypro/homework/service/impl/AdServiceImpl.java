package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.model.ExtendedAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public AdServiceImpl(AdRepository adRepository,
                         AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile imageFile, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        AdEntity adEntity = adMapper.toAdEntity(properties);
        adEntity.setAuthor(user);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/images/ads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(filename);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    imageFile.transferTo(filePath);
                }
                adEntity.setImage("/images/ads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        adRepository.save(adEntity);
        Ad ad = adMapper.toDto(adEntity);
        return ad;
    }

    @Override
    public Ads getAllAds() {
        List<Ad> ad = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        Ads ads = new Ads();
        ads.setCount(ad.size());
        ads.results(ad);
        return ads;
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity ad = adRepository.findById(id).orElseThrow(NullPointerException::new);
        return adMapper.toExtendedAd(ad);
    }

    public Ads getMyAds(Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<AdEntity> allByAuthorId = adRepository.findAllByAuthorId(user.getId());
        List<Ad> adsList = allByAuthorId.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(adsList.size());
        ads.setResults(adsList);

        return ads;
    }

    public void removeAd(Integer id, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity adEntity = adRepository.findById(id).orElseThrow(NullPointerException::new);
        if (!UserEntity.Role.ADMIN.equals(user.getRole()) || !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        adRepository.deleteById(id);
    }

    public Ad updateAds(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity adEntity = adRepository.findById(id).orElseThrow(NullPointerException::new);

        if (!UserEntity.Role.ADMIN.equals(user.getRole()) || !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setDescription(createOrUpdateAd.getDescription());

        adRepository.save(adEntity);
        return adMapper.toDto(adEntity);
    }

    public List<byte[]> updateImage(Integer id, MultipartFile imageFile, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AdEntity adEntity = adRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!UserEntity.Role.ADMIN.equals(user.getRole()) || !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (imageFile == null || imageFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is empty");
        }

        try {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/images/ads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            imageFile.transferTo(filePath);
            adEntity.setImage(filePath.toString());
            adRepository.save(adEntity);

            byte[] bytes = Files.readAllBytes(filePath);
            return List.of(bytes);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image", e);
        }
    }

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
