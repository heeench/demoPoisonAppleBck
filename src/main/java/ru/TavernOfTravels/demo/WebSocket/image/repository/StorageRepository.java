package ru.TavernOfTravels.demo.WebSocket.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {

    Optional<ImageData> findByName(String fileName);
}
