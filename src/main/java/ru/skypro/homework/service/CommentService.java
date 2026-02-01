package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comments;

public interface CommentService {

    Comments getComments(Integer adId);
}
