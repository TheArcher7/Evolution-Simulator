package main.java.model;

import main.java.util.Pos;

public class Food {
    // Attributes
    private Pos position;
    private double value;
    private double size;
    private Pos[] hitbox;

    // Constructor
    public Food(Pos position, double value, double size) {
        this.position = position;
        this.value = value;
        this.size = size;

        // Initialize hitbox
        this.hitbox = new Pos[4];
        updateHitbox();
    }

    // Getter methods
    public Pos getPosition() {
        return position;
    }

    public double getValue() {
        return value;
    }

    // Setter methods
    public void setPosition(Pos position) {
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

    public Pos[] getHitbox() {
        return hitbox;
    }

    // Method to update hitbox based on position and size
    public void updateHitbox() {
        double hitboxSize = size;

        hitbox[0] = new Pos(position.xCoord - hitboxSize, position.yCoord - hitboxSize);
        hitbox[1] = new Pos(position.xCoord + hitboxSize, position.yCoord - hitboxSize);
        hitbox[2] = new Pos(position.xCoord - hitboxSize, position.yCoord + hitboxSize);
        hitbox[3] = new Pos(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
    }

    // Additional methods for the Food class could be added here
}

