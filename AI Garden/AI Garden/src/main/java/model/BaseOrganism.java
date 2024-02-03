package main.java.model;

import main.java.util.Point;

public class BaseOrganism {
    // Attributes
    private Point position;
    private double weight;
    private double energy;
    private double maxEnergy;

    // Attributes for Life
    private int age;
    private int maxAge;
    private boolean isAlive;
    private double energyNeededToReproduce;
    private double weightNeededToReproduce;

    // Attributes for vision
    private double phiDirection; //direction the creature is facing
    private double deltaDirection; //the change of direction of the creature. Between -1 and 1
    private double[] thetaVisionDirection; //the angles of the vision lines
    private double visionRadius; //the distance the vision lines extend from the organism
    private double size;
    private Point[] hitbox;


    // Constructor
    public BaseOrganism(Point position, double weight, double maxEnergy) {
        this.position = position;
        this.weight = weight;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy; // Start with full energy

        // Initialize hitbox
        this.hitbox = new Point[4];
        updateHitbox();
    }

    // Constructor 2
    public BaseOrganism(Point position, double weight, double maxEnergy,
                        double phiDirection, double[] thetaVisionDirection, double visionRadius) {
        this.position = position;
        this.weight = weight;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.phiDirection = phiDirection;
        this.thetaVisionDirection = thetaVisionDirection;
        this.visionRadius = visionRadius;

        // Initialize hitbox
        this.hitbox = new Point[4];
        updateHitbox();
    }

    // Constructor 3
    public BaseOrganism(Point position, double weight, double maxEnergy,
                        double phiDirection, double[] thetaVisionDirection, double visionRadius,
                        int age, int maxAge, boolean isAlive,
                        double energyNeededToReproduce, double weightNeededToReproduce) {
        this.position = position;
        this.weight = weight;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.phiDirection = phiDirection;
        this.thetaVisionDirection = thetaVisionDirection;
        this.visionRadius = visionRadius;
        this.age = age;
        this.maxAge = maxAge;
        this.isAlive = isAlive;
        this.energyNeededToReproduce = energyNeededToReproduce;
        this.weightNeededToReproduce = weightNeededToReproduce;

        // Initialize hitbox
        this.hitbox = new Point[4];
        updateHitbox();
    }

    // Additional methods for the organism's behavior could be added here

    // Example method to simulate the organism consuming energy
    public void consumeEnergy(double amount) {
        energy += amount;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    // Example method to simulate the organism losing energy
    public void loseEnergy(double amount) {
        energy -= amount;
        if (energy < 0) {
            energy = 0;
        }
    }

    //TODO add more methods





    // Getter methods
    public Point getPosition() {
        return position;
    }

    public double getWeight() {
        return weight;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    // Setter methods
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setMaxEnergy(double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    // Getter and setter methods for vision attributes
    public double getPhiDirection() {
        return phiDirection;
    }

    public void setPhiDirection(double phiDirection) {
        this.phiDirection = phiDirection;
    }

    public double[] getthetaVisionDirection() {
        return thetaVisionDirection;
    }

    public void setthetaVisionDirection(double[] thetaVisionDirection) {
        this.thetaVisionDirection = thetaVisionDirection;
    }

    public double getVisionRadius() {
        return visionRadius;
    }

    public void setVisionRadius(double visionRadius) {
        this.visionRadius = visionRadius;
    }

    public double getDeltaDirection() {
        return deltaDirection;
    }

    public void setDeltaDirection(double deltaDirection) {
        this.deltaDirection = deltaDirection;
    }

    // Getter and setter methods for reproduction attributes
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public double getEnergyNeededToReproduce() {
        return energyNeededToReproduce;
    }

    public void setEnergyNeededToReproduce(double energyNeededToReproduce) {
        this.energyNeededToReproduce = energyNeededToReproduce;
    }

    public double getWeightNeededToReproduce() {
        return weightNeededToReproduce;
    }

    public void setWeightNeededToReproduce(double weightNeededToReproduce) {
        this.weightNeededToReproduce = weightNeededToReproduce;
    }



    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        updateHitbox();
    }

    // Method to update hitbox based on position and size
    public void updateHitbox() {
        double hitboxSize = size;

        hitbox[0] = new Point(position.xCoord - hitboxSize, position.yCoord - hitboxSize);
        hitbox[1] = new Point(position.xCoord + hitboxSize, position.yCoord - hitboxSize);
        hitbox[2] = new Point(position.xCoord - hitboxSize, position.yCoord + hitboxSize);
        hitbox[3] = new Point(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
    }
}

