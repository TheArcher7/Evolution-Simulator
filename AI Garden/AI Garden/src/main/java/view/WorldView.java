package main.java.view;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;
import main.java.util.Pos;

import javax.swing.*;
import java.awt.*;

public class WorldView extends JPanel{
    public static boolean DEBUG_MODE = true;

    private WorldModel world;

    private int zoomFactor;
    private int offsetX;
    private int offsetY;


    // Constructor
    public WorldView(WorldModel world) {
        this.world = world;
        zoomFactor = 1;
        setFocusable(true);
    }

    // Methods
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawFood(g);
        drawOrganisms(g);
    }

    private void drawFood(Graphics g) {
        for (Food food : world.getFoods()) {
            g.setColor(food.color);
            g.fillOval(
                    (int) (food.position.xCoord / zoomFactor + (double) offsetX / zoomFactor),
                    (int) (food.position.yCoord / zoomFactor + (double) offsetY / zoomFactor),
                    (int) (food.size / zoomFactor),
                    (int) (food.size / zoomFactor)
                    );
            if(DEBUG_MODE){
                drawHitbox(g, food.getHitbox());
            }
        }
    }

    private void drawOrganisms(Graphics g) {
        int organismRadius, X, Y, R;
        for (BaseOrganism organism : world.getOrganisms()) {
            organismRadius = (int) Math.round(organism.getSize());
            X = (int) (organism.position.xCoord * zoomFactor) + offsetX;
            Y = (int) (organism.position.yCoord * zoomFactor) + offsetY;
            R = (int) (organismRadius * zoomFactor);

            // Draw the organism's body
            g.drawOval( X - R, Y - R, 2 * R, 2 * R);

            if(DEBUG_MODE){
                drawVisionLines(g, X, Y, organism.getVisionPoints());
                drawHitbox(g, organism.getHitbox());
            }
        }
    }

    private void drawVisionLines(Graphics g, int X, int Y, Pos[] points) {
        g.setColor(Color.RED);

        for (Pos point : points) {
            g.drawLine(
                X, Y, 
                (int) ((point.xCoord * zoomFactor) + offsetX), 
                (int) ((point.yCoord * zoomFactor) + offsetY));
        }
    }

    private void drawHitbox(Graphics g, Pos[] points){
        g.setColor(Color.BLUE);
        //draws a line between every point in the hitbox. Should look like a square with an x through the center
        drawLine(g, points, 0, 1);
        drawLine(g, points, 1, 2);
        drawLine(g, points, 2, 3);
        drawLine(g, points, 3, 0);
        drawLine(g, points, 1, 3);
        drawLine(g, points, 0, 2);
    }

    private void drawLine(Graphics g, Pos[] points, int point1, int point2){
        g.drawLine(
            (int) ((points[point1].xCoord * zoomFactor) + offsetX), 
            (int) ((points[point1].yCoord * zoomFactor) + offsetY), 
            (int) ((points[point2].xCoord * zoomFactor) + offsetX), 
            (int) ((points[point2].yCoord * zoomFactor) + offsetY));
    }


    // Setters and Getters
    public int getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(int zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setWorld(WorldModel worldModel) {
        this.world = worldModel;
    }
}
