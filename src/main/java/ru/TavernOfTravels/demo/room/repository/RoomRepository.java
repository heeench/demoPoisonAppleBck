package ru.TavernOfTravels.demo.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.TavernOfTravels.demo.room.model.Room;
import ru.TavernOfTravels.demo.user.model.User;

import java.util.List;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
        List<Room> findByCreatedByUserEmail(String email);

        boolean existsByIdAndCreatedByUserEmail(UUID roomUUID, String userEmail);

        @Query("SELECT r.createdByUser.id FROM Room r WHERE r.id = :roomId")
        Integer findCreatedByUserIdByRoomId(@Param("roomId") UUID roomId);
}

