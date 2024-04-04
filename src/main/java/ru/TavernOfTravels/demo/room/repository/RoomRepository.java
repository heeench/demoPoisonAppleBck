package ru.TavernOfTravels.demo.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.TavernOfTravels.demo.room.model.Room;

import java.util.List;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
        List<Room> findByCreatedByUserEmail(String email);

        boolean existsByIdAndCreatedByUserEmail(UUID roomUUID, String userEmail);
}

