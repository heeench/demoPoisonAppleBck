package ru.TavernOfTravels.demo.user.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private String nickname;
    private String email;
    private UUID roomId;

    public UserDTO(String nickname, String email, UUID roomId) {
        this.nickname = nickname;
        this.email = email;
        this.roomId = roomId;
    }
}
