package main.java.model;

import main.java.util.Point;

public class Food {
    // Attributes
    private Point position;
    private double value;
    private double size;
    private Point[] hitbox;

    // Constructor
    public Food(Point position, double value, double size) {
        this.position = position;
        this.value = value;
        this.size = size;

        // Initialize hitbox
        this.hitbox = new Point[4];
        updateHitbox();
    }

    // Getter methods
    public Point getPosition() {
        return position;
    }

    public double getValue() {
        return value;
    }

    // Setter methods
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        updateHitbox();
    }

    public Point[] getHitbox() {
        return hitbox;
    }

    // Method to update hitbox based on position and size
    public void updateHitbox() {
        double hitboxSize = size;

        hitbox[0] = new Point(position.xCoord - hitboxSize, position.yCoord - hitboxSize);
        hitbox[1] = new Point(position.xCoord + hitboxSize, position.yCoord - hitboxSize);
        hitbox[2] = new Point(position.xCoord - hitboxSize, position.yCoord + hitboxSize);
        hitbox[3] = new Point(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
    }

    // Additional methods for the Food class could be added here
}

