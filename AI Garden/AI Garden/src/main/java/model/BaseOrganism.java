package main.java.model;

import main.java.ai.AI;
import main.java.util.Pos;

import java.awt.Color;
import java.util.Random;


public class BaseOrganism {
    // Attributes for Life
    public Pos position;
    public double weight;
    public double energy;
    public double maxEnergy;
    public int age;
    public int generation; //0 means original, 1 means the offspring of the original, 2 means the offspring of the offspring, and so on...
    public int maxAge;
    public boolean isAlive;
    public double energyNeededToReproduce;
    public double weightNeededToReproduce;

    // Attributes for AI
    public AI ai;
    public Boolean aiIsCalculating = false; //boolean used for concurrency
    public double velocity; // a value between -1 and 1. An output from the AI
    public double maxVelocity; //a value determining the creature's maximum velocity
    public double thetaDirection = 0; // direction in degrees the creature is facing
    public double deltaDirection = 0; // the change of direction of the creature. Between -1 and 1. An output from the AI
    public double[] phiVisionDirection; // the offset in degrees from the thetaDirection for each vision line
    public double visionRadius; // the distance the vision lines extend from the organism
    public Pos[] hitbox;
    public Pos[] visionPoints;

    // Attributes for graphics and data visualization
    public double size = 1;
    public int r = 256, g = 256, b = 256;
    public Color color = new Color(0, 0, 255);


    // Constructors

