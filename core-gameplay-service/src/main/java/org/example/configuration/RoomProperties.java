package org.example.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@Data
@ConfigurationProperties("app.room")
public class RoomProperties {
    private SpawnPoint first;
    private SpawnPoint second;

    @NoArgsConstructor
    @Data
    public static class SpawnPoint {
        private int x;
        private int y;
    }
}
