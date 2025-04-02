package com.example.bridgeorbust.physicsSimulation;


import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BridgeSimulation extends Application {
    private MediaPlayer mediaPlayer;
    private List<Pin> pins = new ArrayList<>();
    private List<Beam> beams = new ArrayList<>();
    private List<Pin> startPins = new ArrayList<>();
    private Pin firstPin = null;
    private double cursorX = 0;
    private int maxRoadBeam;
    private int maxTruss;
    private double cursorY = 0;
    private boolean play = false;
    private CheckBox gridModeButton = new CheckBox("Grid Mode");
    private double gridSizeX = 20;
    private double gridSizeY = 20;
    private boolean roadMode = false;
    private boolean lost = false;
    public Ball ball1;
    private int mouseCounter = 0;
    private double lostArbitraryLimit;
    private double winArbitraryLimit;
    private double previousWindowWidth;
    private double previousWindowHeight;
    private double maxLength = 250;
    private VBox winWidget = new VBox();
    public int level = 1;
    
    // Load the audio file
    AudioClip beamSound = new AudioClip(getClass().getResource("/sounds/pop.mp3").toString());

    @Override
    public void start(Stage stage) {

        Media media = new Media(getClass().getResource("/sounds/bridgingasmile.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();


        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas));

        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);


        ball1 = new Ball(50, 100, 10, 20);
//        ball2 = new Ball(80, 100, 10, 20);
        Pane pane = new Pane();

        gridModeButton.setSelected(true);
        // Bind the canvas size to the pane size
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        ImageView playPause = new ImageView(new Image("file:play.png"));
        Button playPauseButton = new Button("", playPause);
        playPause.setFitWidth(30);  // Resize if needed
        playPause.setFitHeight(30);
        playPauseButton.setLayoutX(30);
        playPauseButton.setLayoutY(20);
        playPauseButton.getStyleClass().add("transparent-button");

        HBox controls = new HBox();
        controls.setSpacing(10);
        RadioButton roadButton = new RadioButton();
        RadioButton trussButton = new RadioButton();

        ImageView roadImage = new ImageView(new Image("file:road-perspective.png"));
        ImageView trussImage = new ImageView(new Image("file:beam.png"));
        roadImage.setFitWidth(30);
        roadImage.setFitHeight(30);
        trussImage.setFitWidth(30);
        trussImage.setFitHeight(30);
        roadButton.setGraphic(roadImage);
        trussButton.setGraphic(trussImage);
//        roadButton.getStyleClass().add("radio-button");
//        trussButton.getStyleClass().add("radio-button");
        roadButton.getStyleClass().remove("radio-button");
        roadButton.getStyleClass().add("toggle-button");
        trussButton.getStyleClass().remove("radio-button");
        trussButton.getStyleClass().add("toggle-button");

        ToggleGroup buttons = new ToggleGroup();
        roadButton.setToggleGroup(buttons);
        trussButton.setToggleGroup(buttons);
        buttons.selectToggle(trussButton);
        controls.getChildren().addAll(roadButton, trussButton);
        controls.setLayoutX((canvas.getWidth() - controls.getWidth()) / 2);
        controls.setLayoutY(canvas.getHeight() - controls.getHeight() - 50);


        ImageView resetImage = new ImageView(new Image("file:arrow.png"));
        Button resetButton = new Button("", resetImage);
        resetImage.setFitWidth(30);
        resetImage.setFitHeight(30);
        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth() - 20);
        resetButton.setLayoutY(20);
        resetButton.getStyleClass().add("transparent-button");

        ImageView undoImage = new ImageView(new Image("file:jump.png"));
        undoImage.setFitWidth(30);
        undoImage.setFitHeight(30);
        Button undoButton = new Button("", undoImage);
        undoButton.getStyleClass().add("transparent-button");


        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth());
        undoButton.setLayoutY(canvas.getHeight() - undoButton.getHeight() - 80);

        ImageView gearImage = new ImageView(new Image("file:sign-out.png"));
        Button gearButton = new Button("", gearImage);
        gearImage.setFitWidth(30);
        gearImage.setFitHeight(30);
        gearButton.setLayoutX(canvas.getWidth() - gearButton.getWidth() - 20);
        gearButton.setLayoutY(canvas.getHeight() - gearButton.getHeight() - 20);
        gearButton.getStyleClass().add("transparent-button");


        winWidget.setLayoutX(canvas.getWidth() / 2);
        winWidget.setLayoutY(canvas.getHeight() / 2);

        winWidget.getStyleClass().add("win-widget");
        winWidget.setDisable(true);


        pane.getChildren().addAll(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget);

        Scene scene = new Scene(pane, 1000, 600);
        setupBridge(gc);

        stage.setMinWidth(400);
        stage.setMinHeight(300);

        playPauseButton.setOnAction(e -> {
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                for (Beam beam : beams) {
                    beam.pin1.resetToInit();
                    beam.pin2.resetToInit();
                }
                ball1.setPosition(new Vector2D(0, 0));
                ball1.setOldPosition(new Vector2D(0, 0));
                this.lost = false;
                play = false;
                firstPin = null;
                gridModeButton.setSelected(true);

            } else {
                playPause.setImage(new Image("file:pause.png"));
                play = true;
                gridModeButton.setSelected(false);
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
                lost = false;
            }
            firstPin = null;
            mouseCounter = 0;
        });
        undoButton.setOnAction(e -> {

            if (firstPin == null) {
                Beam beam = beams.get(beams.size() - 1);
                if (mouseCounter > 0) {
                    destroyBeam(beam);
                    System.out.println(mediaPlayer.getCurrentTime());
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
                if (pin.getConnectedBeamsSize() == 0 && !pin.isStartPin()) {
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

                if (play && !lost) {
                    updateSimulation(deltaTime);
                }

                render(gc);
            }

        }.start();
        previousWindowWidth = canvas.getWidth();
        previousWindowHeight = canvas.getHeight();
        updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget);

        // Add listeners to update button positions when the canvas size changes

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget));
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget));

