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
        double startingFoodAmount = 8000;
        WorldModel world = new WorldModel(width, height, 3);
        world.desiredMaxFoodAmount = 2000; //food will decrease over time
        world.foodDensity = startingFoodAmount / (width * height);
        world.desiredWidth = 10500; //will expand over time

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

        // Create food
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

    /**
     * Creates an average starting world with an area size and food amount that has been tested to provide balanced
     * competative environments for developing the organism's AI. 
     */
    public static WorldModel createAverageWorld() {
        //Set up world and size
        int width = 3520;
        int height = 2470;
        double startingFoodAmount = 8000;
        WorldModel world = new WorldModel(width, height, 3);
        world.foodDensity = startingFoodAmount / (width * height);
        world.width *= 1.1;
        world.desiredWidth = 8000;
        world.height *= 1.1;
        world.maxFoodAmount = (int) ((world.width * world.height) * world.foodDensity);
        world.desiredMaxFoodAmount = 8500;

        //Populate with organisms
        BaseOrganism[] organisms = {
            new BaseOrganism(new Pos(world.width / 1.5, world.height / 1.5), world),
            new BaseOrganism(new Pos(world.width / 4, world.height / 1.5), world),
            new BaseOrganism(new Pos(world.width / 4, world.height / 4), world),
            new BaseOrganism(new Pos(world.width / 1.5, world.height / 4), world)
        };
        for (BaseOrganism o : organisms) {
            world.addOrganism(o);
        }

        // Create food
        Food[] foods = new Food[world.maxFoodAmount];
        for (int i = 0; i < world.maxFoodAmount; i++) {
            double posX = world.width * Math.random();
            double posY = world.height * Math.random();
            foods[i] = new Food(new Pos(posX, posY), world.maxFoodEnergy, 3);
            foods[i].age = Food.generateRandomNumber(0, 50);

            world.addFood(foods[i]);
        }

        return world;
    }

    public static WorldModel createBigWorld(){
        //Set up world and size
        int width = 3520;
        int height = 2470;
        double startingFoodAmount = 8000;
        WorldModel world = new WorldModel(width, height, 6);
        world.foodDensity = startingFoodAmount / (width * height);
        world.width = world.width * 2;
        world.desiredWidth = 13750;
        world.height = world.height * 2;
        world.maxFoodAmount = (int) ((world.width * world.height) * world.foodDensity);
        world.desiredMaxFoodAmount = 12000;

        //Populate with organisms
        BaseOrganism[] organisms = {
            new BaseOrganism(new Pos(world.width / 1.5, world.height / 1.5), world),
            new BaseOrganism(new Pos(world.width / 4, world.height / 1.5), world),
            new BaseOrganism(new Pos(world.width / 4, world.height / 4), world),
            new BaseOrganism(new Pos(world.width / 1.5, world.height / 4), world)
        };
        for (BaseOrganism o : organisms) {
            world.addOrganism(o);
        }

        // Create food
        Food[] foods = new Food[world.maxFoodAmount];
        for (int i = 0; i < world.maxFoodAmount; i++) {
            double posX = world.width * Math.random();
            double posY = world.height * Math.random();
            foods[i] = new Food(new Pos(posX, posY), world.maxFoodEnergy, 3);
            foods[i].age = Food.generateRandomNumber(0, 50);

            world.addFood(foods[i]);
        }

        return world;
    }
}
