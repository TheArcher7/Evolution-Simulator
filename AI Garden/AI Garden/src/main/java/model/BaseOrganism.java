package main.java.model;

import main.java.ai.AI;
import main.java.ai.NeuralNetwork;
import main.java.util.Pos;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
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
    public int r = 255, g = 255, b = 255;
    public Color color = new Color(0, 0, 255);
    public int numFoodEaten = 0;
    public double energySpentSinceLastEating = 0;
    public ArrayList<Double> energySpendingLog = new ArrayList<>(); //TODO, record energy efficiencies so that an average efficiency can be found
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
        initVision(); //Initialize without an AI
    }
    
    // Additional methods for the organism's behavior could be added here

    public void initializeStandardOrganism(){
        deltaDirection = generateRandomDoubleInRange(-1, 1);
        velocity = generateRandomDoubleInRange(-1, 1);

        color = generateRandomColor();
        generation = 0;

        maxVelocity = 2.0;
        maxDeltaDirection = 3.0;
        weight = 300.0;
        maxEnergy = 3200.0;
        energy = maxEnergy / 2; // Start with half full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 3000.0;
        weightNeededToReproduce = 200.0;

        size = 4; // initializes a hitbox with a width and height of size*2 ('size' units away in each cardinal direction)
        thetaDirection = generateRandomDoubleInRange(0, 360);
        visionRadius = 80;
        phiVisionDirection = new double[]{50.0, 20.0, 0, -20.0, -50.0,};
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
        r = random.nextInt(255); //uses 230 rather than 256 to improve visibility
        g = random.nextInt(230);
        b = random.nextInt(255);
        return new Color(r, g, b);
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

        // Add a random value between -3 and 3 to each color component
        newborn.r = Math.max(0, Math.min(255, r + random.nextInt(9) - 3)); // Ensure the value stays within [0, 255]
        newborn.g = Math.max(0, Math.min(230, g + random.nextInt(9) - 3)); // Ensure the value stays within [0, 230]
        newborn.b = Math.max(0, Math.min(255, b + random.nextInt(9) - 3)); // Ensure the value stays within [0, 255]
        newborn.color = new Color(newborn.r, newborn.g, newborn.b);

        
        if (WorldModel.LEARN_ENABLED) {
            // combine the AI of both parents to create new AI for child, mutates the new AI, and sets it for the child
            //AI newbornAI = ai.asexualCrossover(); //ai.crossover(otherParent.ai);
            //newbornAI.mutate();
            //newbornAI.setOrganismSelf(newborn);
            //newborn.ai = newbornAI;

            //NeuralNetwork nn = ai.neuralNetwork.crossover(otherParent.ai.neuralNetwork); //FIXME for some reason, doesn't work
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
    public double getAverageEnergySpent(){
        if (energySpendingLog.size() == 0) {return 0.0;}
        Double sum = 0.0;
        for (Double d : energySpendingLog){
            sum += d;
        }
        double average = sum / energySpendingLog.size();
        return average;
    }

}
