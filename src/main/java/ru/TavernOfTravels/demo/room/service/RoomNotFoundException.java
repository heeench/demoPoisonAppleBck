package ru.TavernOfTravels.demo.room.service;

public class RoomNotFoundException extends RuntimeException  {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
