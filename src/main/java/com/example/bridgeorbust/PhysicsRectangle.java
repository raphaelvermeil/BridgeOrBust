package com.example.bridgeorbust;



import javafx.scene.shape.Rectangle;

public class PhysicsRectangle extends Rectangle {
    private double position_current_x;
    private double position_current_y;
    private double position_old_x;
    private double position_old_y;
    private double acceleration_x;
    private double acceleration_y;
    private double mass = 1;
    private double pivot_x;
    private double pivot_y;

    public PhysicsRectangle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.position_current_x = x;
        this.position_current_y = y;
        this.position_old_x = x;
        this.position_old_y = y;
        this.acceleration_x = 0;
        this.acceleration_y = 0;
        this.pivot_x = pivot_x;
        this.pivot_y = pivot_y;

    }

    public void updatePosition(double dt) {
        double velocity_x = this.getX() - this.getPosition_old_x();
        double velocity_y = this.getY() - this.getPosition_old_y();

        this.setPosition_old_x(this.getX());
        this.setPosition_old_y(this.getY());

        this.setX(this.getX() + velocity_x + this.getAcceleration_x() * dt * dt);
        this.setY(this.getY() + velocity_y + this.getAcceleration_y() * dt * dt);

        this.setAcceleration_x(0);
        this.setAcceleration_y(0);

//        if(this.pivot_x != 0 && this.pivot_y != 0){
//
//        }



    }

    void accelerate(double ax, double ay) {
        this.setAcceleration_x(this.getAcceleration_x() + ax);
        this.setAcceleration_y(this.getAcceleration_y() + ay);
    }

    public void rotateAroundPivot(double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        // Translate rectangle to origin (pivot point)
        double translatedX = this.getX() - pivot_x;
        double translatedY = this.getY() - pivot_y;

        // Apply rotation
        double rotatedX = translatedX * cos - translatedY * sin;
        double rotatedY = translatedX * sin + translatedY * cos;

        // Translate back to original position
        this.setX(rotatedX + pivot_x);
        this.setY(rotatedY + pivot_y);
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

    public double getPivot_y() {
        return pivot_y;
    }

    public void setPivot_y(double pivot_y) {
        this.pivot_y = pivot_y;
    }

    public double getPivot_x() {
        return pivot_x;
    }

    public void setPivot_x(double pivot_x) {
        this.pivot_x = pivot_x;
    }
}