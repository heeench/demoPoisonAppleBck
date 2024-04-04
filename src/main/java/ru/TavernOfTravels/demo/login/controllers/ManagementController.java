package ru.TavernOfTravels.demo.login.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.TavernOfTravels.demo.response.ResponseHandler;
import ru.TavernOfTravels.demo.user.model.User;
import ru.TavernOfTravels.demo.user.services.UserServiceImpl;

@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Management")
public class ManagementController {

    private final UserServiceImpl service;

    public ManagementController(UserServiceImpl service) {
        this.service = service;
    }

    @Operation(
            description = "Get endpoint for manager",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public ResponseEntity<Object> get() {
        return ResponseHandler.responseBuilder( "Users data displayed below",
                    HttpStatus.OK,
                    service.readAll());
    }
    @PostMapping
    public String post() {
        return "POST:: management controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: management controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: management controller";
    }


    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> read(@PathVariable(name = "id") long id) {

        return ResponseEntity.ok((service.read(id)));
    }

//    @PutMapping("/user/{id}")
//    public ResponseEntity<Object> update(@PathVariable(name = "id") long id, @RequestBody User user) {
//
//        return ResponseEntity.ok(service.update(user, id));
//    }
//
}