package main.java.model;

import main.java.ai.AI;
import main.java.ai.NeuralNetwork;
import main.java.util.Pos;

import java.awt.Color;
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

    public boolean isBestOrganism = false;

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

    public boolean gluttony = false;//ate the most food
    public boolean lust = false;    //most children
    public boolean sloth = false;   //most efficient traveling
    public boolean pride = false;   //oldest
    public boolean greed = false;   //item gathering not implemented
    public boolean wrath = false;   //murder not implemented
    public boolean envy = false;    //oldest generation


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
        velocity = 0;

        color = generateRandomColor();
        generation = 0;

        maxVelocity = 2.0;
        maxDeltaDirection = 6.0;
        weight = 400.0;
        maxEnergy = 6200.0;
        energy = maxEnergy / 2; // Start with half full energy
        age = 0;
        maxAge = 120;
        isAlive = true;
        energyNeededToReproduce = 3000.0;
        weightNeededToReproduce = 200.0;

        size = 4; // initializes a hitbox with a width and height of size*2 ('size' units away in each cardinal direction)
        thetaDirection = generateRandomDoubleInRange(0, 360);
        visionRadius = 120;
        phiVisionDirection = new double[]{50.0, 30.0, 20.0, 10.0, 0, -10.0, -20.0, -30.0, -50.0,};
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

    @Deprecated
    public void changeDirection(double speedOfChange){
        if (speedOfChange <= 0) {speedOfChange = 1;}
        thetaDirection += (deltaDirection * speedOfChange);
        updateVisionPoints();
    }

    /**
     * Method to update hitbox based on position and size
     */
    public void updateHitbox() {
        double hitboxSize = size;
        hitbox[0] = new Pos(position.xCoord - hitboxSize, position.yCoord - hitboxSize);
        hitbox[1] = new Pos(position.xCoord + hitboxSize, position.yCoord + hitboxSize);
    }

    /**
     * Method to update visionPoints based on position, visionRadius, and phiVisionDirection
     */
    public void updateVisionPoints() {
        for (int i = 0; i < phiVisionDirection.length; i++) {
            double phi = Math.toRadians(thetaDirection - phiVisionDirection[i]);

            double xCoord = position.xCoord + (visionRadius * Math.cos(phi));
            double yCoord = position.yCoord + (visionRadius * Math.sin(phi));

            visionPoints[i] = new Pos(xCoord, yCoord);
        }
    }
    public void resetVisionPoints(){
        if (visionPoints.length != phiVisionDirection.length) {
            visionPoints = new Pos[phiVisionDirection.length];
            updateVisionPoints();
        }
    }

    // Method to generate a random color
    private Color generateRandomColor() {
        Random random = new Random();
        r = random.nextInt(255); //uses 230 rather than 255 to improve visibility
        g = random.nextInt(230);
        b = random.nextInt(255);
        return new Color(r, g, b);
    }

    // Method to generate a random double within a specified range
    private double generateRandomDoubleInRange(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    /**
     * Returns a BaseOrganism that can be considered to be a "child" 
     * between the owner organism of this method and another organism passed to it.
     * 
     * Gives the child slight mutations.
     */
    public BaseOrganism reproduce(BaseOrganism otherParent) {
        //TODO otherParent cannot be a child

        // set the attributes of the new child organism according to the primary parent
        Pos p = new Pos(position.xCoord, position.yCoord);
        BaseOrganism newborn = new BaseOrganism(p, ai.model);
        newborn.phiVisionDirection = this.phiVisionDirection;
        newborn.resetVisionPoints();
        newborn.generation = generation + 1;
        numChildren++;

        // mutate all inherent attributes not associated with the AI
        Random random = new Random();
        newborn.energy = energyNeededToReproduce;
        newborn.weight = weightNeededToReproduce / 2;

        //TODO more mutations

        // Add a random value between -3 and 3 to each color component
        newborn.r = Math.max(0, Math.min(255, r + random.nextInt(9) - 3)); // Ensure the value stays within [0, 255]
        newborn.g = Math.max(0, Math.min(230, g + random.nextInt(9) - 3)); // Ensure the value stays within [0, 230]
        newborn.b = Math.max(0, Math.min(255, b + random.nextInt(9) - 3)); // Ensure the value stays within [0, 255]
        newborn.color = new Color(newborn.r, newborn.g, newborn.b);

        // Mutate the AI
        if (WorldModel.LEARN_ENABLED) {
            // combine the AI of both parents to create new AI for child, mutates the new AI, and sets it for the child
            //AI newbornAI = ai.asexualCrossover(); //ai.crossover(otherParent.ai);
            //newbornAI.mutate();
            //newbornAI.setOrganismSelf(newborn);
            //newborn.ai = newbornAI;

            //NeuralNetwork nnn = ai.neuralNetwork.crossover(otherParent.ai.neuralNetwork); //FIXME doesn't work
            NeuralNetwork nn = new NeuralNetwork(ai.neuralNetwork);
            nn.mutate(ai.model.mutationRate, ai.model.mutationStrength);
            newborn.ai.setNeuralNetwork(nn);
            newborn.ai.resetNumInputs();
        }
        else {
            // Copies the AI and nueral network without mutating
            newborn.ai = ai.asexualCrossover();
            newborn.ai.setOrganismSelf(newborn);
        }
        
        recentChild = newborn;
        return newborn;
    }


    //TODO add methods for toString() equals() and more

    /**
     * Method for returning a string representation of the attributes of the world
     * to be saved in a text file.
     */
    public String getSerialization(String organismNameOrID){
        StringBuilder builder = new StringBuilder();
        builder.append("BaseOrganism " + organismNameOrID + System.lineSeparator());
        
        //append organism data
        String organismData = 
                "weight " + weight +
                " maxEnergy " + maxEnergy +
                " age " + age + " size " + size + " generation " + generation + 
                " energyNeededToReproduce " + energyNeededToReproduce + 
                " weightNeededToReproduce " + weightNeededToReproduce +
                " colorR " + r + " colorG " + g + " colorB " + b + 
                " maxVelocity " + maxVelocity + " maxDeltaDirection " + maxDeltaDirection +
                " visionRadius " + visionRadius + " phiVisionDirection[] " + phiVisionDirection.length;
        builder.append(organismData);
        for (double d : phiVisionDirection) {
            builder.append(" " + d);
        }
        builder.append(System.lineSeparator());

        //append ai data
        builder.append(ai.getSerialization());

        //append world data (some organism's adaptations may be specific to an environment)


        return builder.toString();
    }



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

    public void resetStatusToFalse() {
        isBestOrganism = false;

        gluttony = false;
        lust = false;
        sloth = false;
        pride = false;
        greed = false;
        wrath = false;
        envy = false;
    }

}
