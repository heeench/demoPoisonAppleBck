package ru.TavernOfTravels.demo.WebSocket.image.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID roomId;
    private String name;
    private String tokenName;
    private String imagePath;
    private double x;
    private double y;
    private double rotation;
    private double scaleX;
    private double scaleY;
    private boolean locked;
}