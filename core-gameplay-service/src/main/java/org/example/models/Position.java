package org.example.models;

import lombok.Getter;

@Getter
public class Position {
    private float x;
    private float y;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
