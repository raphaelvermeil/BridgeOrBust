package com.example.bridgeorbust;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SimplePhysicsEngine extends Application {

    private final double WIDTH = 800;
    private final double HEIGHT = 600;


    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        PhysicsObject rig = new PhysicsObject(550, 400, 30);
        PhysicsObject rig2 = new PhysicsObject(250, 400, 20);
        Circle constraints = new Circle(400, 400, 200);
        constraints.setFill(Color.BLACK);
        rig.setFill(Color.WHITE);
        rig2.setFill(Color.WHITE);

        Circle circle = new Circle(150, 100, 30);
        pane.getChildren().addAll(circle, constraints, rig, rig2);
        Scene scene = new Scene(pane, 800, 800);





        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now; // Initialize the first frame time
                    return;
                }

                double dt = (now - lastTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
                lastTime = now;


                applyGravity(0, 1000, List.of(rig, rig2));
                updatePositions(dt, List.of(rig, rig2));
                applyConstraints(List.of(rig, rig2));

                // Update the physics
//                update(circle, dt);
            }
        };
        timer.start();



        stage.setScene(scene);
        stage.setTitle("Simple Physics Engine");
        stage.show();
    }

    double velocityTest = 0;
void update(Circle circle, double dt) {





    //THIS WORKS:
//    double gravity = 100;
//    double accelerationX = 0;
//    double accelerationY = gravity;
//
//    double prevX = circle.getCenterX();
//    double prevY = circle.getCenterY();
//    double velocityX = circle.getCenterX() - prevX;
//    double velocityY = circle.getCenterY() - prevY;
//    double prevVelocity = velocityTest;
//    velocityTest = prevVelocity + accelerationY * dt;
//
//
//    circle.setCenterY(circle.getCenterY() + velocityTest*dt + accelerationY * dt * dt);
//
//    accelerationY = 0;
//    System.out.println("VelocityTest: " + velocityTest);
}

//void updatePositions(double dt, List<PhysicsObject> objects) {
//    for (PhysicsObject object : objects) {
//        object.update;
//    }
//}

    void updatePositions(double dt, List<PhysicsObject> objects) {
        for (PhysicsObject object : objects) {
            object.updatePosition(dt);
        }
    }

    void applyGravity(double ax, double ay, List<PhysicsObject> objects) {
        for (PhysicsObject object : objects) {
            object.accelerate(ax, ay);
        }
    }

    void applyConstraints(List<PhysicsObject> objects){
        Vector2D position = new Vector2D(400, 400);
        double radius = 200;

        for (PhysicsObject obj : objects) {
            Vector2D objPosition = new Vector2D(obj.getCenterX(), obj.getCenterY());
            Vector2D toObj = objPosition.subtract(position);
            double dist = toObj.magnitude();

            if (dist > radius - obj.getRadius()) {
                Vector2D n = toObj.normalize();
                Vector2D newPosition = position.add(n.scale(200 - obj.getRadius()));
                obj.setCenterX(newPosition.getX());
                obj.setCenterY(newPosition.getY());
            }
        }
//        double constraintsPositionX = 400;
//        double constraintsPositionY = 400;
//
//        double constraintRadius = 200;
//        for (PhysicsObject object : objects) {
//            double distance = Math.sqrt(Math.pow(object.getCenterX() - constraintsPositionX, 2) + Math.pow(object.getCenterY() - constraintsPositionY, 2));
//            if (distance > constraintRadius - 15) {
//                 double nX = (object.getCenterX() - constraintsPositionX) / distance;
//                 double nY = (object.getCenterY() - constraintsPositionY) / distance;
//                 object.setCenterX(constraintsPositionX + nX * (distance-0.5));
//                 object.setCenterY(constraintsPositionY + nY * (distance-0.5));
//            }
//        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}






