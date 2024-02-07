package main.java.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.model.BaseOrganism;
import main.java.model.WorldModel;

public class AI {
    protected final WorldModel model;
    protected final BaseOrganism organismSelf;
    protected final ExecutorService executor;

    // Constructor
    public AI(WorldModel model, BaseOrganism organism) {
        this.model = model;
        this.organismSelf = organism;
        this.executor = Executors.newFixedThreadPool(1); // Adjust the pool size as needed
    }

    public void update() {
        // Calculate inputs through a neural network using multithreading
        organismSelf.aiIsCalculating = true;
        executor.submit(() -> {
            takeInputs();
            processNeuralNetwork();
            organismSelf.aiIsCalculating = false; 
            //TODO organisms must not move before all AI calculations for all organisms are done
            //handleMoving();
        });
    }

    private void takeInputs() {
        // Calculate what the organism can see
        // This method can be executed in a separate thread
    }

    private void processNeuralNetwork() {
        // Compute the data in a neural network to determine the organism's behavior
        // This method can be executed in a separate thread

        //Should modify the deltaDirection and the velocity of the organism
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

    public void shutdown() {
        executor.shutdown();
    }
}
