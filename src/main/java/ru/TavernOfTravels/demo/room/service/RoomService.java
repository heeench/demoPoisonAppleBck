package ru.TavernOfTravels.demo.room.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.TavernOfTravels.demo.room.model.Room;
import ru.TavernOfTravels.demo.room.repository.RoomRepository;
import ru.TavernOfTravels.demo.user.model.User;
import ru.TavernOfTravels.demo.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<Room> getAllRoomsByEmail(String email) {
        return roomRepository.findByCreatedByUserEmail(email);
    }

    public boolean existsByIdAndCreatedByUserEmail(UUID uuid, String email) {
        return roomRepository.existsByIdAndCreatedByUserEmail(uuid, email);
    }

    public Room createRoom(Room room, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        List<User> users = getUsersInRoom(room.getId());

        Room newRoom = Room.builder()
                .id(UUID.randomUUID())
                .name(room.getName())
                .isOpen(true)
                .createdByUser(user)
                .users(users)
                .build();

        return roomRepository.save(newRoom);
    }


    public void addUserToRoom(UUID roomId, String email) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + roomId));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (!room.getUsers().contains(user)) {
            room.getUsers().add(user);
            roomRepository.save(room);
        }
    }

    public List<User> getUsersInRoom(UUID roomId) {
        if (roomId == null) {
            return Collections.emptyList(); // Возвращаем пустой список, если идентификатор комнаты равен null
        }
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + roomId));
        return room.getUsers();
    }

    public UUID enterTheRoom(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + roomId));
        return room.getId();
    }

    public boolean existsById(UUID roomId) {
        return roomRepository.existsById(roomId);
    }
}

