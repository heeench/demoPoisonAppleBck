package ru.TavernOfTravels.demo.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.TavernOfTravels.demo.map.entity.Map;
import ru.TavernOfTravels.demo.map.repository.MapRepository;
import ru.TavernOfTravels.demo.room.repository.RoomRepository;

import java.util.UUID;

@Service
public class MapService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final MapRepository mapRepository;

    public MapService(RoomRepository roomRepository, MapRepository mapRepository) {
        this.roomRepository = roomRepository;
        this.mapRepository = mapRepository;
    }

    public void saveMap(Map map, UUID roomId) {
        Map existingMap = mapRepository.findByRoomId(roomId);
        if (existingMap != null) {
            existingMap.setWalls(map.getWalls());
            existingMap.setGridState(map.getGridState());
            existingMap.setX(map.getX());
            existingMap.setY(map.getY());
            mapRepository.save(existingMap);
        } else {
            if (roomRepository.existsById(roomId)) {
                Map createdMap = new Map().builder()
                        .walls(map.getWalls())
                        .gridState(map.getGridState())
                        .x(map.getX())
                        .y(map.getY())
                        .roomId(roomId)
                        .build();
                mapRepository.save(createdMap);
            } else {
                System.out.println("Room with id:" + roomId + " is not found");
            }
        }
    }


    public Map loadMap(UUID roomId) {
        return mapRepository.findByRoomId(roomId);
    }
}
