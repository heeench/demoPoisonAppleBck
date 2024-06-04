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
import java.util.List;
import java.util.UUID;


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
        var image = new ImageData().builder()
                .name(fileName)
                .imagePath("http://localhost:8080/api/images/" + roomId + "/" + fileName)
                .roomId(UUID.fromString(roomId))
                .x(200)
                .y(100)
                .rotation(0)
                .scaleY(0.5)
                .scaleX(0.5)
                .build();
        saveImagePos(image);

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
        if (imageDataRepository.findByName(imagePos.getName()) == null) {
            imageDataRepository.save(imagePos);
        } else { ImageData existingImageData = imageDataRepository.findByName(imagePos.getName());
            if (existingImageData != null) {
                existingImageData.setImagePath(imagePos.getImagePath());
                existingImageData.setName(imagePos.getName());
                existingImageData.setTokenName(imagePos.getTokenName());
                existingImageData.setX(imagePos.getX());
                existingImageData.setY(imagePos.getY());
                existingImageData.setScaleX(imagePos.getScaleX());
                existingImageData.setScaleY(imagePos.getScaleY());
                existingImageData.setRotation(imagePos.getRotation());
                existingImageData.setLocked(imagePos.isLocked());
                imageDataRepository.save(existingImageData);
            }
        }
    }

    public ImageData getImagePos(String imageName) {
        return imageDataRepository.findByName(imageName);
    }


//    public boolean deleteImage(String imageName) {
//        ImageData image = imageDataRepository.findByName(imageName);
//        Long imageId = image.getId();
//        System.out.println(imageName +  " " + imageId);
//        imageDataRepository.deleteById(imageId);
//        if (imageDataRepository.findByName(imageName) == null) {
//            return true;
//        } else { return false; }
//    }

    public List<ImageData> fetchContent(UUID roomId) {
        List<ImageData> images = imageDataRepository.findByRoomId(roomId);
        return images;
    }
}
