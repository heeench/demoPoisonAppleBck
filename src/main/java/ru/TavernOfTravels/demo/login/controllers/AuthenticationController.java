package ru.TavernOfTravels.demo.login.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.TavernOfTravels.demo.login.auth.AuthenticationRequest;
import ru.TavernOfTravels.demo.login.auth.AuthenticationResponse;
import ru.TavernOfTravels.demo.login.auth.AuthenticationService;
import ru.TavernOfTravels.demo.login.auth.RegisterRequest;
import ru.TavernOfTravels.demo.user.model.User;

import javax.swing.text.AbstractDocument;
import javax.swing.text.html.HTMLDocument;
import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) { return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
