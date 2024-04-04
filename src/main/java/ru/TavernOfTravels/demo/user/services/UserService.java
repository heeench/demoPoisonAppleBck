package ru.TavernOfTravels.demo.user.services;

import ru.TavernOfTravels.demo.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> readAll();

    Optional<User> read(long id);

    boolean update(User user, long id);

    boolean delete(long id);
}
