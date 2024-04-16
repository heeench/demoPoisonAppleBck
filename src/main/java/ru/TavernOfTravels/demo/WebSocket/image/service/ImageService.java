package ru.TavernOfTravels.demo.WebSocket.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;
import ru.TavernOfTravels.demo.WebSocket.image.repository.ImageDataRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    public ImageDataRepository imageDataRepository;

    @Value("${images.directory}")
    private String imagesDirectory;

    public String saveImage(MultipartFile file, String roomId) throws IOException {
        createRoomDirectory(roomId);

        String fileName = generateFileName(file.getOriginalFilename());
        String roomPath = imagesDirectory + File.separator + roomId;
        Path path = Paths.get(roomPath, fileName);
        Files.write(path, file.getBytes());

        return "http://localhost:8080/api/images/" + roomId + "/" + fileName;
    }

    private void createRoomDirectory(String roomId) {
        String roomPath = imagesDirectory + File.separator + roomId;
        File directory = new File(roomPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String generateFileName(String originalFileName) {
        return "image_" + System.currentTimeMillis() + "_" + originalFileName;
    }

    public Resource getImage(String roomId, String imageName) {
        try {
            Path imagePath = Paths.get(imagesDirectory, roomId, imageName);
            Resource imageResource = new UrlResource(imagePath.toUri());

            if (imageResource.exists() || imageResource.isReadable()) {
                return imageResource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveImagePos(ImageData imagePos) {
        imageDataRepository.save(imagePos);
    }

    public ImageData getImagePos(String imageName) {
        return imageDataRepository.findByName(imageName);
    }
}
