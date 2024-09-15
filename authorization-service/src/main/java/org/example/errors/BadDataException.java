package org.example.errors;

public class BadDataException extends RuntimeException {
    public BadDataException(String message) {
        super(message);
    }
}
