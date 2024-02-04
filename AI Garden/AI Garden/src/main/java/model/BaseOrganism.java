package main.java.model;

import main.java.util.Pos;


public class BaseOrganism {
    // Attributes
    private Pos position;
    private double weight;
    private double energy;
    private double maxEnergy;
    private double velocity; // a value between -1 and 1. An output from the AI
    private double maxVelocity; //a value determining the creature's maximum velocity

    // Attributes for Life
    private int age;
    private int maxAge;
    private boolean isAlive;
    private double energyNeededToReproduce;
    private double weightNeededToReproduce;

    // Attributes for vision
    private double thetaDirection; // direction in degrees the creature is facing
    private double deltaDirection = 0; // the change of direction of the creature. Between -1 and 1. An output from the AI
    private double[] phiVisionDirection; // the offset in degrees from the thetaDirection for each vision line
    private double visionRadius; // the distance the vision lines extend from the organism
    private double size;
    private Pos[] hitbox;
    private Pos[] visionPoints;

    // Constructor
    public BaseOrganism(Pos position) {
        this.position = position;
        weight = 12;
        maxEnergy = 120;
        energy = maxEnergy;// Start with full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 50;
        weightNeededToReproduce = 20;

        size = weight / 3;
        thetaDirection = 90;
        visionRadius = 3;
        phiVisionDirection = new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0};
        initVision();
    }

    // Constructor 2
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
        this.hitbox = new Pos[4];
        updateHitbox();

        // Initialize visionPoints
        this.visionPoints = new Pos[phiVisionDirection.length];
        updateVisionPoints();
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
        hitbox[1] = new Pos(position.xCoord + hitboxSize, position.yCoord - hitboxSize);
        hitbox[2] = new Pos(position.xCoord - hitboxSize, position.yCoord + hitboxSize);
        hitbox[3] = new Pos(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
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


    //TODO add methods for toString() equals() and more





    // Getter methods
    public Pos getPosition() {
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
    public void setPosition(Pos position) {
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
    public double getThetaDirection() {
        return thetaDirection;
    }

    public void setThetaDirection(double thetaDirection) {
        this.thetaDirection = thetaDirection;
    }

    public double[] getPhiVisionDirection() {
        return phiVisionDirection;
    }

    public void setPhiVisionDirection(double[] phiVisionDirection) {
        this.phiVisionDirection = phiVisionDirection;
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

    public Pos[] getHitbox() {
        return hitbox;
    }

    public Pos[] getVisionPoints() {
        return visionPoints;
    }

}


