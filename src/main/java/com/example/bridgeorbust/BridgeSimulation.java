package com.example.bridgeorbust;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BridgeSimulation extends Application {
    private List<Pin> pins = new ArrayList<>();
    private List<Beam> beams = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas));

        setupBridge();

        new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                updateSimulation(deltaTime);
                render(gc);
            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Bridge Simulation");
        stage.show();
    }

    private void setupBridge() {
        Pin p1 = new Pin(200, 300, true);
        Pin p2 = new Pin(400, 300, false);
        Pin p3 = new Pin(600, 300, false);
        Pin p4 = new Pin(800, 300, true);
        Pin p5 = new Pin(200, 400, true);
        Pin p6 = new Pin(800, 400, true);
//        Pin p5 = new Pin(400, 150, false);
//        Pin p6 = new Pin(600, 150, false);
//        Pin p7 = new Pin(400, 350, false);
//        Pin p8 = new Pin(400, 400, false);

        Beam b1 = new Beam(p1, p2, 300, 10);
        Beam b2 = new Beam(p2, p3, 300, 10);
        Beam b3 = new Beam(p3, p4, 300, 10);
        Beam b4 = new Beam(p2, p5, 300, 10);
        Beam b5 = new Beam(p3, p6, 300, 10);

//        Beam b4 = new Beam(p1, p5, 300, 10);
//        Beam b5 = new Beam(p2, p5, 300, 10);
//        Beam b6 = new Beam(p4, p6, 300, 10);
//        Beam b7 = new Beam(p3, p6, 300, 10);
//        Beam b8 = new Beam(p5, p6, 300, 10);
//        Beam b9 = new Beam(p2, p7, 300, 10);
//        Beam b10 = new Beam(p7, p8, 300, 10);


        pins.add(p1);
        pins.add(p2);
        pins.add(p3);
        pins.add(p4);
        pins.add(p5);
        pins.add(p6);




        beams.add(b1);
        beams.add(b2);
        beams.add(b3);
        beams.add(b4);
        beams.add(b5);
//        beams.add(b6);
//        beams.add(b7);
//        beams.add(b8);
//        beams.add(b9);
//        beams.add(b10);

    }

    private void updateSimulation(double deltaTime) {
        for (Pin pin : pins) {
            pin.calculateForces();
        }
        for (Pin pin : pins) {
            pin.update(deltaTime);
        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 600);

        gc.setStroke(Color.BLACK);
        for (Beam beam : beams) {
            Vector2D pos1 = beam.pin1.getPosition();
            Vector2D pos2 = beam.pin2.getPosition();
            gc.setStroke(beam.isBroken() ? Color.RED : Color.BLACK);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

        gc.setFill(Color.BLUE);
        for (Pin pin : pins) {
            Vector2D pos = pin.getPosition();
            gc.fillOval(pos.x - 5, pos.y - 5, 10, 10);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