    // Constructor for a simple organism that is useful for testing purposes
    public BaseOrganism(Pos position, WorldModel model) {
        this.position = position;
        deltaDirection = generateRandomDoubleInRange(-1, 1);
        velocity = generateRandomDoubleInRange(-1, 1);

        color = generateRandomColor();
        generation = 0;

        maxVelocity = 3;
        weight = 30;
        maxEnergy = 120;
        energy = maxEnergy; // Start with full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 100;
        weightNeededToReproduce = 20;

        size = 4;
        thetaDirection = generateRandomDoubleInRange(0, 360);
        visionRadius = 60;
        phiVisionDirection = new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0};
        initVision(model);
    }

    
    @Deprecated
    public BaseOrganism(Pos position) {
        this.position = position;
        weight = 30;
        maxEnergy = 120;
        energy = maxEnergy;// Start with full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 100;
        weightNeededToReproduce = 20;

        size = weight / 3;
        thetaDirection = 90;
        visionRadius = 60;
        phiVisionDirection = new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0};
        initVision();
    }

    @Deprecated
    public BaseOrganism(Pos position, double weight, double maxEnergy,
                        double thetaDirection, double[] phiVisionDirection, double visionRadius) {
        this.position = position;
        this.weight = weight;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.thetaDirection = thetaDirection;
        this.phiVisionDirection = phiVisionDirection;
        this.visionRadius = visionRadius;

        initVision();
    }

    // Constructor 3
    @Deprecated
    public BaseOrganism(Pos position, double weight, double maxEnergy,
                        double thetaDirection, double[] phiVisionDirection, double visionRadius,
                        int age, int maxAge, boolean isAlive,
                        double energyNeededToReproduce, double weightNeededToReproduce) {
        this.position = position;
        this.weight = weight;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.thetaDirection = thetaDirection;
        this.phiVisionDirection = phiVisionDirection;
        this.visionRadius = visionRadius;
        this.age = age;
        this.maxAge = maxAge;
        this.isAlive = isAlive;
        this.energyNeededToReproduce = energyNeededToReproduce;
        this.weightNeededToReproduce = weightNeededToReproduce;

        initVision();
    }

    // Constructor used for testing vision and rendering
    @Deprecated
    public BaseOrganism(Pos position, double thetaDirection, double[] phiVisionDirection, double visionRadius, double size){
            this.position = position;
            this.thetaDirection = thetaDirection;
            this.phiVisionDirection = phiVisionDirection;
            this.visionRadius = visionRadius;
            this.size = size;

            initVision();
        }

    // Additional methods for the organism's behavior could be added here

    private void initVision() {
        // Initialize hitbox
        this.hitbox = new Pos[2];
        updateHitbox();

        // Initialize visionPoints
        this.visionPoints = new Pos[phiVisionDirection.length];
        updateVisionPoints();
    }

    private void initVision(WorldModel model) {
        // Initialize hitbox
        this.hitbox = new Pos[2];
        updateHitbox();

        // Initialize visionPoints
        this.visionPoints = new Pos[phiVisionDirection.length];
        updateVisionPoints();

        // Initialize the AI
        ai = new AI(model, this);
    }

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

    public void changeDirection(double speedOfChange){
        if (speedOfChange <= 0) {speedOfChange = 1;}
        thetaDirection += (deltaDirection * speedOfChange);
        updateVisionPoints();
    }

    // Method to update hitbox based on position and size
    public void updateHitbox() {
        double hitboxSize = size;

        hitbox[0] = new Pos(position.xCoord - hitboxSize, position.yCoord - hitboxSize);
        hitbox[1] = new Pos(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
    }

    // Method to update visionPoints based on position, visionRadius, and phiVisionDirection
    public void updateVisionPoints() {
        for (int i = 0; i < phiVisionDirection.length; i++) {
            double phi = Math.toRadians(thetaDirection - phiVisionDirection[i]);

            double xCoord = position.xCoord + (visionRadius * Math.cos(phi));
            double yCoord = position.yCoord + (visionRadius * Math.sin(phi));

            visionPoints[i] = new Pos(xCoord, yCoord);
        }
    }

    // Method to generate a random color
    private Color generateRandomColor() {
        Random random = new Random();
        r = random.nextInt(256);
        g = random.nextInt(256);
        b = random.nextInt(256);
        return new Color(r, b, b);
    }

    // Method to generate a random double within a specified range
    private double generateRandomDoubleInRange(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }


    //TODO add methods for toString() equals() and more




    //Getter and Setter methods
    @Deprecated
    public void setAI(AI ai) {
        this.ai = ai;
    }

    // Getter methods
    @Deprecated
    public Pos getPosition() {
        return position;
    }

    @Deprecated
    public double getWeight() {
        return weight;
    }

    @Deprecated
    public double getEnergy() {
        return energy;
    }

    @Deprecated
    public double getMaxEnergy() {
        return maxEnergy;
    }

    // Setter methods
    @Deprecated
    public void setPosition(Pos position) {
        this.position = position;
    }

    @Deprecated
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Deprecated
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    @Deprecated
    public void setMaxEnergy(double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    // Getter and setter methods for vision attributes
    @Deprecated
    public double getThetaDirection() {
        return thetaDirection;
    }

    @Deprecated
    public void setThetaDirection(double thetaDirection) {
        this.thetaDirection = thetaDirection;
    }

    @Deprecated
    public double[] getPhiVisionDirection() {
        return phiVisionDirection;
    }

    @Deprecated
    public void setPhiVisionDirection(double[] phiVisionDirection) {
        this.phiVisionDirection = phiVisionDirection;
    }

    @Deprecated
    public double getVisionRadius() {
        return visionRadius;
    }

    @Deprecated
    public void setVisionRadius(double visionRadius) {
        this.visionRadius = visionRadius;
    }

    @Deprecated
    public double getDeltaDirection() {
        return deltaDirection;
    }

    @Deprecated
    public void setDeltaDirection(double deltaDirection) {
        this.deltaDirection = deltaDirection;
    }

    // Getter and setter methods for reproduction attributes
    @Deprecated
    public int getAge() {
        return age;
    }

    @Deprecated
    public void setAge(int age) {
        this.age = age;
    }

    @Deprecated
    public int getMaxAge() {
        return maxAge;
    }

    @Deprecated
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Deprecated
    public boolean isAlive() {
        return isAlive;
    }

    @Deprecated
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Deprecated
    public double getEnergyNeededToReproduce() {
        return energyNeededToReproduce;
    }

    @Deprecated
    public void setEnergyNeededToReproduce(double energyNeededToReproduce) {
        this.energyNeededToReproduce = energyNeededToReproduce;
    }

    @Deprecated
    public double getWeightNeededToReproduce() {
        return weightNeededToReproduce;
    }

    @Deprecated
    public void setWeightNeededToReproduce(double weightNeededToReproduce) {
        this.weightNeededToReproduce = weightNeededToReproduce;
    }

    @Deprecated
    public double getSize() {
        return size;
    }

    @Deprecated
    public void setSize(double size) {
        this.size = size;
        updateHitbox();
    }

    @Deprecated
    public Pos[] getHitbox() {
        return hitbox;
    }

    @Deprecated
    public Pos[] getVisionPoints() {
        return visionPoints;
    }

}
