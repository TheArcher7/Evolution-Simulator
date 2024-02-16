package main.java.util;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;

// A class to be used to set up the world, populate it with organisms

public class WorldFactory {
    public static WorldModel createTestWorld() {
        //Set up world and size
        int width = 1000;
        int height = 800;
        WorldModel world = new WorldModel(width, height, 5);

        // Example organism usage
        //Pos position = new Pos(0, 0);
        //BaseOrganism exampleOrganism = new BaseOrganism(position, world);

        //Populate with organisms
        BaseOrganism[] organisms = {
            new BaseOrganism(new Pos(width / 2, height / 2), world),
            new BaseOrganism(new Pos(width / 2, height / 2), world),
            new BaseOrganism(new Pos(width / 2, height / 2), world),
            new BaseOrganism(new Pos(width / 2, height / 2), world)
        };
        for (BaseOrganism o : organisms) {
            world.addOrganism(o);
        }

        
        Food[] foods = new Food[organisms.length * 100];
        for (int i = 0; i < organisms.length * 10; i++) {
            double posX = width * Math.random();
            double posY = height * Math.random();
            foods[i] = new Food(new Pos(posX, posY), (int) (Math.random() * WorldModel.maxFoodEnergy), 3);

            world.addFood(foods[i]);
        }

        return world;
    }
}
