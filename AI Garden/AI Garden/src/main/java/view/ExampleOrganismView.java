package main.java.view;

import javax.swing.*;
import java.awt.*;

import main.java.model.BaseOrganism;
import main.java.util.Pos;


public class ExampleOrganismView extends JFrame {
    private BaseOrganism organism;

    public ExampleOrganismView(BaseOrganism organism) {
        this.organism = organism;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Map the coordinate plane to the pixels on the frame with scaling
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int offsetX = frameWidth / 2;
        int offsetY = frameHeight / 2;
        double scaleFactor = 4; //higher == larger on screen

        int organismRadius = (int) Math.round(organism.size);


        // Draw the organism
        int X = (int) (organism.position.xCoord * scaleFactor) + offsetX;
        int Y = (int) (organism.position.yCoord * scaleFactor) + offsetY;
        int R = (int) (organismRadius * scaleFactor);
        g.drawOval( X - R, Y - R, 2 * R, 2 * R);

        // Draw the organism's vision lines
        Pos[] visionPoints = organism.visionPoints;
        drawVisionLines(g, offsetX, offsetY, (int) scaleFactor, X, Y, visionPoints);

        // Draw the organism's hitbox
        Pos[] hitbox = organism.hitbox;
        drawHitbox(g, offsetX, offsetY, (int) scaleFactor, hitbox);
    }

    private void drawVisionLines(Graphics g, int offsetX, int offsetY, int scaleFactor, int X, int Y, Pos[] points) {
        g.setColor(Color.RED);

        for (Pos point : points) {
            g.drawLine(
                X, Y, 
                (int) ((point.xCoord * scaleFactor) + offsetX), 
                (int) ((point.yCoord * scaleFactor) + offsetY));
        }
    }

    private void drawHitbox(Graphics g, int offsetX, int offsetY, int scaleFactor, Pos[] points){
        g.setColor(Color.BLUE);
        //draws a line between every point in the hitbox. Should look like a square with an x through the center
        drawLine(g, offsetX, offsetY, scaleFactor, points, 0, 1);
        drawLine(g, offsetX, offsetY, scaleFactor, points, 1, 2);
        drawLine(g, offsetX, offsetY, scaleFactor, points, 2, 3);
        drawLine(g, offsetX, offsetY, scaleFactor, points, 3, 0);
        drawLine(g, offsetX, offsetY, scaleFactor, points, 1, 3);
        drawLine(g, offsetX, offsetY, scaleFactor, points, 0, 2);
    }

    private void drawLine(Graphics g, int offsetX, int offsetY, int scaleFactor, Pos[] points, int point1, int point2){
        g.drawLine(
            (int) ((points[point1].xCoord * scaleFactor) + offsetX), 
            (int) ((points[point1].yCoord * scaleFactor) + offsetY), 
            (int) ((points[point2].xCoord * scaleFactor) + offsetX), 
            (int) ((points[point2].yCoord * scaleFactor) + offsetY));
    }

}

