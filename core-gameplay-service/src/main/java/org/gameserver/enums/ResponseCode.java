package org.gameserver.enums;

public enum ResponseCode {
    OK("Success"),
    ROOM_NOT_FOUND("Room not found");

    private final String message;

    ResponseCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
