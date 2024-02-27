package main.java.statistics;

public class LogElement {
    // Statistics about the organisms
    public final int population;
    public final double timeHours;

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
    public LogElement(int population, double timeHours, double averageEnergy, double averageWeight, double heaviestOrganismWeight,
                      int averageAge, int oldestOrganismAge, int averageGeneration, int newestGeneration,
                      int oldestGeneration, double averageVelocity, int averageNumChildren,
                      int mostNumChildrenAmount, int averageNumFoodEaten, int mostNumFoodEaten,
                      double averageEnergySpentPerFood, double lowestEnergySpentPerFoodValue,
                      int foodCount, int maxFoodAmount, int averageFoodAge, int oldestFoodAge) {
        this.population = population;
        this.timeHours = timeHours;
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
            "\n                   population=" + population +
            "\n                         time=" + formatTime(timeHours) +
            "\n                averageEnergy=" + averageEnergy +
            "\n                averageWeight=" + averageWeight +
            "\n       heaviestOrganismWeight=" + heaviestOrganismWeight +
            "\n                   averageAge=" + averageAge +
            "\n            oldestOrganismAge=" + oldestOrganismAge +
            "\n            averageGeneration=" + averageGeneration +
            "\n             newestGeneration=" + newestGeneration +
            "\n             oldestGeneration=" + oldestGeneration +
            "\n              averageVelocity=" + averageVelocity +
            "\n           averageNumChildren=" + averageNumChildren +
            "\n        mostNumChildrenAmount=" + mostNumChildrenAmount +
            "\n          averageNumFoodEaten=" + averageNumFoodEaten +
            "\n             mostNumFoodEaten=" + mostNumFoodEaten +
            "\n    averageEnergySpentPerFood=" + averageEnergySpentPerFood +
            "\nlowestEnergySpentPerFoodValue=" + lowestEnergySpentPerFoodValue +
            "\n                    foodCount=" + foodCount +
            "\n                maxFoodAmount=" + maxFoodAmount +
            "\n               averageFoodAge=" + averageFoodAge +
            "\n                oldestFoodAge=" + oldestFoodAge +
            "\n}";
    }

    public static String formatTime(double timeInHours) {
        // Calculate hours, minutes, and seconds
        int hours = (int) timeInHours;
        int minutes = (int) ((timeInHours - hours) * 60);
        int seconds = (int) (((timeInHours - hours) * 60 - minutes) * 60);

        // Format the time as "hhh:mm:ss"
        String formattedTime = String.format("%03d:%02d:%02d", hours, minutes, seconds);
        return formattedTime;
    }

}
