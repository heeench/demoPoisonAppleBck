package ru.TavernOfTravels.demo.WebSocket.image.controller;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.TavernOfTravels.demo.WebSocket.image.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ImageController(ImageService imageService, SimpMessagingTemplate messagingTemplate) {
        this.imageService = imageService;
        this.messagingTemplate = messagingTemplate;
    }

    // Метод для загрузки изображения на сервер и отправки его URL клиентам
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("roomId") String roomId) {
        try {
            // Сохраняем изображение и получаем его URL
            String imageUrl = imageService.saveImage(file, roomId);

            // Отправляем URL всем клиентам через WebSocket
            messagingTemplate.convertAndSend("/topic/image/upload/" + roomId, imageUrl);

            return ResponseEntity.ok().body("Image uploaded successfully. URL: " + imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/{roomId}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("roomId") String roomId, @PathVariable("imageName") String imageName) {
        // Получаем ресурс изображения по его имени и roomId
        Resource imageResource = imageService.getImage(roomId, imageName);

        // Проверяем, существует ли такое изображение
        if (imageResource != null) {
            // Если существует, возвращаем его с помощью ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Здесь вы можете указать правильный MIME тип для вашего изображения
                    .body(imageResource);
        } else {
            // Если изображение не найдено, возвращаем код состояния 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}

