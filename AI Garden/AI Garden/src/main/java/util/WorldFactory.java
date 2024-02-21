package main.java.util;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;

// A class to be used to set up the world, populate it with organisms

public class WorldFactory {
    public static WorldModel createTestWorld() {
        //Set up world and size
        int width = 3520;
        int height = 2470;
        WorldModel world = new WorldModel(width, height, 3);
        double startingFoodAmount = 8000;
        world.foodDensity = startingFoodAmount / (width * height);

        // Example organism usage
        //Pos position = new Pos(0, 0);
        //BaseOrganism exampleOrganism = new BaseOrganism(position, world);

        //Populate with organisms
        BaseOrganism[] organisms = {
            new BaseOrganism(new Pos(width / 1.5, height / 1.5), world),
            new BaseOrganism(new Pos(width / 4, height / 1.5), world),
            new BaseOrganism(new Pos(width / 4, height / 4), world),
            new BaseOrganism(new Pos(width / 1.5, height / 4), world)
        };
        for (BaseOrganism o : organisms) {
            world.addOrganism(o);
        }

        
        Food[] foods = new Food[world.maxFoodAmount];
        for (int i = 0; i < world.maxFoodAmount; i++) {
            double posX = width * Math.random();
            double posY = height * Math.random();
            foods[i] = new Food(new Pos(posX, posY), world.maxFoodEnergy, 3);
            foods[i].age = Food.generateRandomNumber(0, 50);

            world.addFood(foods[i]);
        }

        return world;
    }
}