//        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton));
//        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPause, controls, resetButton, undoButton));


        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.Z),
                () -> undoButton.fire()
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.R),
                () -> resetButton.fire()
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.SPACE),
                () -> playPauseButton.fire()
        );

        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Bridge Simulation");
        stage.show();
    }


    private void updateOnResize(Canvas canvas, Button playPauseButton, HBox controls, Button resetButton, Button undoButton, Button gearButton, VBox winWidget) {
        playPauseButton.setLayoutX(30);
        playPauseButton.setLayoutY(20);
        for (Pin pin : startPins) {
            pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
        }
        for (Pin pin : pins) {
            if (pin.isStartPin() == false) {
                pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
            }
        }
        for (Beam beam : beams) {
            beam.beamSizeBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight());

        }
        ball1.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight());
        this.maxLength = maxLength * canvas.getWidth() / previousWindowWidth;

        controls.setLayoutX((canvas.getWidth() - controls.getWidth()) / 2);
        controls.setLayoutY(canvas.getHeight() - controls.getHeight() - 50);

        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth() - 225);
        resetButton.setLayoutY(30);

        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth() - 150);
        undoButton.setLayoutY(30);

        gearButton.setLayoutX(canvas.getWidth() - gearButton.getWidth() - 75);
        gearButton.setLayoutY(30);

        winWidget.setLayoutX(canvas.getWidth() / 2);
        winWidget.setLayoutY(canvas.getHeight() / 2);



        winWidget.setMinWidth(canvas.getWidth() * 400 / 1000);
        winWidget.setMinHeight(canvas.getHeight() * 400 / 600);

        winWidget.setLayoutX(canvas.getWidth() / 2 - winWidget.getMinWidth() / 2);
        winWidget.setLayoutY(canvas.getHeight() / 2 - winWidget.getMinHeight() / 2);

        this.winArbitraryLimit *= canvas.getWidth() / this.previousWindowWidth;
        this.lostArbitraryLimit *= canvas.getHeight() / this.previousWindowHeight;

        this.gridSizeX *= canvas.getWidth() / this.previousWindowWidth;
        this.gridSizeY *= canvas.getHeight() / this.previousWindowHeight;

        previousWindowWidth = canvas.getWidth();
        previousWindowHeight = canvas.getHeight();
    }

    private void setupBridge(GraphicsContext gc) {
        if (startPins.isEmpty()) {
            switch (level) {
                case 1:
                    level1();
                    break;
                case 2:
                    level2();
                    break;
                default:
                    System.out.println("there is no such level");
                    break;
            }
        }
//            level1();
        List<Pin> newPins = new ArrayList<Pin>();
        pins.addAll(startPins);
        for (Pin pin : pins) {
            if (pin.isPositionFixed()) {
                Pin pin2 = null;
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    pin2 = new Pin(-200, pin.getPosition().y, true);
                    beams.add(new Beam(pin2, pin, 1000, 0.0001, true));
                    gc.setFill(Color.GREEN);
                } else {
                    pin2 = new Pin(gc.getCanvas().getWidth() + 200, pin.getPosition().y, true);
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

        this.lostArbitraryLimit = 550;
        this.winArbitraryLimit = 900;

        this.maxRoadBeam = 4;
        this.maxTruss = 30;

        startPins.add(p1);
        startPins.add(p4);
        startPins.add(p5);
        startPins.add(p6);
    }

    private void level2() {//fix numeration!
        Pin p1 = new Pin(150, 300, true);
        Pin p2 = new Pin(850, 300, true);
        Pin p3 = new Pin(400, 550, true);

        this.lostArbitraryLimit = 670;

        startPins.add(p1);
        startPins.add(p2);
        startPins.add(p3);
//        car(100);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if (gridModeButton.isSelected()) {
            x = (x % gridSizeX > gridSizeX / 2) ? x - x % gridSizeX + gridSizeX : x - x % gridSizeX;
            y = (y % gridSizeY > gridSizeY / 2) ? y - y % gridSizeY + gridSizeY : y - y % gridSizeY;
        }

        Pin clickedPin = getPinAt(x, y);

        if (y < 80 || play) {
            System.out.println("Cannot build.");
        } else if (firstPin == null) {
            if (clickedPin != null) {
                firstPin = clickedPin;
                beamSound.play();
            } else {
                firstPin = new Pin(x, y, false);
                pins.add(firstPin);
                beamSound.play();
            }
        } else {
            double deltaX = x - firstPin.getPosition().x;
            double deltaY = y - firstPin.getPosition().y;
            if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= this.maxLength) {
                Pin secondPin = (clickedPin != null ? clickedPin : new Pin(x, y, false));
                if (clickedPin == null) {
                    pins.add(secondPin);
                }

                Beam beam = new Beam(firstPin, secondPin, 900, 0.025, roadMode);

                beamSound.play();
                beams.add(beam);
                firstPin = null;
                mouseCounter++;

                if (beam.isPhysical()) {
                    this.maxRoadBeam--;
                } else {
                    this.maxTruss--;
                }

            }
        }
    }

    private void handleMouseMove(MouseEvent event) {
        cursorX = event.getX();
        cursorY = event.getY();

        if (gridModeButton.isSelected()) {
            cursorX = (cursorX % gridSizeX > gridSizeX / 2) ? cursorX- cursorX % gridSizeX + gridSizeX : cursorX - cursorX % gridSizeX;
            cursorY = (cursorY % gridSizeY > gridSizeY / 2) ? cursorY - cursorY % gridSizeY + gridSizeY : cursorY - cursorY % gridSizeY;
        }

        for(Pin pin : pins){
            Vector2D pos = pin.getPosition();
            if (pos.subtract(new Vector2D(cursorX, cursorY)).magnitude() < 20) {
                pin.setClicked(true);
            } else {
                pin.setClicked(false);
            }
        }
    }

    private Pin getPinAt(double x, double y) {
        for (Pin pin : pins) {
            Vector2D pos = pin.getPosition();
            if (pos.subtract(new Vector2D(x, y)).magnitude() < 20) {
//                pin.setClicked(true);

                return pin;
            }
        }
        return null;
    }


    private void checkWin() {
        if (ball1.getPosition().y > lostArbitraryLimit) {
            this.lost = true;
            System.out.println("you lost");
        }
        if (ball1.getPosition().x > winArbitraryLimit) {
            win();
        }
    }

    private void win() {
        winWidget.setDisable(false);
    }

    private void updateSimulation(double deltaTime) {
        for (Pin pin : pins) {
            pin.calculateForces();
            //System.out.println(pin.getConnectedBeamsSize());
            if (pin.getConnectedBeamsSize() == 0 && !pin.isStartPin()) {
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
        if (beam.isPhysical())
            maxRoadBeam++;
        else
            maxTruss++;
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setStroke(Color.DARKGREY);
        for (Beam beam : beams) {
            Vector2D pos1 = beam.pin1.getPosition();
            Vector2D pos2 = beam.pin2.getPosition();

            if (beam.isPhysical()) {
                gc.setStroke(Color.rgb((int) beam.getRedColorCoefficient(), 0, (int) beam.getblueColorCoefficient()));
                gc.setLineWidth(16);
                gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(2);
                gc.setLineDashes(10);

                gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
                gc.setLineDashes(0);
            } else {
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(10);
//            gc.setStroke(Color.BLACK);

                gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);


//                gc.setStroke(Color.rgb((int) beam.getRedColorCoefficient(), 0, (int) beam.getblueColorCoefficient()));
                gc.setStroke(Color.rgb(115 + (int) beam.getRedColorCoefficient(), 115, 115 + (int) beam.getblueColorCoefficient()));

                gc.setLineWidth(4);
//            gc.setStroke(Color.BLACK);

                gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
            }


        }

        if (firstPin != null) {
            gc.setStroke(Color.DARKGREY);
            Vector2D pos1 = firstPin.getPosition();
            double x = cursorX;
            double y = cursorY;
            if (gridModeButton.isSelected()) {
                x = (x % gridSizeX > gridSizeX / 2) ? x - x % gridSizeX + gridSizeX : x - x % gridSizeX;
                y = (y % gridSizeY > gridSizeY / 2) ? y - y % gridSizeY + gridSizeY : y - y % gridSizeY;
            }
            double deltaX = x - pos1.x;
            double deltaY = y - pos1.y;
            double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double ratio = this.maxLength / magnitude;
            Vector2D pos2 = ratio >= 1 ? new Vector2D(x, y) : new Vector2D(pos1.x + deltaX * ratio, pos1.y + deltaY * ratio);
            gc.strokeLine(pos1.x, pos1.y, pos2.x, pos2.y);
        }

        for (Pin pin : pins) {

            if (pin.isPositionFixed()) {
                gc.setFill(Color.ROSYBROWN);
                gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    gc.fillRoundRect(0, pin.getPosition().y + 8, pin.getPosition().x, gc.getCanvas().getHeight() - pin.getPosition().y, 10, 10); // (x, y, width, height)
                } else {
                    gc.fillRect(pin.getPosition().x, pin.getPosition().y + 8, gc.getCanvas().getWidth() - pin.getPosition().x, gc.getCanvas().getHeight() - pin.getPosition().y); // (x, y, width, height)
                }
            }

            double R = 20.0;
            double r = 16.0;

            Vector2D pos = pin.getPosition();
            double centerX = pos.x - 10;
            double centerY = pos.y - 10;


            if (pin.isClicked()) {
                R = 25;
                r = 21;
            } else {
                R = 20;
                r = 16;
            }

            double offsetR = (R - 20) / 2;
            double offsetr = (r - 16) / 2;

            if (pin.isPositionFixed()) {
                gc.setFill(Color.BLACK);
                gc.fillRect(centerX - offsetR, centerY - offsetR, R, R);
                gc.setFill(Color.GREY);
                gc.fillRect(centerX - offsetr + 2, centerY - offsetr + 2, r, r);
            } else {
                gc.setFill(Color.BLACK);
                gc.fillOval(centerX - offsetR, centerY - offsetR, R, R);
                gc.setFill(Color.GREY);
                gc.fillOval(centerX - offsetr + 2, centerY - offsetr + 2, r, r);
            }




        }

        gc.setFill(Color.RED);
        gc.fillOval(ball1.getPosition().x, ball1.getPosition().y, ball1.getRadius(), ball1.getRadius());


        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.setLineDashes(5);
        gc.strokeLine(0, this.lostArbitraryLimit, gc.getCanvas().getWidth(), this.lostArbitraryLimit);
//        for (double x = 0; x < gc.getCanvas().getWidth(); x += (15 + 10)) {
//            gc.strokeLine(x, this.lostArbitraryLimit, x + 15, this.lostArbitraryLimit);
//        }

        if (lost) {
            String text = "LOST";
            gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 50)); // Change font size as needed
            gc.setFill(Color.DARKRED);
            double textWidth = gc.getFont().getSize() * text.length() * 0.4; // Approximate width
            double textHeight = gc.getFont().getSize(); // Font size is a good estimate for height

            double x = (gc.getCanvas().getWidth() - textWidth) / 2;
            double y = (gc.getCanvas().getHeight() - textHeight) / 2 + textHeight; // Adjust for baseline alignment

            gc.fillText(text, x, y);
        } else if (!play) {
            String text = "Roads Left: " + maxRoadBeam + " Trusses Left: " + maxTruss;
            gc.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20)); // Change font size as needed
            gc.setFill(Color.ROSYBROWN);
            double textWidth = gc.getFont().getSize() * text.length() * 0.4; // Approximate width
            double textHeight = gc.getFont().getSize(); // Font size is a good estimate for height

            double x = (gc.getCanvas().getWidth() - textWidth) / 2;
            double y = 30; // Adjust for baseline alignment

            gc.fillText(text, x, y);

        }
        //Drawing grid
        if (gridModeButton.isSelected()) {
            gc.setStroke(Color.rgb(100, 100, 100, 0.5));
            gc.setLineWidth(0.5);
            for (double x = 0; x < gc.getCanvas().getWidth(); x += gridSizeX) {
                gc.strokeLine(x, 0, x, gc.getCanvas().getHeight());
            }
            for (double y = 0; y < gc.getCanvas().getHeight(); y += gridSizeY) {
                gc.strokeLine(0, y, gc.getCanvas().getWidth(), y);
            }
        }


    }

    public static void main(String[] args) {
        launch();
    }
}


