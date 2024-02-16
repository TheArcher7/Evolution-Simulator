package main.java.model;


import java.util.ArrayList;
import java.util.List;

public class WorldModel {
    //TODO add better access methods for these 
    public static int maxFoodEnergy = 1000;
    public static int maxFoodAmount = 8000;
    public int ticksPer_OneFoodSpawned = 7;

    public static double baseEnergyDepletionRate = 1;
    public static double speedEnergyDepletionFactor = 1;

    public static boolean LEARN_ENABLED = true; // the AI will evolve
    public double mutationRate = 0.1;
    public double mutationStrength = 0.01;
    public static double worldMutationRate = 0.1;
    public static boolean useLocalMutationRate = false; //if true, then organisms can determin their own mutation rate
    public static double speedFactor = 1;

    public boolean useLifespan = false; //if enabled, will kill organisms older than a certain age
    public int lifespan = 120;

    private final List<BaseOrganism> organisms;
    private final List<Food> foods;

    public double width;
    public double height;

    // Constructor
    public WorldModel(int width, int height, int foodCreationRate) {
        organisms = new ArrayList<>();
        foods = new ArrayList<>();
        this.width = width;
        this.height = height;
        ticksPer_OneFoodSpawned = foodCreationRate;
    }


    // Getter and Setter for width
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    // Getter and Setter for height
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // Getter for organisms list
    public List<BaseOrganism> getOrganisms() {
        return organisms;
    }

    // Getter for foods list
    public List<Food> getFoods() {
        return foods;
    }

    // Method to add organism to the list
    public void addOrganism(BaseOrganism organism) {
        organisms.add(organism);
    }

    // Method to remove organism from the list
    public void removeOrganism(BaseOrganism organism) {
        organisms.remove(organism);
    }

    // Method to add food to the list
    public void addFood(Food food) {
        foods.add(food);
    }

    // Method to remove food from the list
    public void removeFood(Food food) {
        foods.remove(food);
    }
}
