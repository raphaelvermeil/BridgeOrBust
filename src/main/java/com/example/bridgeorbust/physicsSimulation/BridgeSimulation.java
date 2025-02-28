package com.example.bridgeorbust.physicsSimulation;
//wow this is such a great project

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BridgeSimulation extends Application {
    private List<Pin> pins = new ArrayList<>();
    private List<Beam> beams = new ArrayList<>();
    private Pin firstPin = null;
    private double stiffness = 4000;
    private double cursorX = 0;
    private double cursorY = 0;
    boolean play = false;
    boolean roadMode = false;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(1000, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas));

        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);

        Pane pane = new Pane();
        pane.getChildren().add(canvas);

        ImageView playPause = new ImageView(new Image("file:play.png"));
        playPause.setX(50);
        playPause.setY(20);
        playPause.setFitWidth(30);  // Resize if needed
        playPause.setFitHeight(30);

        HBox controls = new HBox();
        controls.setSpacing(10);
        RadioButton roadButton = new RadioButton("Road");
        RadioButton trussButton = new RadioButton("Truss");
        ToggleGroup buttons = new ToggleGroup();
        roadButton.setToggleGroup(buttons);
        trussButton.setToggleGroup(buttons);
        buttons.selectToggle(trussButton);
        controls.getChildren().addAll(roadButton, trussButton);
        controls.setLayoutX(canvas.getWidth() - 120);
        controls.setLayoutY(10);

        Button resetButton = new Button("Reset");
        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth() - 70);
        resetButton.setLayoutY(canvas.getHeight() - resetButton.getHeight() -50);

//        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
//            resetButton.setLayoutX(newVal.doubleValue() - resetButton.getWidth() - 10);
//        });
//
//        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
//            resetButton.setLayoutY(newVal.doubleValue() - resetButton.getHeight() - 10);
//        });



        pane.getChildren().addAll(playPause, controls, resetButton);

        Scene scene = new Scene(pane);
        setupBridge(gc);

        playPause.setOnMouseClicked(e -> {
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                play = false;
            } else {
                playPause.setImage(new Image("file:pause.png"));
                play = true;
            }
        });
        trussButton.setOnAction(e -> roadMode = false);
        roadButton.setOnAction(e -> roadMode = true);
        resetButton.setOnAction(e -> {
            pins.clear();
            beams.clear();
            setupBridge(gc);
        });

        new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;
                if (play) {
                    updateSimulation(deltaTime);
                }

                render(gc);
            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Bridge Simulation");
        stage.show();
    }

    private void setupBridge(GraphicsContext gc) {
        level1();

        for (Pin pin : pins) {
            if (pin.isPositionFixed()) {
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    beams.add(new Beam(new Pin(0, pin.getPosition().y, true), pin, 1000, 0.0001, true));
                    gc.setFill(Color.GREEN);
                    //gc.fillRect(5, pin.getPosition().y, pin.getPosition().x, gc.getCanvas().getHeight()-pin.getPosition().y); // (x, y, width, height)
                } else {
                    beams.add(new Beam(pin, new Pin(gc.getCanvas().getWidth(), pin.getPosition().y, true), 1000, 0.0001, true));
                }

            }
        }

