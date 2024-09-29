package org.example.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@Data
@ConfigurationProperties("app.player")
public class PlayerProperties {
    private int initialHealth;
    private int damage;
    private int speed;
}
