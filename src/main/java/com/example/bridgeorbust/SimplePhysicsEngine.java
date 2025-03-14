//package com.example.bridgeorbust;
//
//import javafx.animation.AnimationTimer;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.transform.Rotate;
//import javafx.stage.Stage;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SimplePhysicsEngine extends Application {
//
//    private final double WIDTH = 800;
//    private final double HEIGHT = 600;
//    Rectangle rect = new Rectangle(400, 400, 100, 30);
//    double angle = Math.toRadians(45);
//    double angleAcc = 0;
//    double angleVel = 0;
//
//    @Override
//    public void start(Stage stage) {
//        Pane pane = new Pane();
//        PhysicsCircle rig = new PhysicsCircle(250, 400, 20);
//        PhysicsCircle rig2 = new PhysicsCircle(350, 400, 20);
//        PhysicsCircle rig3 = new PhysicsCircle(350, 400, 20);
//        PhysicsCircle rig4 = new PhysicsCircle(450, 400, 20);
//        PhysicsCircle rig5 = new PhysicsCircle(550, 400, 20);
//        PhysicsCircle rig6 = new PhysicsCircle(650, 400, 20);
//
//        PhysicsRectangle rectangle = new PhysicsRectangle(400, 400, 100, 30);
//
//
//
//        double pivotX = 500;
//        double pivotY = 430;
//        Rotate rotate = new Rotate(-45, pivotX, pivotY); // Initial rotation at 0 degrees
//        rect.getTransforms().add(rotate);
//        Line line = new Line(400, 400, rig.getCenterX(), rig.getCenterY());
//        PhysicsRod rod = new PhysicsRod(rig, rig2, 100);
//        PhysicsRod rod2 = new PhysicsRod(rig2, rig4, 100);
//        PhysicsRod rod3 = new PhysicsRod(rig4, rig5, 100);
//        PhysicsRod rod4 = new PhysicsRod(rig5, rig6, 100);
//
//        Circle constraints = new Circle(400, 400, 400);
//        constraints.setFill(Color.BLACK);
//        rig.setFill(Color.WHITE);
//        rig2.setFill(Color.WHITE);
//        rig3.setFill(Color.WHITE);
//        rig4.setFill(Color.WHITE);
//        rig5.setFill(Color.WHITE);
//        rig6.setFill(Color.WHITE);
//        rectangle.setFill(Color.WHITE);
//        rect.setFill(Color.WHITE);
//        line.setStroke(Color.WHITE);
//        line.setStrokeWidth(30);
//
//
//
//
//
//        pane.getChildren().addAll(constraints, rig, rig2, rod.getLine(), rod2.getLine(), rod3.getLine(), rig4, rig5, rig6, rod4.getLine());
//        Scene scene = new Scene(pane, 800, 800);
//
//
//
//
//
//
//        AnimationTimer timer = new AnimationTimer() {
//            private long lastTime = 0;
//            @Override
//            public void handle(long now) {
//                if (lastTime == 0) {
//                    lastTime = now; // Initialize the first frame time
//                    return;
//                }
//
//                double dt = (now - lastTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
//                lastTime = now;
//
//
//
//
//
//
//                int substeps = 8;
//                double substep_dt = dt / substeps;
//                for (int i = 0; i < substeps; i++) {
//                    applyGravity(0, 9.8, List.of(rig, rig2, rig4, rig5));
//                    updatePositions(substep_dt, List.of(rig, rig2, rig4, rig5));
//                    applyConstraints(List.of(rig, rig2), constraints);
//                    solveCollisions(List.of(rig, rig2));
//                    applyGravityRectangle(0, 1000, rectangle);
//                    updateRectangle(substep_dt, rectangle);
////                    rectangle.setRotate(rectangle.getRotate() + 1);
//                    rectangle.rotateAroundPivot(0.01);
//                    rod.enforceConstraint();
//                    rod2.enforceConstraint();
//                    rod3.enforceConstraint();
//                    rod4.enforceConstraint();
//
//                    rig.setCenterX(250);
//                    rig.setCenterY(400);
//                    rig6.setCenterX(650);
//                    rig6.setCenterY(400);
//
//
//
//
//
//                }
//
//
//
//
//                // Update the physics
////                update(circle, dt);
//            }
//        };
//        timer.start();
//
//
//
//        stage.setScene(scene);
//        stage.setTitle("Simple Physics Engine");
//        stage.show();
//    }
//
//    double velocityTest = 0;
//
//
//
//
//    void updatePositions(double dt, List<PhysicsCircle> objects) {
//        for (PhysicsCircle object : objects) {
//            object.updatePosition(dt);
//        }
//    }
//
//    void updateRectangle(double dt, PhysicsRectangle rectangle) {
//        rectangle.updatePosition(dt);
//    }
//
//    void applyGravity(double ax, double ay, List<PhysicsCircle> objects) {
//        for (PhysicsCircle object : objects) {
//            object.accelerate(ax, ay);
//        }
//    }
//
//    void applyGravityRectangle(double ax, double ay, PhysicsRectangle rectangle) {
//        rectangle.accelerate(ax, ay);
//    }
//
//    void applyConstraints(List<PhysicsCircle> objects, Circle constraints) {
//        Vector2D position = new Vector2D(400, 400);
//        double radius = 200;
//
//        for (PhysicsCircle obj : objects) {
//            Vector2D objPosition = new Vector2D(obj.getCenterX(), obj.getCenterY());
//            Vector2D toObj = objPosition.subtract(position);
//            double dist = toObj.magnitude();
//
//            if (dist > constraints.getRadius() - obj.getRadius()) {
//                Vector2D n = toObj.normalize();
//                Vector2D newPosition = position.add(n.scale(constraints.getRadius() - obj.getRadius()));
//                obj.setCenterX(newPosition.getX());
//                obj.setCenterY(newPosition.getY());
//            }
//        }
//    }
//
//    void solveCollisions(List<PhysicsCircle> objects) {
//        for (int i = 0; i < objects.size(); i++) {
//            PhysicsCircle obj1 = objects.get(i);
//            for (int j = i + 1; j < objects.size(); j++) {
//                PhysicsCircle obj2 = objects.get(j);
//                Vector2D toObj = new Vector2D(obj2.getCenterX() - obj1.getCenterX(), obj2.getCenterY() - obj1.getCenterY());
//                double dist = toObj.magnitude();
//                double overlap = obj1.getRadius() + obj2.getRadius() - dist;
//                if (overlap > 0) {
//                    Vector2D n = toObj.normalize();
//                    Vector2D correction = n.scale(overlap / 2);
//                    obj1.setCenterX(obj1.getCenterX() - correction.getX());
//                    obj1.setCenterY(obj1.getCenterY() - correction.getY());
//                    obj2.setCenterX(obj2.getCenterX() + correction.getX());
//                    obj2.setCenterY(obj2.getCenterY() + correction.getY());
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//
//
//
//
//
//
