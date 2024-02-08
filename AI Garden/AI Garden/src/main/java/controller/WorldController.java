package main.java.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.model.BaseOrganism;
import main.java.model.WorldModel;
import main.java.view.MainWindow;
import main.java.view.WorldView;

public class WorldController {
    private WorldModel worldModel;
    private WorldView worldView;
    private MainWindow mainWindow;
    private ExecutorService executor;

    // Constructor
    public WorldController(WorldModel worldModel, WorldView worldView) {
        this.worldModel = worldModel;
        this.worldView = worldView;
        this.executor = Executors.newCachedThreadPool(); // Adjust the thread pool as needed
    }

    /*
     * Methods for updating world
     */

    public void updateWorld(double deltaTime) {
        updateAI();
        moveOrganisms();

        // TODO Handle eating, energy depletion,, reproduction, and death
        Iterator<BaseOrganism> judgementDay = worldModel.getOrganisms().iterator();
        while (judgementDay.hasNext()){
            BaseOrganism organism = judgementDay.next();
            // handle eating
            // handle energy depletion
            // handle reproduction
            // handle death
        }

        // TODO create more food
    }


    /*
     * Methods for calculating the organisms and interractions in the world
     */

     public void updateAI() {
        // Create a countdown latch to synchronize AI calculations
        CountDownLatch aiCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // Update the AI
        Iterator<BaseOrganism> organismAiIterator = worldModel.getOrganisms().iterator();
        while (organismAiIterator.hasNext()){
            BaseOrganism organism = organismAiIterator.next();
            
            executor.submit(() -> {
                organism.ai.update();
                aiCalculationLatch.countDown(); // Signal completion of AI calculation
            });
        }

        // Wait for all AI calculations to complete
        try {
            aiCalculationLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
     }

     public void moveOrganisms(){
        // Create a countdown latch to synchronize the movement calculations
        CountDownLatch movementCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // Update the positions of the organisms in the world based on the output of their AI
        Iterator<BaseOrganism> organismPositionIterator = worldModel.getOrganisms().iterator();
        while (organismPositionIterator.hasNext()){
            BaseOrganism organism = organismPositionIterator.next();

            executor.submit(() -> {
                organism.ai.handleMoving();
                movementCalculationLatch.countDown();
            });
        }

        // Wait for all movement calculations to complete
        try {
            movementCalculationLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
     }


     // Shutdown the executor service when it's no longer needed
    public void shutdown() {
        executor.shutdown();
    }


    /*
     * Methods for rendering world
     */

    public void changeZoomFactor(int zoomChange, boolean relative) {
        int zoomFactor = worldView.getZoomFactor();

        if (zoomFactor + zoomChange > 0) {
            worldView.setZoomFactor(zoomFactor + zoomChange);

            if (relative) {
                if (zoomChange > 0) {
                    worldView.setOffsetX(worldView.getOffsetX() + (int) ((worldView.getSize().width) * 0.8) / 2);
                    worldView.setOffsetY(worldView.getOffsetY() + worldView.getSize().height / 2);
                } else {
                    worldView.setOffsetX(worldView.getOffsetX() - (int) ((worldView.getSize().width) * 0.8) / 2);
                    worldView.setOffsetY(worldView.getOffsetY() - worldView.getSize().height / 2);
                }
            }
        }
    }

    public void changeOffsetX(int offsetChange) {
        int offsetX = worldView.getOffsetX();
        worldView.setOffsetX(offsetX + offsetChange);
    }

    public void changeOffsetY(int offsetChange) {
        int offsetY = worldView.getOffsetY();
        worldView.setOffsetY(offsetY + offsetChange);
    }

    public void changeSpeed(double speedChange) {
        double change = WorldModel.speedFactor + speedChange;
        setSpeed(change);
    }
    
    public void setSpeed(double speed) {
        if (speed >= 0 && speed <= 15) {
            WorldModel.speedFactor = speed;
            //mainWindow.setSpeedSlider(speed);
        }
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setViewInputFocus() {
        mainWindow.setViewInputFocus();
    }
}
