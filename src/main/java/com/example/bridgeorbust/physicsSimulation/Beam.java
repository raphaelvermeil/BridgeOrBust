package com.example.bridgeorbust.physicsSimulation;

import static com.example.bridgeorbust.physicsSimulation.BridgeSimulation.ball1;

public class Beam {
    public Pin pin1, pin2;
    private double restLength;
    private double stiffness;
    private double stiffnessX;
    private double stiffnessY;
    private double mass;
    private double massPerLength;
    private double massPerX;
    private double massPerY;
    private double breakLimit;
    private boolean broken = false;
    private boolean physical = false;
    private double gAccel;
    //private double maxLength = 250;
    private double airFrictionCoefficient = 3;
    private double redColorCoefficient;
    private double blueColorCoefficient;
    private Vector2D vectorObject;

    public Beam() {

    }

    public Beam(Pin p1, Pin p2, double[] stiffness, double[] massPerLength, double breakLimit, double gAccel, boolean physical) {
        this.pin1 = p1;
        this.pin2 = p2;
        this.vectorObject = p1.getPosition().subtract(p2.getPosition());
        this.restLength = vectorObject.magnitude();
//        this.stiffness = stiffness;
        this.stiffnessX = stiffness[0];
        this.stiffnessY = stiffness[1];
//        this.massPerLength = massPerLength;
        this.massPerX = massPerLength[0];
        this.massPerY = massPerLength[1];
        createMass();
        this.gAccel = gAccel;
        this.breakLimit = breakLimit;
        this.physical = physical;

        p1.addBeam(this);
        p2.addBeam(this);
    }

    public void setPhysical(boolean physical) {
        this.physical = physical;
    }

    public boolean isPhysical() {
        return physical;
    }

    public void addForceAndMassIfConnected(Pin pin, Vector2D forceSum, double massSum) {

        if (!this.isBroken()) {
            if (pin == pin1 || pin == pin2) {
                pin.setForceSum(forceSum.add(getForceAtPin(pin)));
                pin.setMassSum(massSum + mass / 2.0);
            }
        }
    }

    public Vector2D getForceAtPin(Pin pin) {
        if (!this.isBroken()) {
            if (pin1 == null || pin2 == null) return new Vector2D();

            Vector2D currentLength = pin2.getPosition().subtract(pin1.getPosition());
            Vector2D displacement = currentLength.normalize().multiply(currentLength.magnitude() - restLength);

            Vector2D forceBeam = new Vector2D(displacement.x * -stiffnessX, displacement.y * -stiffnessY);//displacement.multiply(-stiffness);
            Vector2D forceGravity = new Vector2D(0, mass * gAccel);
            Vector2D forceAirFriction = pin.getVelocity().multiply(-airFrictionCoefficient);
            if (currentLength.magnitude() > restLength) {
                this.redColorCoefficient = (forceBeam.magnitude() / breakLimit) * 140;

            } else if (currentLength.magnitude() < restLength) {
                this.blueColorCoefficient = (forceBeam.magnitude() / breakLimit) * 140;
            }

            if (forceBeam.magnitude() > this.breakLimit) { // Arbitrary break limit
                broken = true;
            }
            if (pin == pin1) {
                return forceBeam.multiply(-1).add(forceGravity.multiply(0.5)).add(forceAirFriction);
            } else if (pin == pin2) {
                return forceBeam.add(forceGravity.multiply(0.5)).add(forceAirFriction);
            } else {
                return new Vector2D();
            }
        }
        return new Vector2D();
    }

    public void beamSizeBinding(double previousWidth, double previousHeight, double newWidth, double newHeight, double[] stiffness, double[] massPerLength, double gAccel) {
        this.vectorObject = pin1.getPosition().subtract(pin2.getPosition());
        this.restLength = vectorObject.magnitude();
//        this.maxLength = maxLength * newWidth / previousWidth;
//        this.massPerLength = massPerLength * newHeight / previousHeight;
        this.stiffnessX = stiffness[0];
        this.stiffnessY = stiffness[1];
        this.massPerX = massPerLength[0];
        this.massPerY = massPerLength[1];
        this.gAccel = gAccel;
        createMass();
    }

    public boolean isBroken() {
        return broken;
    }

    public double getRedColorCoefficient() {
        return redColorCoefficient;
    }


    public void setRedColorCoefficient(double redColorCoefficient) {
        this.redColorCoefficient = redColorCoefficient;
    }

    public double getblueColorCoefficient() {
        return blueColorCoefficient;
    }

    public void setblueColorCoefficient(double blueColorCoefficient) {
        this.blueColorCoefficient = blueColorCoefficient;
    }

    public void createMass() {
//        this.mass = this.restLength * this.massPerLength;
//        System.out.println("1 " + this.mass);
        this.mass = Math.sqrt(massPerX * vectorObject.x * vectorObject.x + massPerY * vectorObject.y * vectorObject.y);
        System.out.println("2 " + this.mass);
    }

    public double getRestLength() {
        return restLength;
    }

    public void setRestLength() {
        this.restLength = this.pin1.getPosition().subtract(this.pin2.getPosition()).magnitude();
        ;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMassPerLength() {
        return massPerLength;
    }

    public void setMassPerLength(double massPerLength) {
        this.massPerLength = massPerLength;
    }

    public double getStiffnessX() {
        return stiffnessX;
    }

    public void setStiffnessX(double stiffnessX) {
        this.stiffnessX = stiffnessX;
    }

    public double getStiffnessY() {
        return stiffnessY;
    }

    public void setStiffnessY(double stiffnessY) {
        this.stiffnessY = stiffnessY;
    }
}

