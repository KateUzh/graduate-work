package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.CreateOrUpdateAd;

import java.util.List;

public interface AdService {

    ResponseEntity<Ad> createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth);

    List<Ad> getAllAds();

    Ad getAd(Integer id);
}
