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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BridgeSimulation extends Application {
    private List<Pin> pins = new ArrayList<>();
    private List<Beam> beams = new ArrayList<>();
    private List<Pin> startPins = new ArrayList<>();
    private Pin firstPin = null;
    private double cursorX = 0;
    private double cursorY = 0;
    private boolean play = false;
    private boolean roadMode = false;
    private boolean lost=false;
    public Ball ball1;
    private Beam previousBeam = null;
    private int mouseCounter = 0;
    private double lostArbitraryLimit;
    private double winArbitraryLimit;
    private double previousWindowWidth;
    private double previousWindowHeight;
    private double maxLength = 250;
    //this is refresh test

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas));

        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);


        ball1 = new Ball(50, 100, 10, 20);
//        ball2 = new Ball(80, 100, 10, 20);
        Pane pane = new Pane();


        // Bind the canvas size to the pane size
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

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
        controls.setLayoutY(100);

        ImageView resetImage = new ImageView(new Image("file:restart-icon.png"));
        Button resetButton = new Button("", resetImage);
        resetImage.setFitWidth(30);
        resetImage.setFitHeight(30);
        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth() - 20);
        resetButton.setLayoutY(20);

        ImageView undoImage = new ImageView(new Image("file:undo-icon.png"));
        undoImage.setFitWidth(30);
        undoImage.setFitHeight(30);
        Button undoButton = new Button("", undoImage);

        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth());
        undoButton.setLayoutY(canvas.getHeight() - undoButton.getHeight() - 80);

        ImageView gearImage = new ImageView(new Image("file:settings-icon.png"));
        Button gearButton = new Button("", gearImage);
        gearImage.setFitWidth(30);
        gearImage.setFitHeight(30);
        gearButton.setLayoutX(canvas.getWidth() - gearButton.getWidth() - 20);
        gearButton.setLayoutY(canvas.getHeight() - gearButton.getHeight() - 20);

        pane.getChildren().addAll(canvas, playPause, controls, resetButton, undoButton, gearButton);

        Scene scene = new Scene(pane, 1000, 600);
        setupBridge(gc);

        stage.setMinWidth(400);
        stage.setMinHeight(300);

        playPause.setOnMouseClicked(e -> {
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                for (Beam beam : beams) {
                    beam.pin1.resetToInit();
                    beam.pin2.resetToInit();
                }
                ball1.setPosition(new Vector2D(0, 0));
                ball1.setOldPosition(new Vector2D(0, 0));
                this.lost=false;
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
            ball1.setPosition(new Vector2D(0, 0));
            ball1.setOldPosition(new Vector2D(0, 0));
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                play = false;
            }
            firstPin = null;
            mouseCounter = 0;
        });
        undoButton.setOnAction(e -> {

            if(firstPin == null){
                Beam beam = beams.get(beams.size() - 1);
                if (mouseCounter > 0) {
                    destroyBeam(beam);

                    if (beam.pin1.getConnectedBeamsSize() < 1) {
                        pins.remove(beam.pin1);
                    } else if (beam.pin2.getConnectedBeamsSize() < 1) {
                        pins.remove(beam.pin2);
                    }
                }
            } else {
                firstPin = null;
            }

            for (Pin pin : pins) {
                if(pin.getConnectedBeamsSize() == 0 && !pin.isStartPin()){
                    pins.remove(pin);
                }
            }

        });

        new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                if (play&&!lost) {
                    updateSimulation(deltaTime);
                }

                render(gc);
            }

        }.start();
        previousWindowWidth = canvas.getWidth();
        previousWindowHeight = canvas.getHeight();
        updateOnResize(canvas, playPause, controls, resetButton, undoButton, gearButton);

        // Add listeners to update button positions when the canvas size changes

            canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton, gearButton));
            canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton, gearButton));

