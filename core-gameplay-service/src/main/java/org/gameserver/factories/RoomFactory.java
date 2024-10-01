package org.gameserver.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.gameserver.configuration.RoomProperties;
import org.gameserver.models.Room;

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
