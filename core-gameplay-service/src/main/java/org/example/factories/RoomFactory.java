package org.example.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.configuration.PlayerProperties;
import org.example.configuration.RoomProperties;
import org.example.models.Player;
import org.example.models.Room;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
public class RoomFactory implements IFactory<Room, ObjectMapper>{
    private final RoomProperties roomProperties;

    @Override
    public Room create(String id, ObjectMapper jsonMapper) {
        var firstSpawnPoint = new float[]{roomProperties.getFirst().getX(), roomProperties.getFirst().getY()};
        var secondSpawnPoint = new float[]{roomProperties.getSecond().getX(), roomProperties.getSecond().getY()};
        return new Room(id, jsonMapper, firstSpawnPoint, secondSpawnPoint);
    }
}
