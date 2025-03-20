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
    //    private List<Beam> physicalBeamsOverCar = new ArrayList<>();
    private Pin firstPin = null;
    private double cursorX = 0;
    private double cursorY = 0;
    boolean play = false;
    boolean roadMode = false;
    public Ball ball;
    private Beam previousBeam = null;
    private int mouseCounter = 0;
    //this is refresh test

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(1000, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas));

        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);


        ball = new Ball(215, 100, 10, 20);
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

        Button undoButton = new Button("Undo");
        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth() - 70);
        undoButton.setLayoutY(canvas.getHeight() - undoButton.getHeight() - 80);

//        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
//            resetButton.setLayoutX(newVal.doubleValue() - resetButton.getWidth() - 10);
//        });
//
//        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
//            resetButton.setLayoutY(newVal.doubleValue() - resetButton.getHeight() - 10);
//        });



        pane.getChildren().addAll(playPause, controls, resetButton, undoButton);

        Scene scene = new Scene(pane);
        setupBridge(gc);

        playPause.setOnMouseClicked(e -> {
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                for (Beam beam:beams){
                    beam.pin1.resetToInit();
                    beam.pin2.resetToInit();
                }
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
            ball.setPosition(new Vector2D(0, 0));
            ball.setOldPosition(new Vector2D(0, 0));
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                play = false;
            }
            firstPin = null;
        });
        undoButton.setOnAction(e -> {

            Beam beam = beams.get(beams.size() - 1);
            if(mouseCounter>0) {
                destroyBeam(beam);

                if (beam.pin1.getConnectedBeamsSize() < 1) {
                    pins.remove(beam.pin1);
                } else if (beam.pin2.getConnectedBeamsSize() < 1) {
                    pins.remove(beam.pin2);
                }
            }
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
                } else {
                    beams.add(new Beam(pin, new Pin(gc.getCanvas().getWidth(), pin.getPosition().y, true), 1000, 0.0001, true));
                }
            }
        }
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

//        car(100);
    }

    private void level2() {//fix numeration!
        Pin p1 = new Pin(150, 300, true);
        Pin p2 = new Pin(850, 300, true);
        Pin p3 = new Pin(400, 550, true);

        pins.add(p1);
        pins.add(p2);
        pins.add(p3);
//        car(100);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        Pin clickedPin = getPinAt(x, y);

        if (y < 80||play) {
            System.out.println("Cannot build.");
        }
        else if (firstPin == null) {
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

                Beam beam = new Beam(firstPin, secondPin, 800, 0.03, roadMode);
                beams.add(beam);
                previousBeam = beam;
                firstPin = null;
                mouseCounter++;

            }
        }
    }

//    private void car(double mass) {
//        this.car = new Car(new Pin(15, 0, false), new Pin(70, 0, false), 10000, mass);
//        pins.add(car.pin1);
//        pins.add(car.pin2);
//        //beams.add(car);
//    }

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
        for (Beam beam : beams) {
            if (beam.isPhysical()) {
                ball.checkCollisions(beam);
            }




            if (beam.isBroken()) {
                destroyBeam(beam);
            }
        }
        ball.accelerate(0, 9.8);
        ball.update(deltaTime);
    }

    private void destroyBeam(Beam beam) {
        beams.remove(beam);
        beam.pin1.removeBeam(beam);
        beam.pin2.removeBeam(beam);
        mouseCounter--;

    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

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
        gc.setStroke(Color.DARKGREY);
        for (Beam beam : beams) {
            Vector2D pos1 = beam.pin1.getPosition();
            Vector2D pos2 = beam.pin2.getPosition();

            gc.setStroke(Color.rgb((int) beam.getRedColorCoefficient(),0, (int)beam.getblueColorCoefficient()));
            gc.setLineWidth(4);
//            gc.setStroke(Color.BLACK);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

        if (firstPin != null) {
            gc.setStroke(Color.DARKGREY);
            Vector2D pos1 = firstPin.getPosition();

            double deltaX = cursorX - pos1.x;
            double deltaY = cursorY - pos1.y;
            double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double ratio = new Beam().getMaxLength() / magnitude;
            Vector2D pos2 = ratio >= 1 ? new Vector2D(cursorX, cursorY) : new Vector2D(pos1.x + deltaX * ratio, pos1.y + deltaY * ratio);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

//        gc.setFill(Color.GRAY);
//        gc.fillOval(car.pin1.getPosition().x - 15, car.pin1.getPosition().y - 15, 30, 30);
//        gc.fillOval(car.pin2.getPosition().x - 15, car.pin2.getPosition().y - 15, 30, 30);

        gc.setFill(Color.RED);
        gc.fillOval(ball.getPosition().x, ball.getPosition().y, ball.getRadius(), ball.getRadius());
    }

    public static void main(String[] args) {
        launch();
    }
}


