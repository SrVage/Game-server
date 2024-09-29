package org.example.models;

public class Vector {
    public static float[] createVector(float[] pointA, float[] pointB) {
        float[] vector = new float[pointA.length];
        for (int i = 0; i < pointA.length; i++) {
            vector[i] = pointB[i] - pointA[i];
        }
        return vector;
    }

    public static float[] createUnitVector(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees-90);
        float x = (float) Math.cos(angleRadians);
        float y = (float) Math.sin(angleRadians);
        return new float[]{x, y};
    }

    public static float dotProduct(float[] vectorA, float[] vectorB) {
        float result = 0.0f;
        for (int i = 0; i < vectorA.length; i++) {
            result += vectorA[i] * vectorB[i];
        }
        return result;
    }

    public static float[] normalizeVector(float[] vector) {
        float magnitude = 0.0f;
        for (float v : vector) {
            magnitude += v * v;
        }
        magnitude = (float) Math.sqrt(magnitude);

        float[] normalizedVector = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / magnitude;
        }
        return normalizedVector;
    }

    public static float calculateDistance(float[] vector) {
        float sum = 0.0f;
        for (float v : vector) {
            sum += v * v;
        }
        return (float) Math.sqrt(sum);
    }
}
