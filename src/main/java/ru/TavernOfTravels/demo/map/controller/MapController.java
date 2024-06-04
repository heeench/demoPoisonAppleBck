package ru.TavernOfTravels.demo.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.TavernOfTravels.demo.map.entity.Map;
import ru.TavernOfTravels.demo.map.service.MapService;

import java.util.UUID;

@Controller
@RequestMapping("/api/map")
public class MapController {
    @Autowired
    private final MapService service;

    public MapController(MapService service) {
        this.service = service;
    }

    @PostMapping("/{roomId}/save")
    public ResponseEntity<?> saveMap(@RequestBody Map map, @PathVariable UUID roomId) {
        try {
            service.saveMap(map, roomId);
            return ResponseEntity.ok().body("Map was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to upload map.");
        }
    }

    @GetMapping("/{roomId}/load")
    public ResponseEntity<?> loadMap(@PathVariable UUID roomId) {
        Map map = service.loadMap(roomId);
        if (map == null) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok().body(map);
    }
}
