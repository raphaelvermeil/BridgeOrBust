package com.example.bridgeorbust.physicsSimulation;

public class Beam {
    public Pin pin1, pin2;
    private double restLength;
    private double stiffness;
    private double mass;
    private boolean broken = false;
    private boolean physical=false;
    private double maxLength=250;
    private double airFrictionCoefficient = 3;
    private double redColorCoefficient;
    private double greenColorCoefficient;
    public Beam(){

    }
    public Beam(Pin p1, Pin p2, double stiffness, double mass) {
        this.pin1 = p1;
        this.pin2 = p2;
        this.restLength = p1.getPosition().subtract(p2.getPosition()).magnitude();
        this.stiffness = stiffness;
        this.mass = mass;

        p1.addBeam(this);
        p2.addBeam(this);
    }
    public Beam(Pin p1, Pin p2, double stiffness, double mass,boolean physical) {
        this.pin1 = p1;
        this.pin2 = p2;
        this.restLength = p1.getPosition().subtract(p2.getPosition()).magnitude();
        this.stiffness = stiffness;
        this.mass = mass;
        this.physical=physical;

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
        if(this.isBroken() == false) {
            if (pin == pin1 || pin == pin2) {
                pin.setForceSum(forceSum.add(getForceAtPin(pin)));
                pin.setMassSum(massSum + mass / 2.0);
            }
        }
    }

    public Vector2D getForceAtPin(Pin pin) {
        if(this.isBroken() == false) {
            if (pin1 == null || pin2 == null) return new Vector2D();

            Vector2D currentLength = pin2.getPosition().subtract(pin1.getPosition());
            Vector2D displacement = currentLength.normalize().multiply(currentLength.magnitude() - restLength);

            Vector2D forceBeam = displacement.multiply(-stiffness);
            Vector2D forceGravity = new Vector2D(0, mass * 60);
            Vector2D forceAirFriction = pin.getVelocity().multiply(-airFrictionCoefficient);
            System.out.println(forceBeam.magnitude());
            if(forceBeam.magnitude() > 0) {
                this.redColorCoefficient = (forceBeam.magnitude() / 5000) * 255;

            } else if(forceBeam.magnitude() < 0) {
                this.greenColorCoefficient = (forceBeam.magnitude() / -5000) * 255;
            }

            
            
//        System.out.println(redColorCoefficient);
//        System.out.println(forceBeam.magnitude());
            if (forceBeam.magnitude() > 3800 || forceBeam.magnitude() < -3800) { // Arbitrary break limit
                broken = true;
            }
//        } else if(forceBeam.magnitude() < 2000 || forceBeam.magnitude() > -2000){
//            broken = false;
//        }

//        Vector2D totalForce = forceBeam.add(forceAirFriction);

//        return pin == pin1 ? forceBeam.multiply(-1).add(forceGravity.multiply(0.5)) : forceBeam.add(forceGravity.multiply(0.5));
            if (pin == pin1) {
                return forceBeam.multiply(-1).add(forceGravity.multiply(0.5)).add(forceAirFriction);
            } else if (pin == pin2) {
                return forceBeam.add(forceGravity.multiply(0.5)).add(forceAirFriction);
            } else {
                return new Vector2D();
            }
//        if (pin == pin1) {
//            return totalForce.multiply(-1).add(forceGravity.multiply(0.5));
//        } else if (pin == pin2) {
//            return totalForce.add(forceGravity.multiply(0.5));
//        } else {
//            return new Vector2D();
//        }
        }
        return new Vector2D();
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

    public double getGreenColorCoefficient() {
        return greenColorCoefficient;
    }

    public void setGreenColorCoefficient(double greenColorCoefficient) {
        this.greenColorCoefficient = greenColorCoefficient;
    }
}

