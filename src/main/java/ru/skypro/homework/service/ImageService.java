package ru.skypro.homework.service;


import org.springframework.web.multipart.MultipartFile;


public interface ImageService {

    String saveUserImage(MultipartFile file);

    String saveAdImage(MultipartFile file);

    byte[] loadUserImage(String filename);

    byte[] loadAdImage(String filename);
}
