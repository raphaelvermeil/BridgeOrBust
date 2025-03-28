package com.example.bridgeorbust.physicsSimulation;

import java.util.ArrayList;
import java.util.List;

public class Pin {
    private Vector2D position;
    private Vector2D initialPosition;
    private Vector2D velocity;
    private Vector2D forceSum;
    private double radius = 20;
    private double massSum;
    private boolean positionFixed;
    private List<Beam> connectedBeams;
    private boolean isStartPin;
    private boolean isClicked=false;

    public Pin(double x, double y, boolean fixed) {
        this.position = new Vector2D(x, y);
        this.initialPosition= new Vector2D(x,y);
        this.velocity = new Vector2D();
        this.forceSum = new Vector2D();
        this.massSum = 0.0;
        this.positionFixed = fixed;
        this.connectedBeams = new ArrayList<>();
        this.isStartPin=false;
    }
    public Pin(double x, double y, boolean fixed,boolean isStartPin) {
        this.position = new Vector2D(x, y);
        this.initialPosition= new Vector2D(x,y);
        this.velocity = new Vector2D();
        this.forceSum = new Vector2D();
        this.massSum = 0.0;
        this.positionFixed = fixed;
        this.connectedBeams = new ArrayList<>();
        this.isStartPin=isStartPin;
    }
    public boolean isStartPin(){
        return isStartPin;
    }
    public void resetToInit(){
        this.position=this.initialPosition;
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

    public void positionBinding(double previousWidth, double previousHeight, double newWidth, double newHeight, boolean play){
        double x = this.position.x * newWidth / previousWidth;
        double y = this.position.y * newHeight / previousHeight;
        position = new Vector2D(x, y);

        double xInit=this.initialPosition.x * newWidth / previousWidth;
        double yInit=this.initialPosition.y * newHeight / previousHeight;
        initialPosition = new Vector2D(xInit, yInit);
//        if (!play)
//            initialPosition=position;
    }

    public void setX(double x){
        this.position=new Vector2D(x, position.y);

    }
    public void setY(double y){
        this.position=new Vector2D(position.x, y);

    }

    public void addBeam(Beam beam) {
        connectedBeams.add(beam);
    }

    public void removeBeam(Beam beam) {
        connectedBeams.remove(beam);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
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

    public Vector2D getVelocity() {
        return velocity;
    }

    public int getConnectedBeamsSize() {
        return connectedBeams.size();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
