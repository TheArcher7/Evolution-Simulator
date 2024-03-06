package main.java.view;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;
import main.java.util.Pos;

import javax.swing.*;
import java.awt.*;

public class WorldView extends JPanel{
    public boolean DEBUG_MODE = true;

    public boolean showGluttony = false;
    public boolean showLust = false;
    public boolean showSloth = false;
    public boolean showPride = false;
    public boolean showGreed = false;
    public boolean showWrath = false;
    public boolean showEnvy = false;

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
            if(DEBUG_MODE){
                drawHitbox(g, food.hitbox);

                continue;
            }
            g.fillOval(
                    (int) (food.position.xCoord / zoomFactor + (double) offsetX / zoomFactor),
                    (int) (food.position.yCoord / zoomFactor + (double) offsetY / zoomFactor),
                    (int) (food.size * 2 / zoomFactor),
                    (int) (food.size * 2 / zoomFactor)
                    );
        }
    }

    private void drawOrganisms(Graphics g) {
        int organismRadius, X, Y, R;
        for (BaseOrganism organism : world.getOrganisms()) {
            g.setColor(organism.color);
            X = (int) (organism.position.xCoord / zoomFactor) + offsetX / zoomFactor;
            Y = (int) (organism.position.yCoord / zoomFactor) + offsetY / zoomFactor;

            if(DEBUG_MODE){
                g.setColor(Color.BLUE);
                drawHitbox(g, organism.hitbox);
                g.setColor(Color.RED);
                if( (showGluttony && organism.gluttony)
                    || (showLust && organism.lust)
                    || (showSloth && organism.sloth)
                    || (showPride && organism.pride)
                    || (showGreed && organism.greed)
                    || (showWrath && organism.wrath)
                    || (showEnvy && organism.envy))
                    g.setColor(Color.BLACK);
                drawVisionLines(g, X, Y, organism.visionPoints);

                String displayString = "" + organism.generation;
                g.drawString(displayString, (int) (organism.position.xCoord / zoomFactor + (double) offsetX / zoomFactor), (int) ((organism.position.yCoord - organism.size -1) / zoomFactor + (double) offsetY / zoomFactor));
                continue;
            }

            // Draw the organism's body
            organismRadius = (int) Math.round(organism.size);
            R = (int) (organismRadius / zoomFactor);
            g.fillOval( X - R, Y - R, 2 * R, 2 * R);

            // Draw the outtermost and center vision lines
            if((showGluttony && organism.gluttony)
                || (showLust && organism.lust)
                || (showSloth && organism.sloth)
                || (showPride && organism.pride)
                || (showGreed && organism.greed)
                || (showWrath && organism.wrath)
                || (showEnvy && organism.envy))
                    g.setColor(Color.BLACK);
            drawVisionLines(g, X, Y, 
                new Pos[]{
                    organism.visionPoints[0],
                    organism.visionPoints[(organism.visionPoints.length - 1) / 2],
                    organism.visionPoints[organism.visionPoints.length - 1]
                }
            );
        }
    }

    private void drawVisionLines(Graphics g, int X, int Y, Pos[] points) {
        for (Pos point : points) {
            g.drawLine(
                X, Y, 
                (int) ((point.xCoord / zoomFactor) + offsetX / zoomFactor), 
                (int) ((point.yCoord / zoomFactor) + offsetY / zoomFactor));
        }
    }

    private void drawHitbox(Graphics g, Pos[] points){
        int x1 = (int) ((points[0].xCoord / zoomFactor) + offsetX / zoomFactor);
        int x2 = (int) ((points[1].xCoord / zoomFactor) + offsetX / zoomFactor);
        int y1 = (int) ((points[0].yCoord / zoomFactor) + offsetY / zoomFactor);
        int y2 = (int) ((points[1].yCoord / zoomFactor) + offsetY / zoomFactor);

        //draws a line between every point in the hitbox. Should look like a square with an x through the center
        g.drawLine(x1, y1, x2, y2); 
        g.drawLine(x1, y1, x1, y2);
        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x2, y1, x2, y2);
        g.drawLine(x1, y2, x2, y2);
        g.drawLine(x1, y2, x2, y1);
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

    // Method to toggle the value of seven sins
    public void toggleShowGluttony() {showGluttony = !showGluttony;}
    public void toggleShowLust() {showLust = !showLust;}
    public void toggleShowSloth() {showSloth = !showSloth;}
    public void toggleShowPride() {showPride = !showPride;}
    public void toggleShowGreed() {showGreed = !showGreed;}
    public void toggleShowWrath() {showWrath = !showWrath;}
    public void toggleShowEnvy() {showEnvy = !showEnvy;}
}
