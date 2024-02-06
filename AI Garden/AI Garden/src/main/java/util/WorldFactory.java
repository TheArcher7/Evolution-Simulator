package main.java.util;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;

// A class to be used to set up the world, populate it with organisms

public class WorldFactory {
    public static WorldModel createTestWorld() {
        //Set up world and size
        int width = 10000;
        int height = 800;
        WorldModel world = new WorldModel(width, height, 50);

        // Example organism usage
        Pos position = new Pos(0, 0);
        double thetaDirection = 90.0;
        double[] phiVisionDirection = new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0};
        double visionRadius = 60.0;
        double size = 10.0;

        BaseOrganism organism = new BaseOrganism(
            position, 
            thetaDirection, 
            phiVisionDirection, 
            visionRadius, 
            size);

        //Populate with organisms
        BaseOrganism[] organisms = {
            new BaseOrganism(new Pos(0.0, 0.0), 
                thetaDirection, 
                phiVisionDirection, 
                visionRadius, 
                size),
            new BaseOrganism(new Pos(-120, 30), 
                0, 
                phiVisionDirection, 
                visionRadius, 
                size),
            new BaseOrganism(
                new Pos(800,300), 
                270, 
                phiVisionDirection, 
                visionRadius, 
                size)
        };
        for (BaseOrganism o : organisms) {
            world.addOrganism(o);
        }

        //TODO set AI

        Food[] foods = new Food[organisms.length * 10];
        for (int i = 0; i < organisms.length * 10; i++) {
            double posX = width * Math.random();
            double posY = height * Math.random();
            foods[i] = new Food(new Pos(posX, posY), (int) (Math.random() * 100), 3);

            world.addFood(foods[i]);
        }

        return world;
    }
}
