package ru.TavernOfTravels.demo.WebSocket.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;

import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData,Long> {

    ImageData findByName(String fileName);
}
