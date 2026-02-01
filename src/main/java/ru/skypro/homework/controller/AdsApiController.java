package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.api.AdsApi;
import ru.skypro.homework.api.AdsApiDelegate;
import ru.skypro.homework.api.AdsApiDelegateImpl;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import javax.annotation.Generated;
import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "http://localhost:3000")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Controller
@RequestMapping("${openapi.aPIDocumentation.base-path:}")
public class AdsApiController implements AdsApi {

    private final AdsApiDelegateImpl delegate;
    private final AdServiceImpl adService;
    private final CommentServiceImpl commentService;

    public AdsApiController(@Autowired(required = false) AdsApiDelegateImpl delegate, AdServiceImpl adService, CommentServiceImpl commentService) {
        this.commentService = commentService;
        this.delegate = Optional.ofNullable(delegate).orElse(new AdsApiDelegateImpl(adService, commentService) {
        });
        this.adService = adService;
    }

    @Override
    public AdsApiDelegate getDelegate() {
        return delegate;
    }

    @PostMapping(value = "/ads", consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(CreateOrUpdateAd properties, @RequestParam("image") MultipartFile image, Authentication auth) {
        return delegate.addAd(properties, image, auth);
    }

    @Override
    public ResponseEntity<Comment> addComment(Integer id, CreateOrUpdateComment createOrUpdateComment,
                                              Authentication auth) {
        return delegate.addComment(id, createOrUpdateComment, auth);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Integer adId, Integer commentId, Authentication auth) {
        return delegate.deleteComment(adId, commentId, auth);
    }

    @Override
    public ResponseEntity<ExtendedAd> getAds(Integer id) {
        return delegate.getAds(id);
    }

    @Override
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return delegate.getAdsMe(authentication);
    }

    @Override
    public ResponseEntity<Ads> getAllAds() {
        return delegate.getAllAds();
    }

    @Override
    public ResponseEntity<Comments> getComments(Integer id) {
        return delegate.getComments(id);
    }

    @Override
    public ResponseEntity<Void> removeAd(Integer id, Authentication auth) {
        return delegate.removeAd(id, auth);
    }

    @Override
    public ResponseEntity<Ad> updateAds(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication auth) {
        return delegate.updateAds(id, createOrUpdateAd, auth);
    }

    @Override
    public ResponseEntity<Comment> updateComment(Integer adId, Integer commentId,
                                                 CreateOrUpdateComment createOrUpdateComment,
                                                 Authentication auth) {
        return delegate.updateComment(adId, commentId, createOrUpdateComment, auth);
    }

    @Override
    public ResponseEntity<List<byte[]>> updateImage(Integer id, MultipartFile image, Authentication auth) {
        return delegate.updateImage(id, image, auth);
    }

}
