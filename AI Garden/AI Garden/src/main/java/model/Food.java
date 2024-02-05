package main.java.model;

import main.java.util.Pos;
import java.awt.Color;

public class Food {
    // Attributes
    public Pos position;
    double value;
    Pos[] hitbox;
    
    // Attributes for graphics
    public double size = 3;
    public Color color = new Color(116, 195, 101);

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

