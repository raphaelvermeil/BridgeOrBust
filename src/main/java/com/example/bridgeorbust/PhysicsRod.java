package com.example.bridgeorbust;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PhysicsRod {
    private PhysicsCircle c1;
    private PhysicsCircle c2;
    private double length;
    private Line line;

    public PhysicsRod(PhysicsCircle c1, PhysicsCircle c2, double length) {
        this.c1 = c1;
        this.c2 = c2;
        this.length = length;
        this.line = new Line();
        this.line.setStroke(Color.WHITE);
        this.line.setStrokeWidth(30);

        updateLine();
    }

    public void enforceConstraint() {
        double dx = c2.getCenterX() - c1.getCenterX();
        double dy = c2.getCenterY() - c1.getCenterY();
        double currentLength = Math.sqrt(dx * dx + dy * dy);
        double difference = length - currentLength;
        double percent = difference / currentLength / 2;
        double offsetX = dx * percent;
        double offsetY = dy * percent;

        c1.setCenterX(c1.getCenterX() - offsetX);
        c1.setCenterY(c1.getCenterY() - offsetY);
        c2.setCenterX(c2.getCenterX() + offsetX);
        c2.setCenterY(c2.getCenterY() + offsetY);

        updateLine();
    }

    private void updateLine() {
        line.setStartX(c1.getCenterX());
        line.setStartY(c1.getCenterY());
        line.setEndX(c2.getCenterX());
        line.setEndY(c2.getCenterY());
    }

    public Line getLine() {
        return line;
    }
}
