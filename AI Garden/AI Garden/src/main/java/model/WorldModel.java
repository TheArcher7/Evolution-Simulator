package main.java.model;


import java.util.ArrayList;
import java.util.List;

public class WorldModel {
    //TODO add better access methods for these 
    public int maxFoodEnergy = 1200;
    public int desiredMaxFoodEnergy = 500;

    public int maxFoodAmount = 8000;
    public int desiredMaxFoodAmount = 2000;

    public int ticksPer_FoodSpawn = 1;
    public int foodSpawnedPerEvent = 3;

    public double foodDensity = 0.0009201325; // Needed for calculating expanding / shrinking worlds. When the world shrinks or expands, it adjusts the maximum food ammount to match the current density

    public static double baseEnergyDepletionRate = 0.4;
    public static double speedEnergyDepletionFactor = 1;

    public static boolean LEARN_ENABLED = true; // the AI will evolve
    public double mutationRate = 0.15;
    public double mutationStrength = 0.1;
    public static double worldMutationRate = 0.1;
    public static boolean useLocalMutationRate = false; //if true, then organisms can determin their own mutation rate
    public static double speedFactor = 1;

    public boolean useLifespan = false; //if enabled, will kill organisms older than a certain age
    public boolean useFoodspoil = true; //if enabled, will kill food older than a certain age
    public int lifespan = 420;
    public int food_lifespan = 620; //600 is 200 seconds longer than the typical oldest foods in simulation tests

    private final List<BaseOrganism> organisms;
    private final List<Food> foods;

    public double desiredWidth = 10500;
    public double width;
    public double height;

    // Constructor
    public WorldModel(int width, int height, int foodCreationRate) {
        organisms = new ArrayList<>();
        foods = new ArrayList<>();
        this.width = width;
        this.height = height;
        foodSpawnedPerEvent = foodCreationRate;
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

    /**
     * Method for returning a string representation of the attributes of the world
     * to be saved in a text file.
     */
    public String getSerialization(){

        return "";
    }


    public void clearOrganisms() {
        organisms.clear();
    }
}
