package com.example.bridgeorbust;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class PhysicsCircle extends Circle {
    private double position_current_x;    // Current X Position
    private double position_current_y;    // Current Y Position
    private double position_old_x;        // Old X Position
    private double position_old_y;        // Old Y Position
    private double acceleration_x;        // X Acceleration
    private double acceleration_y;        // Y Acceleration
    private double radius;                // Size
    private double mass = 1;              // Default mass
    private double dragOffsetX;
    private double dragOffsetY;



    public PhysicsCircle(double x, double y, double radius) {
        super(x, y, radius);
        this.position_current_x = x;
        this.position_current_y = y;
        this.position_old_x = x;
        this.position_old_y = y;
        this.acceleration_x = 0;
        this.acceleration_y = 0;
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
    }

    public PhysicsCircle(double radius){
        super(radius);
    }
    private void onMousePressed(MouseEvent event) {
        dragOffsetX = getCenterX() - event.getSceneX();
        dragOffsetY = getCenterY() - event.getSceneY();
    }

    private void onMouseDragged(MouseEvent event) {
        setCenterX(event.getSceneX() + dragOffsetX);
        setCenterY(event.getSceneY() + dragOffsetY);
    }

    public void updatePosition(double dt) {
        double velocity_x = this.getCenterX() - this.getPosition_old_x();
        double velocity_y = this.getCenterY() - this.getPosition_old_y();


        this.setPosition_old_x(this.getCenterX());
        this.setPosition_old_y(this.getCenterY());

        // Verlet integration equation

        this.setCenterX(this.getCenterX() + velocity_x + this.getAcceleration_x() * dt);
        this.setCenterY(this.getCenterY() + velocity_y + this.getAcceleration_y() * dt);

        // Reset acceleration
        this.setAcceleration_x(0);

        this.setAcceleration_y(0);

    }

    void accelerate(double ax, double ay) {

        this.setAcceleration_x(this.getAcceleration_x() + ax);

        this.setAcceleration_y(this.getAcceleration_y() + ay);
    }

    void testMove(){
        this.setCenterX(this.getCenterX() + 1);
    }

    public double getAcceleration_x() {
        return acceleration_x;
    }

    public void setAcceleration_x(double acceleration_x) {
        this.acceleration_x = acceleration_x;
    }

    public double getAcceleration_y() {
        return acceleration_y;
    }

    public void setAcceleration_y(double acceleration_y) {
        this.acceleration_y = acceleration_y;
    }

    public double getPosition_old_x() {
        return position_old_x;
    }

    public void setPosition_old_x(double position_old_x) {
        this.position_old_x = position_old_x;
    }

    public double getPosition_old_y() {
        return position_old_y;
    }

    public void setPosition_old_y(double position_old_y) {
        this.position_old_y = position_old_y;
    }
}