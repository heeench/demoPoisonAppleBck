package ru.TavernOfTravels.demo.map.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.TavernOfTravels.demo.map.entity.Map;

import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface MapRepository extends JpaRepository<Map, Long> {
    Map findByRoomId(UUID roomId);

}
