package ru.TavernOfTravels.demo.room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.TavernOfTravels.demo.room.model.Room;
import ru.TavernOfTravels.demo.room.service.RoomService;
import ru.TavernOfTravels.demo.user.model.User;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    @CrossOrigin("http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(@RequestParam("email") String email) {
        List<Room> rooms = roomService.getAllRoomsByEmail(email);
        return ResponseEntity.ok(rooms);
    }

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody Room room, @RequestParam("email") String email) {
        Room createdRoom = roomService.createRoom(room, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/{roomId}/users/{email}")
    public ResponseEntity<Void> addUserToRoom(@PathVariable UUID roomId, @PathVariable String email) {
        roomService.addUserToRoom(roomId, email);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/{roomId}/users")
    public ResponseEntity<List<User>> getUsersInRoom(@PathVariable UUID roomId) {
        List<User> users = roomService.getUsersInRoom(roomId);
        return ResponseEntity.ok(users);
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/{roomId}")
    public ResponseEntity<UUID> enterTheRoom(@PathVariable UUID roomId){
        UUID getRoom = roomService.enterTheRoom(roomId);
        return ResponseEntity.ok().body(getRoom);
    }
}
