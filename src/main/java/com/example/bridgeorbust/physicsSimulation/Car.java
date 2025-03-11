package com.example.bridgeorbust.physicsSimulation;

public class Car extends Beam{

    public Car() {
    }

    public Car(Pin p1, Pin p2, double stiffness, double mass) {
        super(p1, p2, stiffness, mass);
    }

    public Car(Pin p1, Pin p2, double stiffness, double mass, boolean physical) {
        super(p1, p2, stiffness, mass, physical);
    }

}
