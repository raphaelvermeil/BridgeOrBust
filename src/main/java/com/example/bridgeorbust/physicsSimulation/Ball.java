package com.example.bridgeorbust.physicsSimulation;



import java.util.ArrayList;
import java.util.List;

public class Ball {
    private Vector2D position;
    private Vector2D oldPosition;

    private Vector2D acceleration;
    private double mass;

//    private List<Beam> beams;
    private double radius;

    public Ball(double x, double y, double mass,  double radius) {
        this.position = new Vector2D(x, y);

        this.oldPosition = new Vector2D(x, y);
        this.mass = 0.0;
        this.acceleration = new Vector2D();
        this.radius = radius;

    }

    public void update(double dt) {

        double velocity_x = this.position.x - this.oldPosition.x;
        double velocity_y = this.position.y - this.oldPosition.y;


        this.oldPosition.x = (this.position.x);
        this.oldPosition.y = (this.position.y);

        // Verlet integration equation

        this.position.x = (this.position.x + velocity_x + this.acceleration.x * dt);
        this.position.y = (this.position.y + velocity_y + this.acceleration.y * dt);

        // Reset acceleration
        this.acceleration.x = 0;

        this.acceleration.y = 0;
        System.out.println(this.position.x + " " + this.position.y);
    }

    void accelerate(double ax, double ay) {
        this.acceleration.x = (this.acceleration.x + ax);

        this.acceleration.y = (this.acceleration.y + ay);
    }

    void checkCollisions(Beam beam) {
        Beam overBeam;
        double deltaX,deltaY,relativePosition,ratio;
        if (beam.isPhysical()) {
            if ((this.position.x > beam.pin1.getPosition().x && this.position.x < beam.pin2.getPosition().x)//if wheel 1 is between two pins of a beam
                    | (this.position.x <= beam.pin1.getPosition().x && this.position.x >= beam.pin2.getPosition().x)) { //Same as above but in reverse order
                if (this.position.y > beam.pin1.getPosition().y - radius | this.position.y > beam.pin2.getPosition().y - radius) {
                        overBeam = beam;
                        deltaX = overBeam.pin1.getPosition().x - overBeam.pin2.getPosition().x;
                        deltaY = overBeam.pin1.getPosition().y - overBeam.pin2.getPosition().y;
                        relativePosition = overBeam.pin1.getPosition().x - this.position.x;
                        ratio = relativePosition / deltaX;
                        this.position.y = overBeam.pin1.getPosition().y + (deltaY * ratio) - radius;
                        System.out.println("1. r=" + ratio + " dY*r=" + deltaY * ratio);
//                        under = true;
                }
            }
        }
    }

//    public void setX(double x){
//        this.position=new Vector2D(x, position.y);
//
//    }
//    public void setY(double y){
//        this.position=new Vector2D(position.x, y);
//
//    }

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
}