//        Beam b1 = new Beam(p1, p2, 300, 10,true);
//        Beam b2 = new Beam(p2, p3, 300, 10,true);
//        Beam b3 = new Beam(p3, p4, 300, 10,true);
//        Beam b4 = new Beam(p2, p5, 300, 10);
//        Beam b5 = new Beam(p3, p6, 300, 10);
////        Beam b4 = new Beam(p1, p5, 300, 10);
////        Beam b5 = new Beam(p2, p5, 300, 10);
////        Beam b6 = new Beam(p4, p6, 300, 10);
////        Beam b7 = new Beam(p3, p6, 300, 10);
////        Beam b8 = new Beam(p5, p6, 300, 10);
////        Beam b9 = new Beam(p2, p7, 300, 10);
////        Beam b10 = new Beam(p7, p8, 300, 10);
//        beams.add(b1);
//        beams.add(b2);
//        beams.add(b3);
//        beams.add(b4);
//        beams.add(b5);
//        beams.add(b6);
//        beams.add(b7);
//        beams.add(b8);
//        beams.add(b9);
//        beams.add(b10);

    }

    private void level1() {//fix numeration!
        Pin p1 = new Pin(200, 300, true);
        Pin p4 = new Pin(800, 300, true);
        Pin p5 = new Pin(200, 400, true);
        Pin p6 = new Pin(800, 400, true);

        pins.add(p1);
        pins.add(p4);
        pins.add(p5);
        pins.add(p6);
    }

    private void level2() {//fix numeration!
        Pin p1 = new Pin(150, 300, true);
        Pin p2 = new Pin(850, 300, true);
        Pin p3 = new Pin(400, 550, true);

        pins.add(p1);
        pins.add(p2);
        pins.add(p3);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        Pin clickedPin = getPinAt(x, y);

        if (y < 100) {
        } else if (firstPin == null) {
            if (clickedPin != null) {
                firstPin = clickedPin;
            } else {
                firstPin = new Pin(x, y, false);
                pins.add(firstPin);
            }
        } else {
            double deltaX = x - firstPin.getPosition().x;
            double deltaY = y - firstPin.getPosition().y;
            if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= new Beam().getMaxLength()) {
                Pin secondPin = (clickedPin != null ? clickedPin : new Pin(x, y, false));
                if (clickedPin == null) {
                    pins.add(secondPin);
                }
                Beam beam = new Beam(firstPin, secondPin, 1200, 10, roadMode);
                beams.add(beam);
                firstPin = null;
            }
        }
    }

    private void handleMouseMove(MouseEvent event) {
        cursorX = event.getX();
        cursorY = event.getY();
    }

    private Pin getPinAt(double x, double y) {
        for (Pin pin : pins) {
            Vector2D pos = pin.getPosition();
            if (pos.subtract(new Vector2D(x, y)).magnitude() < 10) {
                return pin;
            }
        }
        return null;
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
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());


        gc.setStroke(Color.BLACK);
        for (Beam beam : beams) {
            Vector2D pos1 = beam.pin1.getPosition();
            Vector2D pos2 = beam.pin2.getPosition();
            gc.setStroke(beam.isPhysical() ? (beam.isBroken() ? Color.RED : Color.YELLOWGREEN) : (beam.isBroken() ? Color.RED : Color.BLACK));
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

        if (firstPin != null) {
            gc.setStroke(Color.BLACK);
            Vector2D pos1 = firstPin.getPosition();

            double deltaX = cursorX - pos1.x;
            double deltaY = cursorY - pos1.y;
            double magnitude=Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double ratio=new Beam().getMaxLength()/magnitude;
            Vector2D pos2 = ratio >= 1 ? new Vector2D(cursorX, cursorY):new Vector2D(pos1.x+deltaX*ratio, pos1.y+deltaY*ratio);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }


        for (Pin pin : pins) {
            if (pin.isPositionFixed()) {
                gc.setFill(Color.GREEN);
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    gc.fillRect(0, pin.getPosition().y, pin.getPosition().x, gc.getCanvas().getHeight() - pin.getPosition().y); // (x, y, width, height)
                } else {
                    gc.fillRect(pin.getPosition().x, pin.getPosition().y, gc.getCanvas().getWidth() - pin.getPosition().x, gc.getCanvas().getHeight() - pin.getPosition().y); // (x, y, width, height)
                }
            }
            gc.setFill(Color.BLUE);
            Vector2D pos = pin.getPosition();
            gc.fillOval(pos.x - 5, pos.y - 5, 10, 10);


        }
    }

    public static void main(String[] args) {
        launch();
    }
}


