package ru.TavernOfTravels.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.TavernOfTravels.demo.room.model.Room;
import ru.TavernOfTravels.demo.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u.nickname FROM User u WHERE u.id = :id")
    String findNicknameById(@Param("id") Integer id);
}
