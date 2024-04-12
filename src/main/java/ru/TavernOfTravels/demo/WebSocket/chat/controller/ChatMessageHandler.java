package ru.TavernOfTravels.demo.WebSocket.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.TavernOfTravels.demo.WebSocket.chat.entity.Message;
import ru.TavernOfTravels.demo.room.repository.RoomRepository;
import ru.TavernOfTravels.demo.user.model.UserDTO;
import ru.TavernOfTravels.demo.user.repository.UserRepository;

import javax.sound.midi.Soundbank;


@Controller
@CrossOrigin("http://localhost:3000")
public class ChatMessageHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<WebSocketSession, UUID> sessionRoomMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<Message>> roomMessages = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<UserDTO>> roomUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<String>> usersInRoom = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<WebSocketSession>> activeUserConnections = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

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

    private boolean existsByNick(UUID roomId, String email) {
        List<String> users = usersInRoom.getOrDefault(roomId, new ArrayList<>());
        return users.contains(email);
    }


    private void saveMessage(UUID roomId, Message message) {
        roomMessages.computeIfAbsent(roomId, key -> new ArrayList<>()).add(message);
    }

    private void sendMessageToAllInRoom(UUID roomId, Message message) {
        messagingTemplate.convertAndSend("/topic/messages/" + roomId, message);
    }

    @MessageMapping("/users")
    public void handleShowUsers(@Payload UserDTO userPayLoad, SimpMessageHeaderAccessor headerAccessor) {
        String nickname = userPayLoad.getNickname();
        String email = userPayLoad.getEmail();
        UUID roomId = userPayLoad.getRoomId();

        if (userRepository.existsByEmail(email) && !existsByNick(roomId, email)) {
            UserDTO user = new UserDTO(nickname, email, roomId);
            saveUser(roomId, user);
            sendUserInRoom(roomId, user, headerAccessor);
        } else if (roomUsers.get(roomId).stream().anyMatch(u -> u.getEmail().equals(email))) {
            UserDTO user = new UserDTO(nickname, email, roomId);
            sendUserInRoom(roomId, user, headerAccessor);
        }
    }
    private void saveUser(UUID roomId, UserDTO user) {
        if (!roomUsers.containsKey(roomId) || !roomUsers.get(roomId).stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            roomUsers.computeIfAbsent(roomId, key -> new ArrayList<>()).add(user);
        }

        if (!usersInRoom.containsKey(roomId) || !usersInRoom.get(roomId).contains(user.getEmail())) {
            usersInRoom.computeIfAbsent(roomId, key -> new ArrayList<>()).add(user.getEmail());
        }
    }
    private void sendUserInRoom(UUID roomId, UserDTO user, SimpMessageHeaderAccessor headerAccessor) {
        List<UserDTO> users = roomUsers.getOrDefault(roomId, Collections.emptyList());
        List<String> nicknames = users.stream()
                .map(UserDTO::getNickname)
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend("/topic/users/" + roomId, nicknames, headerAccessor.getMessageHeaders());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UUID roomId = UUID.fromString((String) session.getAttributes().get("roomId"));
        sessionRoomMap.put(session, roomId);

        // Add user to active connections
        activeUserConnections
                .computeIfAbsent(roomId.toString(), key -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UUID roomId = sessionRoomMap.remove(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = activeUserConnections.get(roomId.toString());
            if (sessions != null) {
                sessions.remove(session);

                // Remove user from usersInRoom if no active connections left
                if (sessions.isEmpty()) {
                    roomUsers.get(roomId).removeIf(user -> user.getEmail().equals((String) session.getAttributes().get("userEmail")));
                    usersInRoom.get(roomId).remove(session.getAttributes().get("userEmail"));
                }
            }
        }
    }

}