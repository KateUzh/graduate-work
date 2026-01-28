package ru.skypro.homework.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.Optional;

@RestController
public class AdssController {

    private final AdsApiDelegateImpl delegate;
    private final AdServiceImpl adService;


    public AdssController(AdsApiDelegateImpl delegate, AdServiceImpl adService) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AdsApiDelegateImpl(adService) {});
        this.adService = adService;
    }

    @PostMapping(value = "/v1/adss", consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(CreateOrUpdateAd properties, @RequestParam("image") MultipartFile image, Authentication auth) {
        return delegate.addAd(properties, image, auth);
    }
}
