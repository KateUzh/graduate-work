package ru.skypro.homework.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.model.CreateOrUpdateComment;
import ru.skypro.homework.model.ExtendedAd;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.impl.AdServiceImpl;

import javax.annotation.Generated;
import java.util.List;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Controller
@RequestMapping("${openapi.aPIDocumentation.base-path:}")
public class AdsApiController implements AdsApi {

    private final AdsApiDelegateImpl delegate;
    private final AdServiceImpl adService;

    public AdsApiController(@Autowired(required = false) AdsApiDelegateImpl delegate, AdServiceImpl adService) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AdsApiDelegateImpl(adService) {});
        this.adService = adService;
    }

    @Override
    public AdsApiDelegate getDelegate() {
        return delegate;
    }

    @PostMapping(value = "/adss", consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(@RequestParam ("image") MultipartFile image, Authentication auth) {
        return delegate.addAd(new CreateOrUpdateAd(), image, auth);
    }

    @Override
    public ResponseEntity<Comment> addComment(Integer id, CreateOrUpdateComment createOrUpdateComment) {
        return AdsApi.super.addComment(id, createOrUpdateComment);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Integer adId, Integer commentId) {
        return AdsApi.super.deleteComment(adId, commentId);
    }

    @Override
    public ResponseEntity<ExtendedAd> getAds(Integer id) {
        return AdsApi.super.getAds(id);
    }

    @Override
    public ResponseEntity<Ads> getAdsMe() {
        return AdsApi.super.getAdsMe();
    }

    @Override
    public ResponseEntity<Ads> getAllAds() {
        return AdsApi.super.getAllAds();
    }

    @Override
    public ResponseEntity<Comments> getComments(Integer id) {
        return AdsApi.super.getComments(id);
    }

    @Override
    public ResponseEntity<Void> removeAd(Integer id) {
        return AdsApi.super.removeAd(id);
    }

    @Override
    public ResponseEntity<Ad> updateAds(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        return AdsApi.super.updateAds(id, createOrUpdateAd);
    }

    @Override
    public ResponseEntity<Comment> updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        return AdsApi.super.updateComment(adId, commentId, createOrUpdateComment);
    }

    @Override
    public ResponseEntity<List<byte[]>> updateImage(Integer id, MultipartFile image) {
        return AdsApi.super.updateImage(id, image);
    }

}
