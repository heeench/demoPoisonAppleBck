package ru.TavernOfTravels.demo.user.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.TavernOfTravels.demo.user.services.UserServiceImpl;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/secured")
public class MainController {

    private final UserServiceImpl service;

    public MainController(UserServiceImpl service) {
        this.service = service;
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/user")
    public String userAccess(Principal principal) {
        if (principal == null) { return null; }
        return service.readNick(principal.getName());
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/user/email")
    public String getUserEmail(Principal principal) {
        if (principal == null) { return null; }
        return principal.getName();
    }
}
