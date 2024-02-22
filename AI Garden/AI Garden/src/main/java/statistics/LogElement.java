package main.java.statistics;

public class LogElement {
    // Statistics about the organisms
    public final int population;
    public final double averageEnergy;
    public final double averageWeight;
    public final double heaviestOrganismWeight;
    public final int averageAge;
    public final int oldestOrganismAge;
    public final int averageGeneration;
    public final int newestGeneration;
    public final int oldestGeneration;
    public final double averageVelocity;
    public final int averageNumChildren;
    public final int mostNumChildrenAmount;
    public final int averageNumFoodEaten;
    public final int mostNumFoodEaten;
    public final double averageEnergySpentPerFood;
    public final double lowestEnergySpentPerFoodValue;

    // Statistics about the world elements
    public final int foodCount;
    public final int maxFoodAmount;
    public final int averageFoodAge;
    public final int oldestFoodAge;

    // Constructor to set elements
    public LogElement(int population, double averageEnergy, double averageWeight, double heaviestOrganismWeight,
                      int averageAge, int oldestOrganismAge, int averageGeneration, int newestGeneration,
                      int oldestGeneration, double averageVelocity, int averageNumChildren,
                      int mostNumChildrenAmount, int averageNumFoodEaten, int mostNumFoodEaten,
                      double averageEnergySpentPerFood, double lowestEnergySpentPerFoodValue,
                      int foodCount, int maxFoodAmount, int averageFoodAge, int oldestFoodAge) {
        this.population = population;
        this.averageEnergy = averageEnergy;
        this.averageWeight = averageWeight;
        this.heaviestOrganismWeight = heaviestOrganismWeight;
        this.averageAge = averageAge;
        this.oldestOrganismAge = oldestOrganismAge;
        this.averageGeneration = averageGeneration;
        this.newestGeneration = newestGeneration;
        this.oldestGeneration = oldestGeneration;
        this.averageVelocity = averageVelocity;
        this.averageNumChildren = averageNumChildren;
        this.mostNumChildrenAmount = mostNumChildrenAmount;
        this.averageNumFoodEaten = averageNumFoodEaten;
        this.mostNumFoodEaten = mostNumFoodEaten;
        this.averageEnergySpentPerFood = averageEnergySpentPerFood;
        this.lowestEnergySpentPerFoodValue = lowestEnergySpentPerFoodValue;
        this.foodCount = foodCount;
        this.maxFoodAmount = maxFoodAmount;
        this.averageFoodAge = averageFoodAge;
        this.oldestFoodAge = oldestFoodAge;
    }

    @Override
    public String toString() {
        return "LogElement{" +
            "\npopulation=" + population +
            "\naverageEnergy=" + averageEnergy +
            "\naverageWeight=" + averageWeight +
            "\nheaviestOrganismWeight=" + heaviestOrganismWeight +
            "\naverageAge=" + averageAge +
            "\noldestOrganismAge=" + oldestOrganismAge +
            "\naverageGeneration=" + averageGeneration +
            "\nnewestGeneration=" + newestGeneration +
            "\noldestGeneration=" + oldestGeneration +
            "\naverageVelocity=" + averageVelocity +
            "\naverageNumChildren=" + averageNumChildren +
            "\nmostNumChildrenAmount=" + mostNumChildrenAmount +
            "\naverageNumFoodEaten=" + averageNumFoodEaten +
            "\nmostNumFoodEaten=" + mostNumFoodEaten +
            "\naverageEnergySpentPerFood=" + averageEnergySpentPerFood +
            "\nlowestEnergySpentPerFoodValue=" + lowestEnergySpentPerFoodValue +
            "\nfoodCount=" + foodCount +
            "\nmaxFoodAmount=" + maxFoodAmount +
            "\naverageFoodAge=" + averageFoodAge +
            "\noldestFoodAge=" + oldestFoodAge +
            "\n}";
    }

}
