package com.example.bridgeorbust.physicsSimulation;
public class Vector2D {
    public double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }


    public Vector2D multiply(Vector2D other) {
        return new Vector2D(this.x * other.x, this.y * other.y);
    }

    public Vector2D normalize() {
        double mag = magnitude();
        return mag > 0 ? new Vector2D(x / mag, y / mag) : new Vector2D();
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
}
