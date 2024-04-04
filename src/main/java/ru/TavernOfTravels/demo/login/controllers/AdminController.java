package ru.TavernOfTravels.demo.login.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.TavernOfTravels.demo.response.ResponseHandler;
import ru.TavernOfTravels.demo.user.model.User;
import ru.TavernOfTravels.demo.user.services.UserServiceImpl;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserServiceImpl service;

    public AdminController (UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Object> get() {
        return ResponseHandler.responseBuilder( "Users data displayed below",
                HttpStatus.OK,
                service.readAll());
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @Hidden
    public String post() {
        return "POST:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    @Hidden
    public String put() {
        return "PUT:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: admin controller";
    }

//    @GetMapping("/users")
//    @PreAuthorize("hasAuthority('admin:read')")
//    public  readAll() {
//        return ResponseEntity.ok();
//    }

    @GetMapping(value = "/user/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Object> read(@PathVariable(name = "id") long id) {

        return ResponseEntity.ok((service.read(id)));
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Object> update(@PathVariable(name = "id") long id, @RequestBody User user) {

        return ResponseEntity.ok(service.update(user, id));
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") long id) {

        return ResponseEntity.ok(service.delete(id));
    }
}