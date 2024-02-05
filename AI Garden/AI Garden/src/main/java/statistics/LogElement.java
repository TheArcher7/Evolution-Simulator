package main.java.statistics;

public class LogElement {
    private final int population;
    private final double averageLifespan;
    private final double averageEnergy;
    private final double averageSpeed;
    private final double averageVisionRange;
    private final double averageNumChildrenBorn;

    // Constructor to set elements
    public LogElement(int population, double averageLifespan, double averageEnergy, double averageSpeed, double averageVisionRange, double averageNumChildrenBorn) {
        this.population = population;
        this.averageLifespan = averageLifespan;
        this.averageEnergy = averageEnergy;
        this.averageSpeed = averageSpeed;
        this.averageVisionRange = averageVisionRange;
        this.averageNumChildrenBorn = averageNumChildrenBorn;
    }

    // Getter for population
    public int getPopulation() {
        return population;
    }

    // Getter for averageLifespan
    public double getAverageLifespan() {
        return averageLifespan;
    }

    // Getter for averageEnergy
    public double getAverageEnergy() {
        return averageEnergy;
    }

    // Getter for averageSpeed
    public double getAverageSpeed() {
        return averageSpeed;
    }

    // Getter for averageVisionRange
    public double getAverageVisionRange() {
        return averageVisionRange;
    }

    // Getter for averageNumChildrenBorn
    public double getAverageNumChildrenBorn() {
        return averageNumChildrenBorn;
    }
}
