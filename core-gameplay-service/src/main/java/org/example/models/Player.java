package org.example.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Player {
    private final String id;
    private Position position;
    private float rotation;
}
