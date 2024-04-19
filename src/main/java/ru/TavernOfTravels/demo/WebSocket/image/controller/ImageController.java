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
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/images")
@CrossOrigin("http://localhost:3000")
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

            return ResponseEntity.ok().body(imageUrl);
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

    @GetMapping("/{roomId}/{imageName}/imagePos")
    public ResponseEntity<ImageData> getPosition(
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

    @GetMapping("/fetchContent/{roomId}")
    public ResponseEntity<List<ImageData>> fetchContent(@PathVariable("roomId") String roomId) {
        List<ImageData> images= imageService.fetchContent(UUID.fromString(roomId));
        System.out.println(images);
        return ResponseEntity.ok().body(images);
    }

//    @DeleteMapping("/delete/{imageName}")
//    public ResponseEntity<?> deleteImage(@PathVariable("imageName") String imageName) {
//        System.out.println(imageName);
//        imageService.deleteImage(imageName);
//        return ResponseEntity.ok().body("Image deleted successfully");
//    }
}
