package main.java.controller;

import java.util.ArrayList;
import java.util.Iterator;

import main.java.model.BaseOrganism;
import main.java.model.WorldModel;
import main.java.view.MainWindow;
import main.java.view.WorldView;

public class WorldController {
    private WorldModel worldModel;
    private WorldView worldView;
    private MainWindow mainWindow;

    // Constructor
    public WorldController(WorldModel worldModel, WorldView worldView) {
        this.worldModel = worldModel;
        this.worldView = worldView;
    }

    /*
     * Methods for updating world
     */

    public void updateWorld(double deltaTime) {

        // Update the AI
        Iterator<BaseOrganism> organismAiIterator = worldModel.getOrganisms().iterator();
        while (organismAiIterator.hasNext()){
            BaseOrganism organism = organismAiIterator.next();
            organism.ai.update();
        }

        Iterator<BaseOrganism> resyncIterator = worldModel.getOrganisms().iterator();
        while (resyncIterator.hasNext()){
            BaseOrganism organism = resyncIterator.next();
            while(organism.aiIsCalculating) {
                // Do nothing
                // This stops the main thread and waits for the organism to finish calculating their next move
                // TODO find a better way to pause for multithreading
            }
        }
        
        // Update the positions of the organisms in the world based on the output of their AI
        Iterator<BaseOrganism> organismPositionIterator = worldModel.getOrganisms().iterator();
        while (organismPositionIterator.hasNext()){
            BaseOrganism organism = organismPositionIterator.next();
            organism.ai.handleMoving();
        }

        // Handle eating, energy depletion,, reproduction, and death
        Iterator<BaseOrganism> judgementDay = worldModel.getOrganisms().iterator();
        while (judgementDay.hasNext()){
            BaseOrganism organism = judgementDay.next();
            // handle eating
            // handle energy depletion
            // handle reproduction
            // handle death
        }


        // create more food
    }


    /*
     * Methods for calculating the organisms and interractions in the world
     */




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