//        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton));
//        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton));

        stage.setScene(scene);
        stage.setTitle("Bridge Simulation");
        stage.show();
    }


    private void updateOnResize(Canvas canvas, ImageView playPause, HBox controls, Button resetButton, Button undoButton, Button gearButton) {
        playPause.setX(50);
        playPause.setY(20);
        for (Pin pin:startPins){
            pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
        }
        for (Pin pin : pins) {
            if (pin.isStartPin() == false) {
                pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
            }
        }
        for(Beam beam: beams){
            beam.beamSizeBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight());

        }
        this.maxLength = maxLength * canvas.getWidth() / previousWindowWidth;

        controls.setLayoutX(50);
        controls.setLayoutY(canvas.getHeight() - 50);

        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth()-225);
        resetButton.setLayoutY(30);

        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth() - 150);
        undoButton.setLayoutY(30);

        gearButton.setLayoutX(canvas.getWidth() - gearButton.getWidth() - 75);
        gearButton.setLayoutY(30);

        this.winArbitraryLimit*= canvas.getWidth()/this.previousWindowWidth;
        this.lostArbitraryLimit*= canvas.getHeight()/this.previousWindowHeight;

        previousWindowWidth = canvas.getWidth();
        previousWindowHeight = canvas.getHeight();
    }

    private void setupBridge(GraphicsContext gc) {
        if(startPins.isEmpty())
            level1();
        List<Pin> newPins = new ArrayList<Pin>();
        pins.addAll(startPins);
        for (Pin pin : pins) {
            if (pin.isPositionFixed()) {
                Pin pin2 = null;
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    pin2 = new Pin(0, pin.getPosition().y, true);
                    beams.add(new Beam(pin2, pin, 1000, 0.0001, true));
                    gc.setFill(Color.GREEN);
                } else {
                    pin2 = new Pin(gc.getCanvas().getWidth(), pin.getPosition().y, true);
                    beams.add(new Beam(pin, pin2, 1000, 0.0001, true));

                }
                newPins.add(pin2);//newPins.removeAll(null);
            }
        }
        pins.addAll(newPins);
    }

    private void level1() {//fix numeration!
        Pin p1 = new Pin(200, 300, true, true);
        Pin p4 = new Pin(800, 300, true, true);
        Pin p5 = new Pin(200, 400, true, true);
        Pin p6 = new Pin(800, 400, true, true);

        this.lostArbitraryLimit=500;
        this.winArbitraryLimit=900;

        startPins.add(p1);
        startPins.add(p4);
        startPins.add(p5);
        startPins.add(p6);
    }

    private void level2() {//fix numeration!
        Pin p1 = new Pin(150, 300, true);
        Pin p2 = new Pin(850, 300, true);
        Pin p3 = new Pin(400, 550, true);

        this.lostArbitraryLimit=670;

        pins.add(p1);
        pins.add(p2);
        pins.add(p3);
//        car(100);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        Pin clickedPin = getPinAt(x, y);

        if (y < 80 || play) {
            System.out.println("Cannot build.");
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
            if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= this.maxLength) {
                Pin secondPin = (clickedPin != null ? clickedPin : new Pin(x, y, false));
                if (clickedPin == null) {
                    pins.add(secondPin);
                }

                Beam beam = new Beam(firstPin, secondPin, 800, 0.018, roadMode);
                beams.add(beam);
                previousBeam = beam;
                firstPin = null;
                mouseCounter++;

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
    private void checkWin(){
        if(ball1.getPosition().y>lostArbitraryLimit){
            this.lost=true;
            System.out.println("you lost");
        }
        if (ball1.getPosition().x>winArbitraryLimit){
            win();
        }
    }
    private void win(){
        System.out.println("You win");
    }
    private void updateSimulation(double deltaTime) {
        for (Pin pin : pins) {
            pin.calculateForces();
            System.out.println(pin.getConnectedBeamsSize());
            if(pin.getConnectedBeamsSize() == 0 && !pin.isStartPin()){
                pins.remove(pin);
            }
        }
        for (Pin pin : pins) {
            pin.update(deltaTime);
        }
        for (Beam beam : beams) {
            if (beam.isPhysical()) {
                ball1.checkCollisions(beam);
            }

            if (beam.isBroken()) {
                destroyBeam(beam);
            }
        }
        ball1.accelerate(0, 9.8);
        ball1.update(deltaTime);
        checkWin();
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

            gc.setStroke(Color.rgb((int) beam.getRedColorCoefficient(), 0, (int) beam.getblueColorCoefficient()));
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
            double ratio = this.maxLength / magnitude;
            Vector2D pos2 = ratio >= 1 ? new Vector2D(cursorX, cursorY) : new Vector2D(pos1.x + deltaX * ratio, pos1.y + deltaY * ratio);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

//        gc.setFill(Color.GRAY);
//        gc.fillOval(car.pin1.getPosition().x - 15, car.pin1.getPosition().y - 15, 30, 30);
//        gc.fillOval(car.pin2.getPosition().x - 15, car.pin2.getPosition().y - 15, 30, 30);

        gc.setFill(Color.RED);
        gc.fillOval(ball1.getPosition().x, ball1.getPosition().y, ball1.getRadius(), ball1.getRadius());
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        for (double x = 0; x < gc.getCanvas().getWidth(); x += (15 + 10)) {
            gc.strokeLine(x, this.lostArbitraryLimit, x + 15, this.lostArbitraryLimit);
        }

        if(lost){
            String text="LOST";
            gc.setFont( Font.font("Comic Sans MS", FontWeight.BOLD, 50)); // Change font size as needed
            gc.setFill(Color.DARKRED);
            double textWidth = gc.getFont().getSize() * text.length() * 0.4; // Approximate width
            double textHeight = gc.getFont().getSize(); // Font size is a good estimate for height

            double x = (gc.getCanvas().getWidth() - textWidth) / 2;
            double y = (gc.getCanvas().getHeight() - textHeight) / 2 + textHeight; // Adjust for baseline alignment

            gc.fillText(text, x, y);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


