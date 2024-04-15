package ru.TavernOfTravels.demo.WebSocket.image.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Value("${images.directory}")
    private String imagesDirectory;

    public String saveImage(MultipartFile file, String roomId) throws IOException {
        // Создаем папку для данной комнаты, если она не существует
        createRoomDirectory(roomId);

        // Генерируем уникальное имя для изображения
        String fileName = generateFileName(file.getOriginalFilename());

        // Путь к папке для данной комнаты
        String roomPath = imagesDirectory + File.separator + roomId;

        // Путь для сохранения изображения
        Path path = Paths.get(roomPath, fileName);

        // Сохраняем изображение
        Files.write(path, file.getBytes());

        // Возвращаем URL для загрузки изображения
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
            // Получаем путь к изображению
            Path imagePath = Paths.get(imagesDirectory, roomId, imageName);

            // Преобразуем путь в ресурс
            Resource imageResource = new UrlResource(imagePath.toUri());

            // Проверяем, существует ли файл по данному пути
            if (imageResource.exists() || imageResource.isReadable()) {
                return imageResource;
            } else {
                // Если файл не найден, возвращаем null
                return null;
            }
        } catch (MalformedURLException e) {
            // Обработка исключения в случае неверного URL
            e.printStackTrace();
            return null;
        }
    }
}
