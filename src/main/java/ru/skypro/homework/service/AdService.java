package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.model.ExtendedAd;

public interface AdService {

    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth);

    Ads getAllAds();

    ExtendedAd getAd(Integer id);
}
