package ru.TavernOfTravels.demo.map.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.exec.spi.JdbcOperationQueryDelete;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String gridState;

    @Column(columnDefinition = "TEXT")
    private String walls;
    private double x;
    private double y;
    private UUID roomId;
}

