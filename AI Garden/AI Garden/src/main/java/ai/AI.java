package main.java.ai;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;

import java.util.List;
import java.util.ArrayList;

public class AI {
    public final WorldModel model;
    private BaseOrganism organismSelf;

    public NeuralNetwork neuralNetwork;
    protected double[] inputs;
    public final int numInputsPerVisionLine; // The number of input nodes that each vision line occupies. Should be 2
    public final int numOutputs;           // The number of outputs expected from the neural network. Should be 2
        // Inputs: each vision line inputs a value for the identity of the item and its distance from the organism, so two input nodes
        // Outputs: the outputs expected from this AI are a deltaDirection (-1 .. 1) and a velocity (0 .. 1)
    public final int numExtraInputs; //things like the weight and energy of the organism that provide it with self awareness

    // Constructor
    public AI(WorldModel model, BaseOrganism organism) {
        this.model = model;
        this.organismSelf = organism;
        numInputsPerVisionLine = 2;
        numExtraInputs = 2;
        numOutputs = 2;
        initializeNeuralNetwork();
    }

    //Constructor for building an AI object from another AI
    public AI(AI ai) {
        this.model = ai.model;
        this.organismSelf = ai.organismSelf;
        numInputsPerVisionLine = ai.numInputsPerVisionLine;
        numExtraInputs = ai.numExtraInputs;
        numOutputs = ai.numOutputs;
        neuralNetwork = new NeuralNetwork(ai.neuralNetwork);
    }

    protected void initializeNeuralNetwork(){
        int i = organismSelf.visionPoints.length * numInputsPerVisionLine + numExtraInputs;

        if (i < 2 + numExtraInputs) {
            throw new IllegalArgumentException("Organism does not have enough vision points to initialize neural network. ");
        }

        if (i > 24)     {neuralNetwork = new NeuralNetwork(i, i, i-8, i-16, numOutputs);}
        else if (i > 10) {neuralNetwork = new NeuralNetwork(i, 12, 8, 4, numOutputs);} 
        else if (i > 6) {neuralNetwork = new NeuralNetwork(i, 4, 3, numOutputs);}
        else            {neuralNetwork = new NeuralNetwork(i, i, numOutputs);}

        inputs = new double[i];
    }

    // Calculate inputs and pass them through a neural network
    public void update() {
        takeInputs();
        processNeuralNetwork();
    }

