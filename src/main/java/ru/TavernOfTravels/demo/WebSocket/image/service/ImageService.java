package ru.TavernOfTravels.demo.WebSocket.image.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;
import ru.TavernOfTravels.demo.WebSocket.image.repository.StorageRepository;
import ru.TavernOfTravels.demo.WebSocket.image.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private StorageRepository repository;

    private final String FOLDER_PATH = "C:\\Users\\awrieu\\Desktop\\VScode\\PoisonAppleImages\\";
    @Transactional
    public String postImage(MultipartFile file, UUID roomId) throws IOException {
        String roomFolderPath = FOLDER_PATH + roomId.toString() + "\\";
        String filePath = roomFolderPath + file.getOriginalFilename();

        Path path = Paths.get(roomFolderPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .filePath(filePath).build());

        file.transferTo(new File(filePath));
        System.out.println( imageData.getImageData().toString());
        if (imageData != null) {
            return file.getOriginalFilename();
        }

        return null;
    }
    @Transactional
    public byte[] getImage(String fileName) throws IOException {
        Optional<ImageData> imageDataOptional = repository.findByName(fileName);
        if (imageDataOptional.isPresent()) {
            ImageData imageData = imageDataOptional.get();
            byte[] decompressedImageData = ImageUtils.decompressImage(imageData.getImageData());
            System.out.println( decompressedImageData.toString());
            return decompressedImageData;
        }
        return null;
    }
}