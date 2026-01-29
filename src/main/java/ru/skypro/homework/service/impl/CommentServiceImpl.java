package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.CreateOrUpdateComment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
    }

    public Comment addComment(int adId, CreateOrUpdateComment dto, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + adId));

        CommentEntity commentEntity = commentMapper.toEntityFromCreateOrUpdateComment(dto);
        commentEntity.setAuthor(user);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(System.currentTimeMillis());

        CommentEntity saved = commentRepository.save(commentEntity);
        return commentMapper.toDto(saved);
    }

    public void deleteComment(int adId, int commentId, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        AdEntity adEntity = adRepository.findById(adId).orElseThrow(() ->
                new EntityNotFoundException("Ad not found with id: " + adId));

        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!UserEntity.Role.ADMIN.equals(user.getRole()) && !comment.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public Comments getComments(Integer adId) {
        List<Comment> result = commentRepository.findAllByAdId(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        return new Comments()
                .count(result.size())
                .results(result);
    }

    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment dto, Authentication auth) {
        UserEntity user = getUserFromAuth(auth);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        AdEntity adEntity = adRepository.findById(adId).orElseThrow(() ->
                new EntityNotFoundException("Ad not found with id: " + adId));


        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!UserEntity.Role.ADMIN.equals(user.getRole()) && !comment.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        comment.setText(dto.getText());
        comment.setCreatedAt(System.currentTimeMillis());
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    private UserEntity getUserFromAuth(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }
}
