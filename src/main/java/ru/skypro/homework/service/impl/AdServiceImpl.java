package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public ResponseEntity<Ad> createAd(CreateOrUpdateAd properties, MultipartFile imageFile, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        AdEntity adEntity = adMapper.toAdEntity(properties);
        adEntity.setAuthor(user);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/images/ads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(filename);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                adEntity.setImage("/images/ads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        adRepository.save(adEntity);
        Ad ad = adMapper.toDto(adEntity);
        return ResponseEntity.ok(ad);
    }

    @Override
    public List<Ad> getAllAds() {
        return adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Ad getAd(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(IllegalStateException::new);
    }

    private UserEntity getUserFromAuth(Authentication auth){
        if (auth == null || !auth.isAuthenticated()){
            return null;
        }
        Object principal = auth.getPrincipal();
        if(principal instanceof CustomUserDetails){
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }
}