    private void takeInputs() {
        // TODO add better way of taking inputs
        inputs[inputs.length - 1] = organismSelf.energy;
        inputs[inputs.length - 2] = organismSelf.weight;

        // Calculates what the organism can see, based on its vision lines and the hitboxes of other entities. These hitboxes must be square.
        double x1, x2, y1, y2, a, b, m, n, x, y, slope, intercept;
        x1 = organismSelf.position.xCoord;
        y1 = organismSelf.position.yCoord;

        //TODO, create a list of all food and organisms in sight range, then use that list to check food. It should be much cheaper than the method below which checks every organism and food for each vision line
        List<BaseOrganism> organismsInSight = new ArrayList<>();
        List<Food> foodInSight = new ArrayList<>();
        
        double minX, maxX, minY, maxY;
        minX = organismSelf.position.xCoord;
        maxX = minX;
        minY = organismSelf.position.yCoord;
        maxY = minY;
        for (int i = 1; i < organismSelf.visionPoints.length; i++) {
            double tempx = organismSelf.visionPoints[i].xCoord;
            double tempy = organismSelf.visionPoints[i].yCoord;
            if (tempx < minX) {minX = tempx;} 
            else if (tempx > maxX) {maxX = tempx;}
            if (tempy < minY) {minY = tempy;} 
            else if (tempy > maxY) {maxY = tempy;}
        }

        otherOrganismInSightLoop :
        for (BaseOrganism o : model.getOrganisms()) {
            if (o.equals(organismSelf)){continue otherOrganismInSightLoop;}
            a = o.hitbox[0].xCoord;
            m = o.hitbox[0].yCoord;
            b = o.hitbox[1].xCoord;
            n = o.hitbox[1].yCoord;
            if(outsideOfRange(minY, maxY, m, n) || outsideOfRange(minX, maxX, a, b)) {
                continue otherOrganismInSightLoop;
            }
            organismsInSight.add(o);
        }

        isFoodInSightLoop :
        for (Food f : model.getFoods()) {
            a = f.hitbox[0].xCoord;
            m = f.hitbox[0].yCoord;
            b = f.hitbox[1].xCoord;
            n = f.hitbox[1].yCoord;
            if(outsideOfRange(minY, maxY, m, n) || outsideOfRange(minX, maxX, a, b)) {
                continue isFoodInSightLoop;
            }
            foodInSight.add(f);
        }



        for (int i = 0; i < organismSelf.visionPoints.length; i++){
            for (int j = 0; j < numInputsPerVisionLine; j++) {
                inputs[i * numInputsPerVisionLine + j] = 0.0;
            } // sets the input to 0.0 at the start
            x2 = organismSelf.visionPoints[i].xCoord;
            y2 = organismSelf.visionPoints[i].yCoord;

            // check all organisms in sight range for intersections
            organismDetectionLoop : 
            for (BaseOrganism o : organismsInSight) {
                if (o.equals(organismSelf)){continue organismDetectionLoop;}

                a = o.hitbox[0].xCoord;
                m = o.hitbox[0].yCoord;
                b = o.hitbox[1].xCoord;
                n = o.hitbox[1].yCoord;
 
                // checking for vertical lines because you can't divide by 0
                if (isBetween(x1, a, b) && isBetween(x2, a, b)) {
                    if (isBetween(n, y1, y2) || isBetween(m, y1, y2)) {
                        detected(i, o); //organism has been detected
                    }
                    continue organismDetectionLoop;
                }
 
                slope = (y2 - y1) / (x2 - x1);
                intercept = y1 - slope * x1;
 
                //checking the first vertical line hitbox
                if (isBetween(a, x1, x2)) {
                    y = slope * a + intercept;
                    if (isBetween(y, m, n)) {
                        detected(i, o);
                        continue organismDetectionLoop;
                    }
                }
                //checking the first horizontal line hitbox
                if (isBetween(m, y1, y2)) {
                    x = (m - intercept) / slope;
                    if (isBetween(x, a, b)) {
                        detected(i, o);
                        continue organismDetectionLoop;
                    }
                }
                //checking the second vertical line hitbox
                if (isBetween(b, x1, y1)) {
                    y = slope * b + intercept;
                    if (isBetween(y, m, n)) {
                        detected(i, o);
                        continue organismDetectionLoop;
                    }
                }
                //checking the second horizontal line hitbox
                if (isBetween(n, y1, y2)) {
                    x = (n - intercept) / slope;
                    if (isBetween(x, a, b)) {
                        detected(i, o);
                        continue organismDetectionLoop;
                    }
                }
            }
        
            // check all food in the sight range for intersections
            foodDetectionLoop :
            for (Food f : foodInSight) {
                a = f.hitbox[0].xCoord;
                m = f.hitbox[0].yCoord;
                b = f.hitbox[1].xCoord;
                n = f.hitbox[1].yCoord;

                // checking for vertical lines because you can't divide by 0
                if (isBetween(x1, a, b) && isBetween(x2, a, b)) {
                    if (isBetween(n, y1, y2) || isBetween(m, y1, y2)) {
                        detected(i, f); //food has been detected
                    }
                    continue foodDetectionLoop;
                }

                slope = (y2 - y1) / (x2 - x1);
                intercept = y1 - slope * x1;

                if (isBetween(a, x1, x2)) {
                    y = slope * a + intercept;
                    if (isBetween(y, m, n)) {
                        detected(i, f);
                        continue foodDetectionLoop;
                    }
                }
                if (isBetween(m, y1, y2)) {
                    x = (m - intercept) / slope;
                    if (isBetween(x, a, b)) {
                        detected(i, f);
                        continue foodDetectionLoop;
                    }
                }
                if (isBetween(b, x1, y1)) {
                    y = slope * b + intercept;
                    if (isBetween(y, m, n)) {
                        detected(i, f);
                        continue foodDetectionLoop;
                    }
                }
                if (isBetween(n, y1, y2)) {
                    x = (n - intercept) / slope;
                    if (isBetween(x, a, b)) {
                        detected(i, f);
                        continue foodDetectionLoop;
                    }
                }
            }
        }
    }

    // Check if both values are either greater than both endpoints or lesser than both endpoints
    public static boolean outsideOfRange(double val1, double val2, double end1, double end2) {
        boolean condition1 = (val1 < Math.min(end1, end2)) && (val2 < Math.min(end1, end2));
        boolean condition2 = (val1 > Math.max(end1, end2)) && (val2 > Math.max(end1, end2));
        return condition1 || condition2;
    }

