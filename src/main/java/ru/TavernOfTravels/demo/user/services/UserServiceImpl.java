package ru.TavernOfTravels.demo.user.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.TavernOfTravels.demo.user.requests.ChangePasswordRequest;
import ru.TavernOfTravels.demo.user.model.User;
import ru.TavernOfTravels.demo.user.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }

    @Override
    public List<User> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> read(long id) {
        return repository.findById((int) id);
    }

    public String readNick(String email) { return repository.findByEmail(email).get().getNickname();}

    @Override
    public boolean update(User user, long id) {
        if (repository.existsById((int) id)) {
            user.setId((int) id);
            repository.save(user);
            return true;
        } return false;
    }

    @Override
    public boolean delete(long id) {
        if (repository.existsById((int) id)) {
            repository.deleteById((int) id);
            return true;
        } return false;
    }
}