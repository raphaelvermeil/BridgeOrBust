package com.example.bridgeorbust.physicsSimulation;

public class Beam {
    public Pin pin1, pin2;
    private double restLength;
    private double stiffness;
    private double mass;
    private double massPerLength;
    private double breakLimit = 2500;
    private boolean broken = false;
    private boolean physical = false;
    private double maxLength = 250;
    private double airFrictionCoefficient = 3;
    private double redColorCoefficient;
    private double blueColorCoefficient;

    public Beam() {

    }

    public Beam(Pin p1, Pin p2, double stiffness, double massPerLength, boolean physical) {
        this.pin1 = p1;
        this.pin2 = p2;
        this.restLength = p1.getPosition().subtract(p2.getPosition()).magnitude();
        this.stiffness = stiffness;
        this.massPerLength = massPerLength;
        createMass();
        this.physical = physical;

        p1.addBeam(this);
        p2.addBeam(this);
    }

    public void setPhysical(boolean physical) {
        this.physical = physical;
    }

    public double getMaxLength() {
        return maxLength;
    }

    public boolean isPhysical() {
        return physical;
    }

    public void addForceAndMassIfConnected(Pin pin, Vector2D forceSum, double massSum) {
        if (this.isBroken() == false) {
            if (pin == pin1 || pin == pin2) {
                pin.setForceSum(forceSum.add(getForceAtPin(pin)));
                pin.setMassSum(massSum + mass / 2.0);
            }
        }
    }

    public Vector2D getForceAtPin(Pin pin) {
        if (this.isBroken() == false) {
            if (pin1 == null || pin2 == null) return new Vector2D();

            Vector2D currentLength = pin2.getPosition().subtract(pin1.getPosition());
            Vector2D displacement = currentLength.normalize().multiply(currentLength.magnitude() - restLength);

            Vector2D forceBeam = displacement.multiply(-stiffness);
            Vector2D forceGravity = new Vector2D(0, mass * 70);
            Vector2D forceAirFriction = pin.getVelocity().multiply(-airFrictionCoefficient);
            System.out.println();
            if (currentLength.magnitude() > restLength) {
                this.redColorCoefficient = (forceBeam.magnitude() / breakLimit) * 255;

            } else if (currentLength.magnitude() < restLength) {
                this.blueColorCoefficient = (forceBeam.magnitude() / breakLimit) * 255;
            }

            if (forceBeam.magnitude() > this.breakLimit || forceBeam.magnitude() < -this.breakLimit) { // Arbitrary break limit
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

    public void beamSizeBinding(double previousWidth, double previousHeight, double newWidth, double newHeight){
        this.restLength = this.pin1.getPosition().subtract(this.pin2.getPosition()).magnitude();;
        this.maxLength = maxLength * newWidth / previousWidth;
        this.massPerLength = massPerLength * newHeight / previousHeight;
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
        this.mass = this.restLength * this.massPerLength;
    }

    public double getRestLength() {
        return restLength;
    }

    public void setRestLength() {
        this.restLength = this.pin1.getPosition().subtract(this.pin2.getPosition()).magnitude();;
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
}

