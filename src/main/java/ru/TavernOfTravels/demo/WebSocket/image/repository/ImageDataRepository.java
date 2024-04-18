package ru.TavernOfTravels.demo.WebSocket.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.TavernOfTravels.demo.WebSocket.image.model.ImageData;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface ImageDataRepository extends JpaRepository<ImageData,Long> {

    ImageData findByName(String fileName);

    List<ImageData> findByRoomId(UUID roomId);
}
