package main.java.statistics;


import java.util.ArrayList;
import java.util.List;

import main.java.model.WorldModel;

public class Statistics {
    private final WorldModel worldModel;
    private final List<LogElement> log;
    private double lastTimeUpdated = 0;

    public Statistics(WorldModel worldModel) {
        this.worldModel = worldModel;
        log = new ArrayList<>();
    }

    public void update(double deltaTime) {
        lastTimeUpdated += deltaTime;

        if (lastTimeUpdated >= 1) {
            lastTimeUpdated = 0;

            //add logElement to log
        }
    }

    public void update(int ticks) {

    }

    public void recordNewStatElement(int seconds){

    }



    public void print() { //prints certain log data to the consol. Useful for testing
        
    }

}
