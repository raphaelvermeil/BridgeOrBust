package com.example.bridgeorbust.physicsSimulation;


import java.util.ArrayList;
import java.util.List;

public class Ball {
    private Vector2D position;
    private Vector2D oldPosition;
    private Beam currentBeam;

    private Vector2D acceleration;
    private double mass;
    private double yLimit;

    //    private List<Beam> beams;
    private double radius;

    public Ball(double x, double y, double mass, double radius) {
        this.position = new Vector2D(x, y);

        this.oldPosition = new Vector2D(x, y);
        this.mass = mass;
        this.acceleration = new Vector2D();
        this.radius = radius;

    }

    public void update(double dt, List<Beam> beams) {
        this.acceleration = new Vector2D(0, 0);
        this.accelerate(0, 9.8);

        double velocity_x = 2;
        double velocity_y = this.position.y - this.oldPosition.y;


        this.oldPosition.x = (this.position.x);
        this.oldPosition.y = (this.position.y);

        // Verlet integration equation

        this.position.x = (this.position.x + velocity_x + this.acceleration.x * dt);
        this.position.y = (this.position.y + velocity_y + this.acceleration.y * dt);
    }

    private void accelerate(double ax, double ay) {
        this.acceleration.x = (this.acceleration.x + ax);
        this.acceleration.y = (this.acceleration.y + ay);
    }

    public boolean checkCollisions(Beam beam) {
        if ((this.position.x > beam.pin1.getPosition().x && this.position.x < beam.pin2.getPosition().x)
                || (this.position.x <= beam.pin1.getPosition().x && this.position.x >= beam.pin2.getPosition().x)) {

            if (this.position.y >= beam.pin1.getPosition().y - this.radius || this.position.y >= beam.pin2.getPosition().y - (this.radius)) {

                if (this.position.y >= beam.pin1.getPosition().y - (2 * this.radius) || this.position.y >= beam.pin2.getPosition().y - (2 * this.radius)) {

                    if (trulyUnder(beam)) {
                        currentBeam = beam;
                        return true;
                    }

                }
            }
        }
        return false;
    }


    private boolean trulyUnder(Beam beam) {
        double deltaX, deltaY, relativeLength, relativeHeight, yLimit;
        Pin leadingPinX, followingPinX;
        if (beam.pin1.getPosition().x >= beam.pin2.getPosition().x) {
            leadingPinX = beam.pin2;
            followingPinX = beam.pin1;
        } else {
            leadingPinX = beam.pin1;
            followingPinX = beam.pin2;
        }

        deltaX = followingPinX.getPosition().x - leadingPinX.getPosition().x;
        deltaY = followingPinX.getPosition().y - leadingPinX.getPosition().y;
        relativeLength = this.position.x + this.radius - leadingPinX.getPosition().x;
        relativeHeight = deltaY * relativeLength / deltaX;
        yLimit = leadingPinX.getPosition().y + relativeHeight - this.radius;
        if (this.position.y > yLimit) {
            if (deltaY <= 0) {
                if (this.oldPosition.y + this.radius <= leadingPinX.getPosition().y + 10) {//+10 is buffer
                    this.yLimit=yLimit;
                    return true;
                }
            } else {
                if (this.oldPosition.y <= yLimit + 10) {
                    this.yLimit=yLimit;
                    return true;
                }
            }
        }
        return false;
    }

    public void replace(Beam beam) {
        if (this.checkCollisions(beam)) {
            this.position.y = this.yLimit;
            this.oldPosition.y = this.yLimit;
        }

    }

    public void positionBinding(double previousWidth, double previousHeight, double newWidth, double newHeight) {
        double x = this.position.x * newWidth / previousWidth;
        double y = this.position.y * newHeight / previousHeight;
        position = new Vector2D(x, y);

        double oldX = this.oldPosition.x * newWidth / previousWidth;
        double oldY = this.oldPosition.y * newHeight / previousHeight;
        oldPosition = new Vector2D(oldX, oldY);

//        double change = Math.max(newHeight / previousHeight, newWidth / previousWidth) - 1;
////        double relativeChange=(change>1)?change-1:1-change;
//        this.radius *= 1 + (change * 0.1);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2D getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Vector2D oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Beam getCurrentBeam() {
        return currentBeam;
    }

    public void setCurrentBeam(Beam currentBeam) {
        this.currentBeam = currentBeam;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
