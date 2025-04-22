package com.example.bridgeorbust.physicsSimulation;

/* problems:
 * !!BUG!! reset forces at pause, color should ideally stay, FIXED
 * change physic values per level? NO
 * calibrate realism FIxED
 * add weight effect to mass (hint: check only physical beams/use trulyUnder())
 * binding is spaghetti, bind width to height? (if so, ratio with full-screen size or smt)
 * \__} massPerLength, breakLimit, maxLength, x-Speed, y-accel, ect.
 * ---- max length in x and y, x
 * <p>
 * grid button while in build mode, (cannot be checked if pins != startPins and in freeMode)
 * level3
 * fix key comb. i.e. spacebar!! now p
 */

import com.example.GUI.GameTitleScreen;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BridgeSimulation extends Application {
    private MediaPlayer mediaPlayer;
    private List<Pin> pins = new ArrayList<>();
    private List<Beam> beams = new ArrayList<>();
    private List<Pin> startPins = new ArrayList<>();
    private Pin firstPin = null;
    private double cursorX = 0;
    private double breakLimitTruss = 4100;
    private double breakLimitRoad = 2700;
    private int maxRoadBeam;
    private int maxTruss;
    private double cursorY = 0;
    private boolean play = false;
    private CheckBox gridModeButton = new CheckBox("GRID");
    private double gridSizeX = 20;
    private double gridSizeY = 20;
    private boolean roadMode = false;
    private boolean lost = false;
    public static Ball ball1 = new Ball(50, 275, 0.02, 20);
    Image carImage;
    private int mouseCounter = 0;
    Button playPauseButton;
    private double lostArbitraryLimit;
    private double winArbitraryLimit;
    private double previousWindowWidth;
    private double previousWindowHeight;
    private double maxLength = 240;
    private double maxLengthX = 240;
    private double maxLengthY = 240;
    private double[] stifness = {900, 900};
    private double[] massPerLRoad = {Math.pow(0.033, 2), Math.pow(0.033, 2)};
    private double[] massPerLTruss = {Math.pow(0.025, 2), Math.pow(0.0253, 2)};
    private double gAccel = 70;
    private VBox winWidget = new VBox();
    public int level = 1;
    private double[][] level1 = {{200,300},{800, 300},{200, 400},{800, 400}};
    private double[][] level2 = {{150, 300},{850, 300},{400, 550}};
    private double[][] level3 = {{200, 400},{800, 200},{800, 400}};
    private int initalPinCount = 0;

    // Load the audio file
    AudioClip beamSound = new AudioClip(getClass().getResource("/sounds/pop.mp3").toString());

    @Override
    public void start(Stage stage) {
        carImage = new Image("file:car.png");
   /*     Media media = new Media(getClass().getResource("/sounds/Waltz.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
*/
        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);


        Pane pane = new Pane();



        gridModeButton.setLayoutX(canvas.getWidth() - gridModeButton.getWidth() - 75);
        gridModeButton.setLayoutY(90);
        gridModeButton.getStyleClass().add("grid-button");

        gridModeButton.setSelected(true);
        // Bind the canvas size to the pane size
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        ImageView playPause = new ImageView(new Image("file:play.png"));
        playPauseButton = new Button("", playPause);
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
        resetButton.setLayoutX(canvas.getWidth() - resetButton.getWidth() - 225);
        resetButton.setLayoutY(30);
        resetButton.getStyleClass().add("transparent-button");

        ImageView undoImage = new ImageView(new Image("file:jump.png"));
        undoImage.setFitWidth(30);
        undoImage.setFitHeight(30);
        Button undoButton = new Button("", undoImage);
        undoButton.getStyleClass().add("transparent-button");


        undoButton.setLayoutX(canvas.getWidth() - undoButton.getWidth() - 150);
        undoButton.setLayoutY(30);

        ImageView gearImage = new ImageView(new Image("file:sign-out.png"));
        Button gearButton = new Button("", gearImage);
        gearImage.setFitWidth(30);
        gearImage.setFitHeight(30);
        gearButton.setLayoutX(canvas.getWidth() - gearButton.getWidth() - 75);
        gearButton.setLayoutY(30);
        gearButton.getStyleClass().add("transparent-button");


        winWidget.setLayoutX(canvas.getWidth() / 2);
        winWidget.setLayoutY(canvas.getHeight() / 2);
        ImageView winImage = new ImageView(new Image("file:win.png"));
        winImage.setFitWidth(200);
        winImage.setFitHeight(200);
        winWidget.getChildren().add(winImage);
        winWidget.setAlignment(javafx.geometry.Pos.CENTER);
        winWidget.setSpacing(40);
        HBox winButtons = new HBox();
        winButtons.setAlignment(javafx.geometry.Pos.CENTER);
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Region spacerMiddle = new Region();
        HBox.setHgrow(spacerMiddle, Priority.ALWAYS);
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        ImageView nextLevelImage = new ImageView(new Image("file:forward.png"));
        ImageView restartImage = new ImageView(new Image("file:arrow.png"));
        nextLevelImage.setFitHeight(30);
        nextLevelImage.setFitWidth(30);
        restartImage.setFitHeight(30);
        restartImage.setFitWidth(30);
        Button nextLevelButton = new Button("", nextLevelImage);
        Button restartButton = new Button("", restartImage);
        winButtons.getChildren().addAll(spacerLeft, restartButton, spacerMiddle, nextLevelButton, spacerRight);
        winWidget.getChildren().add(winButtons);

        winWidget.getStyleClass().add("win-widget");
        winWidget.setDisable(true);


        pane.getChildren().addAll(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget, gridModeButton);

        Scene scene = new Scene(pane, 1000, 600);
        setupBridge(gc);

        stage.setMinWidth(400);
        stage.setMinHeight(300);

        AtomicBoolean gridFlag = new AtomicBoolean(gridModeButton.isSelected());
        playPauseButton.setOnAction(e -> {

            if (play) {
                stage.setResizable(true);
                playPause.setImage(new Image("file:play.png"));
                for (Beam beam : beams) {
                    beam.pin1.resetToInit();
                    beam.pin2.resetToInit();
                }
                ball1.setPosition(new Vector2D(50, 275));
                ball1.setOldPosition(new Vector2D(50, 275));
                this.lost = false;
                play = false;
                firstPin = null;

                gridModeButton.setSelected(gridFlag.get());
                for (Pin pin : pins) {
                    pin.setVelocity(new Vector2D(0, 0));
                }
            } else {
                gridFlag.set(gridModeButton.isSelected());
                playPause.setImage(new Image("file:pause.png"));
                play = true;
                gridModeButton.setSelected(false);
                stage.setResizable(false);

            }
        });
        restartButton.setOnAction(e -> {
            for (Beam beam : beams) {
                beam.pin1.resetToInit();
                beam.pin2.resetToInit();
            }
            ball1.setPosition(new Vector2D(0, 0));
            ball1.setOldPosition(new Vector2D(0, 0));
            this.lost = false;
            firstPin = null;
            gridModeButton.setSelected(true);
            winWidget.setDisable(true);
        });
        trussButton.setOnAction(e -> roadMode = false);
        roadButton.setOnAction(e -> roadMode = true);
        resetButton.setOnAction(e -> {
            if (firstPin != null) {
                if (firstPin.getConnectedBeamsSize() == 0)
                    pins.remove(firstPin);
                firstPin = null;
            }

            while (mouseCounter > 0)
                destroyBeam(beams.getLast());

            ball1.setPosition(new Vector2D(0, 0));
            ball1.setOldPosition(new Vector2D(0, 0));
            if (play) {
                playPause.setImage(new Image("file:play.png"));
                gridModeButton.setSelected(true);
                play = false;
                lost = false;
            }

            mouseCounter = 0;
        });
        undoButton.setOnAction(e -> {
            if (firstPin == null) {
                if (mouseCounter > 0) {
                    destroyBeam(beams.getLast());
                    System.out.println(mediaPlayer.getCurrentTime());
                }
            } else {
                if (firstPin.getConnectedBeamsSize() == 0)
                    pins.remove(firstPin);
                firstPin = null;
            }

        });
        //gear button action
        gearButton.setOnAction(e -> {
            GameTitleScreen gameTitleScreen = new GameTitleScreen();
            stage.setScene(gameTitleScreen.createGameTitleScene(stage));
        });

        nextLevelButton.setOnAction(e -> {
            if (level == 3) {
                level = 1;
            } else {
                level++;
            }
            beams.clear();
            pins.clear();
            startPins.clear();
            System.out.println("next level: " + level);
            setupBridge(gc);
            winWidget.setDisable(true);
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


                if((!gridModeButton.isSelected() && pins.size() > initalPinCount) || (gridModeButton.isSelected() && pins.size() > initalPinCount)) {
                    gridModeButton.setDisable(true);
                } else{
                    gridModeButton.setDisable(false);
                }


                render(gc);
            }

        }.start();
        previousWindowWidth = canvas.getWidth();
        previousWindowHeight = canvas.getHeight();
        updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget, gridModeButton);

        // Add listeners to update button positions when the canvas size changes

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget, gridModeButton));
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateOnResize(canvas, playPauseButton, controls, resetButton, undoButton, gearButton, winWidget, gridModeButton));

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.Z),
                () -> undoButton.fire()
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.R),
                () -> resetButton.fire()
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.P),
                () -> playPauseButton.fire()
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.G),
                () -> gridModeButton.fire()
        );

        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Bridge Simulation");
        stage.show();
    }

    private void updateOnResize(Canvas canvas, Button playPauseButton, HBox controls, Button resetButton, Button undoButton, Button gearButton, VBox winWidget, CheckBox gridModeButton) {
        playPauseButton.setLayoutX(30);
        playPauseButton.setLayoutY(20);

        this.maxLengthX = maxLengthX * canvas.getWidth() / previousWindowWidth;
        this.maxLengthY = maxLengthY * canvas.getHeight() / previousWindowHeight;
        this.maxLength = Math.max(maxLengthX, maxLengthY);
        this.massPerLRoad[0] *= Math.pow(previousWindowWidth / canvas.getWidth(), 2);
        this.massPerLRoad[1] *= Math.pow(previousWindowHeight / canvas.getHeight(), 2);
        this.massPerLTruss[0] *= Math.pow(previousWindowWidth / canvas.getWidth(), 2);
        this.massPerLTruss[1] *= Math.pow(previousWindowHeight / canvas.getHeight(), 2);
        this.stifness[0] *=   previousWindowWidth/canvas.getWidth();
        this.stifness[1] *= previousWindowHeight/canvas.getHeight();

//        this.gAccel *= canvas.getHeight() / previousWindowHeight;
        for (int i = 0; i<level1.length; i++){
            level1[i][0] *= canvas.getWidth() / previousWindowWidth;
            level1[i][1] *= canvas.getHeight() / previousWindowHeight;
        }
        for (int i = 0; i<level2.length; i++){
            level2[i][0] *= canvas.getWidth() / previousWindowWidth;
            level2[i][1] *= canvas.getHeight() / previousWindowHeight;
        }
        for (int i = 0; i<level3.length; i++){
            level3[i][0] *= canvas.getWidth() / previousWindowWidth;
            level3[i][1] *= canvas.getHeight() / previousWindowHeight;
        }
        for (Pin pin : startPins) {
            pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
        }
        for (Pin pin : pins) {
            if (!pin.isStartPin()) {
                pin.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), play);
            }
        }
        for (Beam beam : beams) {
            beam.beamSizeBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight(), stifness, (beam.isPhysical()) ? massPerLRoad : massPerLTruss, gAccel);

        }
        ball1.positionBinding(previousWindowWidth, previousWindowHeight, canvas.getWidth(), canvas.getHeight());


        controls.setLayoutX((canvas.getWidth() - controls.getWidth()) / 2);
        controls.setLayoutY(canvas.getHeight() - controls.getHeight() - 50);

        resetButton.setLayoutX(canvas.getWidth() - 225);
        resetButton.setLayoutY(30);

        undoButton.setLayoutX(canvas.getWidth() - 150);
        undoButton.setLayoutY(30);

        gearButton.setLayoutX(canvas.getWidth() - 75);
        gearButton.setLayoutY(30);

        gridModeButton.setLayoutX(canvas.getWidth() - 100);
        gridModeButton.setLayoutY(90);

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
                case 3:
                    level3();
                    break;
                default:
                    System.out.println("there is no such level");
                    break;
            }
        }

        List<Pin> newPins = new ArrayList<Pin>();
        pins.addAll(startPins);
        for (Pin pin : pins) {
            if (pin.isPositionFixed()) {
                Pin pin2 = null;
                if (pin.getPosition().x <= (gc.getCanvas().getWidth()) / 2) {
                    pin2 = new Pin(-200, pin.getPosition().y, true);
                    beams.add(new Beam(pin2, pin, new double[]{1, 1}, new double[]{0.0001, 0.0001}, 5000, 1, true));
                    gc.setFill(Color.GREEN);

                } else {
                    pin2 = new Pin(gc.getCanvas().getWidth() + 200, pin.getPosition().y, true);
                    beams.add(new Beam(pin, pin2, new double[]{1, 1}, new double[]{0.0001, 0.0001}, 5000, 1, true));

                }
                newPins.add(pin2);


            }
        }
        pins.addAll(newPins);
        initalPinCount = pins.size();
    }

    private void level1() {//fix numeration!
        for (int i=0; i < level1.length; i++){
            Pin pin = new Pin(level1[i][0], level1[i][1], true, true);
            startPins.add(pin);

        }

        this.lostArbitraryLimit = 550;
        this.winArbitraryLimit = 900;

        this.maxRoadBeam = 4;
        this.maxTruss = 30;
    }

    private void level2() {//fix numeration!
        for (int i=0; i < level2.length; i++){
            Pin pin = new Pin(level2[i][0], level2[i][1], true, true);
            startPins.add(pin);
        }
        this.lostArbitraryLimit = 500;
        this.winArbitraryLimit = 900;

        this.maxRoadBeam = 4;
        this.maxTruss = 18;

//        startPins.add(p1);
//        startPins.add(p2);
//        startPins.add(p3);
    }

    private void level3() {//fix numeration!
        for (int i=0; i < level3.length; i++){
            Pin pin = new Pin(level3[i][0], level3[i][1], true, true);
            startPins.add(pin);
        }

        this.lostArbitraryLimit = 500;
        this.winArbitraryLimit = 900;

        this.maxRoadBeam = 4;
        this.maxTruss = 10;

    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) { // Check if right-click
            for (Beam beam : beams) {
                if (!beam.pin1.isPositionFixed() || !beam.pin2.isPositionFixed()) {
                    if (isMouseHoveringOverBeam(x, y, beam)) {
                        destroyBeam(beam);
                        return;
                    }
                }
            }
        } else if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
            if (roadMode && maxRoadBeam <= 0 || !roadMode && maxTruss <= 0) {
                System.out.println("Cannot build this beam.");
                return;
            }
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
                double deltaX = Math.abs(x - firstPin.getPosition().x);
                double deltaY = Math.abs(y - firstPin.getPosition().y);
                if ((deltaX <= this.maxLengthX && deltaY <= this.maxLengthY) && Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= this.maxLength) {
                    Pin secondPin = (clickedPin != null ? clickedPin : new Pin(x, y, false));
                    if (clickedPin == null) {
                        pins.add(secondPin);
                    }

                    Beam beam = new Beam(firstPin, secondPin, stifness, roadMode ? massPerLRoad : massPerLTruss, (roadMode) ? breakLimitRoad : breakLimitTruss, gAccel, roadMode);

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
    }

    private void handleMouseMove(MouseEvent event) {
        cursorX = event.getX();
        cursorY = event.getY();

        if (gridModeButton.isSelected()) {
            cursorX = (cursorX % gridSizeX > gridSizeX / 2) ? cursorX - cursorX % gridSizeX + gridSizeX : cursorX - cursorX % gridSizeX;
            cursorY = (cursorY % gridSizeY > gridSizeY / 2) ? cursorY - cursorY % gridSizeY + gridSizeY : cursorY - cursorY % gridSizeY;
        }

        for (Pin pin : pins) {
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

    private boolean isMouseHoveringOverBeam(double mouseX, double mouseY, Beam beam) {
        Vector2D pos1 = beam.pin1.getPosition();
        Vector2D pos2 = beam.pin2.getPosition();

        // Calculate the distance from the mouse to the line segment
        double distance = pointToSegmentDistance(mouseX, mouseY, pos1.x, pos1.y, pos2.x, pos2.y);

        // Define a threshold for hovering (e.g., the width of the beam)
        double threshold = 10.0; // Adjust as needed

        return distance <= threshold;
    }

    private double pointToSegmentDistance(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lengthSquared = dx * dx + dy * dy;
        double t = ((px - x1) * dx + (py - y1) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;
        double distance = Math.sqrt((px - closestX) * (px - closestX) + (py - closestY) * (py - closestY));
        return distance;
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
        playPauseButton.fire();

    }

    private void updateSimulation(double deltaTime) {
        for (Beam beam : beams) {
            if (beam.isBroken()) {
                destroyBeam(beam);
            }
        }
        ball1.update(deltaTime, beams);

        for (Pin pin : pins) {
            if (pin.getConnectedBeamsSize() == 0 && !pin.isStartPin()) {
                pins.remove(pin);
                continue;
            }

            for (Beam beam : pin.getConnectedBeams()) {
                if (ball1.checkCollisions(beam)) {
                    if (pin == beam.pin1) {
                        if (beam.pin1.getPosition().subtract(ball1.getPosition()).magnitude() <= ball1.getPosition().subtract(beam.pin2.getPosition()).magnitude()) {//ball closer to pin1
                            pin.calculateForces(ball1.getMass());
                        }
                    } else {
                        if (beam.pin2.getPosition().subtract(ball1.getPosition()).magnitude() <= ball1.getPosition().subtract(beam.pin1.getPosition()).magnitude())
                            pin.calculateForces(ball1.getMass());
                    }
                } else {
                    pin.calculateForces(0);
                }
            }
        }

        for (Pin pin : pins)
            pin.update(deltaTime);

        System.out.println(pins.getFirst().getMassSum());
        for (Beam beam : beams) {
            if (beam.isPhysical()) {
                ball1.replace(beam);
            }
        }
        checkWin();
    }

    private void destroyBeam(Beam beam) {
        beams.remove(beam);
        beam.pin1.removeBeam(beam);
        beam.pin2.removeBeam(beam);
        if (beam.pin1.getConnectedBeamsSize() < 1) {
            pins.remove(beam.pin1);
        }
        if (beam.pin2.getConnectedBeamsSize() < 1) {
            pins.remove(beam.pin2);
        }
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
//            double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//

            Vector2D delta = new Vector2D((Math.abs(deltaX) <= maxLengthX) ? deltaX : Math.copySign(maxLengthX, deltaX), (Math.abs(deltaY) <= maxLengthY) ? deltaY : Math.copySign(maxLengthY, deltaY));//ratio >= 1 ? new Vector2D(x, y) : new Vector2D(pos1.x + deltaX * ratio, pos1.y + deltaY * ratio);
            if (delta.magnitude() > maxLength) {
                double ratio = this.maxLength / delta.magnitude();
                delta = delta.multiply(ratio);
            }
            Vector2D pos2 = pos1.add(delta);

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

        Beam currentBeam = ball1.getCurrentBeam();
        if (currentBeam != null) {
            Vector2D pos1 = currentBeam.pin1.getPosition();
            Vector2D pos2 = currentBeam.pin2.getPosition();
            double angle = Math.toDegrees(Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x));

            // Apply rotation to the car image
            gc.save();
            gc.translate(ball1.getPosition().x + 17.175, ball1.getPosition().y + 7.575); // Translate to the center of the car image
            gc.rotate(angle);
            gc.drawImage(carImage, -17.175, -7.575, 34.35, 15.15); // Draw the car image centered
            gc.restore();
        } else {
            gc.drawImage(carImage, ball1.getPosition().x, ball1.getPosition().y, (34.35), 15.15);

            gc.setStroke(Color.RED);
            gc.setLineWidth(1);
            gc.setLineDashes(5);
            gc.strokeLine(0, this.lostArbitraryLimit, gc.getCanvas().getWidth(), this.lostArbitraryLimit);
        }

        if (lost) {
            String text = "BUST";
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