    // Check if the value is between the two endpoints inclusively
    public static boolean isBetween(double valueToCheck, double endPoint1, double endPoint2) {
        return (valueToCheck >= Math.min(endPoint1, endPoint2)) && (valueToCheck <= Math.max(endPoint1, endPoint2));
    }

    // TODO create entity class to hold all entities with their hitbox data so that the following detected() methods can be simplified. (optional)
    
    //calculate distance and prepare values inside the inputs[] for the neural network
    private void detected(int visionLineIndex, Food f) {
        int index = visionLineIndex * numInputsPerVisionLine;
        double distance = organismSelf.visionRadius - organismSelf.position.distanceTo(f.position); //numbers get bigger the closer it gets, which may be easier for the AI to interpret
        if (inputs[index] == 0 || inputs[index] > distance) {
            inputs[index] = distance;
            inputs[index + 1] = 1.0; //f.value; //TODO test if f.value or 1.0 works better as inputs
            return;
        }
    }

    //calculate distance and prepare values inside the inputs[] for the neural network
    private void detected(int visionLineIndex, BaseOrganism o) {
        int index = visionLineIndex * numInputsPerVisionLine;
        double distance = organismSelf.visionRadius - organismSelf.position.distanceTo(o.position);
        if (inputs[index] == 0 || inputs[index] > distance) {
            inputs[index] = distance;
            inputs[index + 1] = -1.0; //TODO change input to reflect the organism's type, such as herbivore (0.1) vs predatore (-1.0), or its color values
            return;
        }
    }

    // Compute the data in a neural network to determine the organism's behavior
    private void processNeuralNetwork() {
        double[] outputs = neuralNetwork.process(inputs);
        // This should only modify the deltaDirection and the velocity of the organism
        // the output is 0..1 and must be transformed into -1..1
        organismSelf.deltaDirection = outputs[0] * 2.0 - 1.0; //(-1..1) can rotate left or right
        organismSelf.velocity = outputs[1]; //(0..1) can only move forward

    }
    
    // Calculates the new position of the creature in the world based on the velocity and direction it is facing
    public void handleMoving() {
        //update the direction based on the deltaDirection (a value of -1 to 1) which is decided by the AI
        organismSelf.thetaDirection += organismSelf.deltaDirection * organismSelf.maxDeltaDirection;
        if(organismSelf.thetaDirection > 360 || organismSelf.thetaDirection < -360){organismSelf.thetaDirection /= 360;}

        //updates the x y position from the maxVelocity * velocity (a value of -1 to 1) which is decided by the AI
        double deltaX = Math.cos(Math.toRadians(organismSelf.thetaDirection)) * organismSelf.velocity * organismSelf.maxVelocity;
        double deltaY = Math.sin(Math.toRadians(organismSelf.thetaDirection)) * organismSelf.velocity * organismSelf.maxVelocity;

        organismSelf.position.xCoord += deltaX;
        organismSelf.position.yCoord += deltaY;
        organismSelf.updateHitbox();
        organismSelf.updateVisionPoints();
    }

    /**
     * Creates an copy of the AI and neural network within without mutating it.
     */
    public AI asexualCrossover(){
        NeuralNetwork n = new NeuralNetwork(neuralNetwork);
        AI a = new AI(this);
        a.setNeuralNetwork(n);
        return a;
    }

    /**
     * Creates a new AI with a new neural network, half copied from this AI and the other half copied from the otherAI.
     * This does not mutate anything.
     */
    public AI crossover(AI otherAI) {
        NeuralNetwork n = neuralNetwork.crossover(otherAI.neuralNetwork);
        AI a = new AI(this);
        a.setNeuralNetwork(n);
        return a;
    }

    // Applies the mutations to the weights and biases of the neural network as defined in the WorldModel
    public void mutate(){
        neuralNetwork.mutate(model.mutationRate, model.mutationStrength);
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
    }

    public void resetNumInputs(){
        int numInputs = organismSelf.visionPoints.length * numInputsPerVisionLine + numExtraInputs;
        this.inputs = new double[numInputs];
    }

    public void setOrganismSelf(BaseOrganism organismSelf) {
        this.organismSelf = organismSelf;
    }


    public String getSerialization() {
        return neuralNetwork.getSerialization();
    }
}
