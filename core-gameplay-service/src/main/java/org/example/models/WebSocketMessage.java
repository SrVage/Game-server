package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    @JsonProperty("command")
    public String command;

    @JsonProperty("room_id")
    public String roomId;

    @JsonProperty("message")
    public String message;
}
