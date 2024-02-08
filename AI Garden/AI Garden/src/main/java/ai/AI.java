package main.java.ai;

import main.java.model.BaseOrganism;
import main.java.model.WorldModel;

public class AI {
    protected final WorldModel model;
    protected final BaseOrganism organismSelf;

    // Constructor
    public AI(WorldModel model, BaseOrganism organism) {
        this.model = model;
        this.organismSelf = organism;
    }

    public void update() {
        // Calculate inputs through a neural network using multithreading
        takeInputs();
        processNeuralNetwork();
    }

    private void takeInputs() {
        // Calculate what the organism can see, based on its vision and the hitboxes of other entities
        
    }

    private void processNeuralNetwork() {
        // Compute the data in a neural network to determine the organism's behavior
        // This should only modify the deltaDirection and the velocity of the organism (both are values between -1 and 1)

    }
    
    public void handleMoving() {
        // Calculates the new position of the creature in the world based on the velocity and direction it is facing

        //update the direction based on the deltaDirection (a value of -1 to 1) which is decided by the AI
        organismSelf.thetaDirection = organismSelf.thetaDirection + organismSelf.deltaDirection;
        if(organismSelf.thetaDirection > 360 || organismSelf.thetaDirection < -360){organismSelf.thetaDirection /= 360;}

        //updates the x y position from the maxVelocity * velocity (a value of -1 to 1) which is decided by the AI
        double deltaX = Math.cos(Math.toRadians(organismSelf.thetaDirection)) * organismSelf.velocity * organismSelf.maxVelocity;
        double deltaY = Math.sin(Math.toRadians(organismSelf.thetaDirection)) * organismSelf.velocity * organismSelf.maxVelocity;

        organismSelf.position.xCoord += deltaX;
        organismSelf.position.yCoord += deltaY;
        organismSelf.updateHitbox();
        organismSelf.updateVisionPoints();
    }
}
