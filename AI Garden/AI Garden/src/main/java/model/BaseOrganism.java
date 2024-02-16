package main.java.model;

import main.java.ai.AI;
import main.java.ai.NeuralNetwork;
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
    public BaseOrganism recentChild;
    //public double energyGivenToNewborn;
    //public double weightGivenToNewborn;

    // Attributes for AI
    public AI ai;
    public double velocity; // a value between -1 and 1. An output from the AI
    public double maxVelocity; //a value determining the creature's maximum velocity
    public double thetaDirection = 0; // direction in degrees the creature is facing
    public double deltaDirection = 0; // the change of direction of the creature. Between -1 and 1. An output from the AI
    public double maxDeltaDirection = 0;
    public double[] phiVisionDirection; // the offset in degrees from the thetaDirection for each vision line
    public double visionRadius; // the distance the vision lines extend from the organism
    public Pos[] hitbox;
    public Pos[] visionPoints;

    // Attributes for graphics and statistics
    public double size = 1;
    public int r = 256, g = 256, b = 256;
    public Color color = new Color(0, 0, 255);
    public double energySpentSinceLastEating = 0;
    public double energyEfficiency = 0; // the amount of energy spent to reach food
    public int numChildren = 0;


    // Constructors

    // Constructor for a simple organism that is useful for testing purposes
    public BaseOrganism(Pos position, WorldModel model) {
        this.position = position;
        initializeStandardOrganism();
        initVision(model);
    }

    /*
     * A constructor to be used when creating children. You must manually assign an AI
     */
    public BaseOrganism(Pos position) {
        this.position = position;
        initializeStandardOrganism();
        initVision();
    }
    
    // Additional methods for the organism's behavior could be added here

    public void initializeStandardOrganism(){
        deltaDirection = generateRandomDoubleInRange(-1, 1);
        velocity = generateRandomDoubleInRange(-1, 1);

        color = generateRandomColor();
        generation = 0;

        maxVelocity = 2;
        maxDeltaDirection = 3;
        weight = 300;
        maxEnergy = 3200;
        energy = maxEnergy / 2; // Start with full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 3000;
        weightNeededToReproduce = 200;

        size = 4;
        thetaDirection = generateRandomDoubleInRange(0, 360);
        visionRadius = 60;
        phiVisionDirection = new double[]{160.0, 90.0, 50.0, 20.0, 0, -20.0, -50.0, -90.0, -160.0};
    }

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

    public BaseOrganism reproduce(BaseOrganism otherParent) {
        // set the attributes of the new child organism
        Pos p = new Pos(position.xCoord, position.yCoord);
        BaseOrganism newborn = new BaseOrganism(p, ai.model);
        newborn.generation = generation + 1;
        numChildren++;

        // mutate all inherent attributes not associated with the AI
        Random random = new Random();
        newborn.energy = energyNeededToReproduce;
        newborn.weight = weightNeededToReproduce / 2;
        //TODO mutate

        //FIXME for some reason, doesn't work
        if (WorldModel.LEARN_ENABLED) {
            // combine the AI of both parents to create new AI for child, mutates the new AI, and sets it for the child
            //AI newbornAI = ai.asexualCrossover(); //ai.crossover(otherParent.ai);
            //newbornAI.mutate();
            //newbornAI.setOrganismSelf(newborn);
            //newborn.ai = newbornAI;

            //NeuralNetwork nn = ai.neuralNetwork.crossover(otherParent.ai.neuralNetwork);
            NeuralNetwork nn = new NeuralNetwork(ai.neuralNetwork);
            nn.mutate(ai.model.mutationRate, ai.model.mutationStrength);
            newborn.ai.setNeuralNetwork(nn);
        }
        else {
            newborn.ai = ai.asexualCrossover();
            newborn.ai.setOrganismSelf(newborn);
        }
        
        
        recentChild = newborn;
        return newborn;
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
