package ru.TavernOfTravels.demo.WebSocket.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import ru.TavernOfTravels.demo.WebSocket.chat.entity.Message;
import ru.TavernOfTravels.demo.room.repository.RoomRepository;



@Controller
@CrossOrigin("http://localhost:3000")
public class ChatMessageHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<WebSocketSession, UUID> sessionRoomMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<Message>> roomMessages = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public ChatMessageHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void handleChatMessage(@Payload Message chatMessage) {
        String nickname = chatMessage.getSender();
        UUID roomId = chatMessage.getRoomId();
        String messageContent = chatMessage.getMessage();

        if (nickname != null && roomId != null && roomRepository.existsById(roomId)) {
            UUID messageId = UUID.randomUUID();
            Message message = new Message(messageId, nickname, messageContent, roomId, new Date());
            saveMessage(roomId, message);
            sendMessageToAllInRoom(roomId, message);
        }
    }

    private void saveMessage(UUID roomId, Message message) {
        roomMessages.computeIfAbsent(roomId, key -> new ArrayList<>()).add(message);
    }

    private void sendMessageToAllInRoom(UUID roomId, Message message) {
        messagingTemplate.convertAndSend("/topic/messages/" + roomId, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionRoomMap.put(session, UUID.fromString((String) session.getAttributes().get("roomId")));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionRoomMap.remove(session);
    }

}