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

    public void updateWorld(double delta) {
        Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
        while (organismIterator.hasNext()){
            BaseOrganism organism = organismIterator.next();
        //TODO Implement all update world methods
        // Loop through all organisms
        // update their positions
        // calculate their vision
        // send inputs to AI
        // handle eating
        // handle reproduction
        // handle energy depletion
        // handle death
        }

        // create more food
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
