package ru.TavernOfTravels.demo.WebSocket.image.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.TavernOfTravels.demo.WebSocket.image.service.ImageService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private ImageService service;

    @PostMapping("/imageStorage")
    public ResponseEntity<?> postImage(@RequestParam("image") MultipartFile file, @RequestParam("roomId") UUID roomId) throws IOException {
        String uploadImage = service.postImage(file, roomId);
        String name = file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK)
                .body(name);
    }

    @GetMapping("/imageStorage/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = service.getImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }
}
