package ru.skypro.homework.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
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

import java.util.List;

@Component
public class AdsApiDelegateImpl implements AdsApiDelegate {

    private final AdServiceImpl adService;

    public AdsApiDelegateImpl(AdServiceImpl adService) {
        this.adService = adService;
    }

    public ResponseEntity<Ad> addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication auth) {
        return adService.createAd(properties, image, auth);
    }

//    @Override
//    public ResponseEntity<Comment> addComment(Integer id, CreateOrUpdateComment createOrUpdateComment) {
//
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteComment(Integer adId, Integer commentId) {
//
//    }
//
//    @Override
//    public ResponseEntity<ExtendedAd> getAds(Integer id) {
//
//    }
//
//    @Override
//    public ResponseEntity<Ads> getAdsMe() {
//
//    }
//
//    @Override
//    public ResponseEntity<Ads> getAllAds() {
//
//    }
//
//    @Override
//    public ResponseEntity<Comments> getComments(Integer id) {
//
//    }
//
//    @Override
//    public ResponseEntity<Void> removeAd(Integer id) {
//
//    }
//
//    @Override
//    public ResponseEntity<Ad> updateAds(Integer id, CreateOrUpdateAd createOrUpdateAd) {
//
//    }
//
//    @Override
//    public ResponseEntity<Comment> updateComment(Integer adId, Integer commentId,
//                                                 CreateOrUpdateComment createOrUpdateComment){
//
//    }
//
//    @Override
//    public ResponseEntity<List<byte[]>> updateImage(Integer id, MultipartFile image){
//
//    }
}
