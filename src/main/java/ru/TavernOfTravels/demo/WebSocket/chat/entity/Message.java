package ru.TavernOfTravels.demo.WebSocket.chat.entity;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
public class Message {

    private UUID id;
    private String sender;
    private String message;
    private UUID roomId;
    private Date timestamp;

    public Message(UUID messageId, String nickname, String messageContent, UUID roomId, Date date) {
        this.id = messageId;
        this.sender = nickname;
        this.message = messageContent;
        this.roomId = roomId;
        this.timestamp = date;
    }
}