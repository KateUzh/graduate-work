package ru.skypro.homework.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
public class AdssController {

    private final AdsApiDelegateImpl delegate;
    private final AdServiceImpl adService;
    private final CommentServiceImpl commentService;


    public AdssController(AdsApiDelegateImpl delegate, AdServiceImpl adService, CommentServiceImpl commentService) {
        this.commentService = commentService;
        this.delegate = Optional.ofNullable(delegate).orElse(new AdsApiDelegateImpl(adService, commentService) {});
        this.adService = adService;
    }

    @PostMapping(value = "/v1/adss", consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(CreateOrUpdateAd properties, @RequestParam("image") MultipartFile image, Authentication auth) {
        return delegate.addAd(properties, image, auth);
    }

    @PatchMapping(value = "/v1/adss/{id}/image", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<byte[]>> updateImage(
            @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id,
            @Parameter(name = "image", description = "", required = true) @RequestParam("image") MultipartFile image,
            Authentication auth
    ){
        return delegate.updateImage(id, image, auth);
    }
}
