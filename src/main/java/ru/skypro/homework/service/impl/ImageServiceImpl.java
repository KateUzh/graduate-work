package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.images.users}")
    private String usersDir;

    @Value("${app.images.ads}")
    private String adsDir;

    @Override
    public String saveUserImage(MultipartFile file) {
        return save(file, usersDir);
    }

    @Override
    public String saveAdImage(MultipartFile file) {
        return save(file, adsDir);
    }

    @Override
    public byte[] loadUserImage(String filename) {
        return load(usersDir + "/" + filename);
    }

    @Override
    public byte[] loadAdImage(String filename) {
        return load(adsDir + "/" + filename);
    }

    private String save(MultipartFile file, String dir) {
        try {
            Files.createDirectories(Path.of(dir));

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Path.of(dir, filename);

            file.transferTo(path);
            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения изображения", e);
        }
    }

    private byte[] load(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Изображение не найдено", e);
        }
    }
}
