package com.example.bridgeorbust.physicsSimulation;

import java.util.ArrayList;
import java.util.List;

public class Pin {
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D forceSum;
    private double massSum;
    private boolean positionFixed;
    private List<Beam> connectedBeams;

    public Pin(double x, double y, boolean fixed) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D();
        this.forceSum = new Vector2D();
        this.massSum = 0.0;
        this.positionFixed = fixed;
        this.connectedBeams = new ArrayList<>();
    }

    public void calculateForces() {
        forceSum = new Vector2D();
        massSum = 0.0;
        for (Beam beam : connectedBeams) {
            beam.addForceAndMassIfConnected(this, forceSum, massSum);
        }
    }

    public void update(double deltaTime) {
        if (!positionFixed && massSum > 0.0) {
            Vector2D acceleration = forceSum.multiply(1.0 / massSum);
            velocity = velocity.add(acceleration.multiply(deltaTime));
            position = position.add(velocity.multiply(deltaTime));
        }
    }

    public void addBeam(Beam beam) {
        connectedBeams.add(beam);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getForceSum() {
        return forceSum;
    }

    public void setForceSum(Vector2D forceSum) {
        this.forceSum = forceSum;
    }

    public double getMassSum() {
        return massSum;
    }

    public void setMassSum(double massSum) {
        this.massSum = massSum;
    }

    public boolean isPositionFixed() {
        return positionFixed;
    }
}
