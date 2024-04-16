package ru.TavernOfTravels.demo.WebSocket.image.controller;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("roomId") String roomId) {
        try {
            String imageUrl = imageService.saveImage(file, roomId);

            messagingTemplate.convertAndSend("/topic/image/upload/" + roomId, imageUrl);

            return ResponseEntity.ok().body("Image uploaded successfully. URL: " + imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/{roomId}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("roomId") String roomId, @PathVariable("imageName") String imageName) {
        Resource imageResource = imageService.getImage(roomId, imageName);

        if (imageResource != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/position")
    public ResponseEntity<ImageData> createPosition(
            @RequestParam("roomId") String roomId, @RequestParam("imageName") String imageName, @RequestParam ImageData imageData
    ) {
        Resource imageResource = imageService.getImage(roomId, imageName);
        if (imageResource != null) {
            var imagePos = new ImageData().builder()
                    .x(imageData.getX())
                    .y(imageData.getY())
                    .rotation(imageData.getRotation())
                    .scaleX(imageData.getScaleX())
                    .scaleY(imageData.getScaleY())
                    .locked(imageData.isLocked())
                    .build();
            imageService.saveImagePos(imagePos);
            messagingTemplate.convertAndSend("/topic/image/position/" + roomId, imagePos);
            return ResponseEntity.ok().body(imagePos);
        }
        return null;
    }

    @GetMapping("/{roomId}/{imageName}/imagePos")
    public ResponseEntity<ImageData> createPosition(
            @PathVariable("roomId") String roomId, @PathVariable("imageName") String imageName
    ) {
        Resource imageResource = imageService.getImage(roomId, imageName);
        ImageData imageData = imageService.getImagePos(imageName);
        if (imageResource != null && imageData != null) {
            return ResponseEntity.ok()
                    .body(imageData);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
