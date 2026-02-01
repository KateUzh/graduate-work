package ru.skypro.homework.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.List;

@Component
public class AdsApiDelegateImpl implements AdsApiDelegate {

    private final AdServiceImpl adService;
    private final CommentServiceImpl commentService;

    public AdsApiDelegateImpl(AdServiceImpl adService, CommentServiceImpl commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    public ResponseEntity<Ad> addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth) {
        return ResponseEntity.ok(adService.createAd(properties, image, auth));
    }

    @Override
    public ResponseEntity<Comment> addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, Authentication auth) {
        return ResponseEntity.ok(commentService.addComment(id, createOrUpdateComment, auth));
    }


    @Override
    public ResponseEntity<Void> deleteComment(Integer adId, Integer commentId, Authentication auth) {
        commentService.deleteComment(adId, commentId, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ExtendedAd> getAds(Integer id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @Override
    public ResponseEntity<Ads> getAdsMe(Authentication auth) {
        return ResponseEntity.ok(adService.getMyAds(auth));
    }

    @Override
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Override
    public ResponseEntity<Comments> getComments(Integer id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @Override
    public ResponseEntity<Void> removeAd(Integer id, Authentication auth) {
        adService.removeAd(id, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Ad> updateAds(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication auth) {
        return ResponseEntity.ok(adService.updateAds(id, createOrUpdateAd, auth));
    }

    @Override
    public ResponseEntity<Comment> updateComment(Integer adId, Integer commentId,
                                                 CreateOrUpdateComment createOrUpdateComment,
                                                 Authentication auth) {
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, createOrUpdateComment, auth));
    }

    @Override
    public ResponseEntity<List<byte[]>> updateImage(Integer id, MultipartFile image, Authentication auth) {
        return ResponseEntity.ok(adService.updateImage(id, image, auth));
    }
}
