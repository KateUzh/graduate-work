package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(
            value = "/users/{filename}",
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_GIF_VALUE
            }
    )
    public byte[] getUserImage(@PathVariable String filename) {
        return imageService.loadUserImage(filename);
    }

    @GetMapping(
            value = "/ads/{filename}",
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_GIF_VALUE
            }
    )
    public byte[] getAdImage(@PathVariable String filename) {
        return imageService.loadAdImage(filename);
    }
}

