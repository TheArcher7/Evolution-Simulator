package main.java.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;
import main.java.statistics.Statistics;
import main.java.util.Pos;
import main.java.view.MainWindow;
import main.java.view.WorldView;

public class WorldController {
    private WorldModel worldModel;
    private WorldView worldView;
    private MainWindow mainWindow;
    private ExecutorService executor;
    private Statistics statistics;

    private ArrayList<BaseOrganism> childrenToAddToTheSimulation = new ArrayList<>();

    public int ticks = 0;
    public int seconds = 0;
    public int minutes = 0;
    public int hours = 0;

    // Constructor
    public WorldController(WorldModel worldModel, WorldView worldView, Statistics statistics) {
        this.worldModel = worldModel;
        this.worldView = worldView;
        this.executor = Executors.newCachedThreadPool(); // Adjust the thread pool as needed
        this.statistics = statistics;
    }

    /*
     * Methods for updating world
     */

    public void updateWorld(double deltaTime) {
        if (ticks % 10 == 0){updateAI();}//triggers 5 times per second

        //moves the organism and calculates their energy depletion
        moveOrganisms();

        //eating, reproduction
        runEnergyCalculations();

        // food and creature removal due to energy depletion
        removeDeadEntities();

        // TODO create more food
        createMoreFood(ticks);

        ticks++;
        if (ticks > 49) { //triggers every 1 second
            seconds++;
            ticks = 0;
            changeWorldArea();
            changeWorldFoodAmount(); 
            //TODO toggleable flexible food regeneration (increases food regen rate when population is low, decreases rate when it is high, tries to keep population within certain bounds)
            increaseAges();


            if (seconds > 58) { //Triggers every 1 minute
                minutes++;
                seconds = 0;
                //TODO check if there are organisms in the system. If there are none, then terminate gracefully or restart the simulation either from checkpoint or from new.
                changeWorldFoodEnergy();


                if (minutes > 58) { //triggers every 1 hour
                    hours++;
                    minutes = 0;

                    // TODO adjust world values to match  desired values, such as 
                    // lower the mutation rate over time
                    // set maximum_lifespan = AVERAGE_LIFESPAN * 1.6 every hour

                }
                
                statistics.print();
            } //1 minute

            statistics.update(executor); // log world statistics
        } //1 second
    }


    /*
     * Methods for calculating the organisms and interractions in the world
     */

    private int updateAICount = 0;
    private int updateAISizeFrequencyTicks = 5;
    private void updateAI() {
        // Only a part of the organisms are updated. Process AI on a rotation
        int startIndex, endIndex;
        if (worldModel.getOrganisms().size() < 100) {
            startIndex = 0;
            endIndex = worldModel.getOrganisms().size();
        }
        else{
            startIndex = (updateAICount * worldModel.getOrganisms().size()) / updateAISizeFrequencyTicks;
            endIndex = ((updateAICount + 1) * worldModel.getOrganisms().size()) / updateAISizeFrequencyTicks;
        }
        updateAICount = (updateAICount + 1) % updateAISizeFrequencyTicks;

        // Create a countdown latch to synchronize AI calculations
        CountDownLatch aiCalculationLatch = new CountDownLatch(endIndex - startIndex);

        // Update the AI for organisms within the current tick's range
        List<BaseOrganism> organismsToUpdate = worldModel.getOrganisms().subList(startIndex, endIndex);
        for (BaseOrganism organism : organismsToUpdate){
            
            executor.submit(() -> {
                try {
                    organism.ai.update();
                } catch (Exception e) {
                    // Handle exception here, such as logging
                    e.printStackTrace();
                } finally {
                    aiCalculationLatch.countDown(); // Signal completion of AI calculation
                }
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

    /**
     * Activates the organism's AI method for moving the organisms.
     * Calls each organism in its own thread.
     */
    private void moveOrganisms(){
        // Create a countdown latch to synchronize the movement calculations
        CountDownLatch movementCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // Update the positions of the organisms in the world based on the output of their AI
        Iterator<BaseOrganism> organismPositionIterator = worldModel.getOrganisms().iterator();
        while (organismPositionIterator.hasNext()){
            BaseOrganism organism = organismPositionIterator.next();

            executor.submit(() -> {
                try {
                    organism.ai.handleMoving();
                    handleEnergyDepletion(organism);
                } catch (Exception e) {
                    // Handle exception here, such as logging
                    e.printStackTrace();
                } finally {
                    movementCalculationLatch.countDown();
                }
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

    private void runEnergyCalculations(){
        // Create a countdown latch to synchronize the energy expenditure and replenishing calculations
        CountDownLatch energyCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // TODO Handle eating, energy depletion,, reproduction, and death
        Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
        while (organismIterator.hasNext()){
            BaseOrganism organism = organismIterator.next();

            executor.submit(() -> {
                try {
                    handleFoodIntake(organism);
                    handleReproduction(organism);
                } catch (Exception e) {
                    // Handle exception here, such as logging
                    e.printStackTrace();
                } finally {
                    energyCalculationLatch.countDown();
                }
            });
        }

        // Wait for all energy consumption and replenishing calculations to complete
        try {
            energyCalculationLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        addChildrenToSimulation();
    }

    private void addChildrenToSimulation(){
        Iterator<BaseOrganism> childIterator = worldModel.getOrganisms().iterator();
        while (childIterator.hasNext()){
            BaseOrganism organism = childIterator.next();
            if(organism.recentChild != null) {
                childrenToAddToTheSimulation.add(organism.recentChild);
                organism.recentChild = null;
            }
        }
        Iterator<BaseOrganism> newbornIterator = childrenToAddToTheSimulation.iterator();
        while (newbornIterator.hasNext()) {
            BaseOrganism newBorn = newbornIterator.next();
            worldModel.addOrganism(newBorn); 
            newbornIterator.remove();
        }
    }

    // Shutdown the executor service when it's no longer needed
    public void shutdown() {
        executor.shutdown();
    }

    private void handleFoodIntake(BaseOrganism organism){
        //Remove food and add energy to creature if it is close enough
        Iterator<Food> foodIterator = worldModel.getFoods().iterator();
        while (foodIterator.hasNext()){
            Food food = foodIterator.next();

            if(organism.position.distanceTo(food.position) < organism.size * 2){ //food consumption algorithm
                //alternative consumption method. Fills the organism's energy according to how much it can possibly handle.
                //double consumed = Math.min(organism.maxEnergy, food.value); 
                //organism.energy += consumed;
                //food.value -= consumed;

                // recording for energy efficiency calculations
                organism.numFoodEaten++;
                organism.energySpendingLog.add(organism.energySpentSinceLastEating);
                organism.energySpentSinceLastEating = 0.0;

                // eat food and restore energy
                organism.energy += food.value;
                food.value = 0;
                if(organism.energy > organism.maxEnergy) {
                    double gainWeight = (organism.energy - organism.maxEnergy) / 14; //convert excess energy to weight
                    organism.energy = organism.maxEnergy;
                    organism.weight += gainWeight;
                }
            }
        }
    }

    private void handleEnergyDepletion(BaseOrganism organism){ 
        double energyDepletion = 1 * WorldModel.baseEnergyDepletionRate;

        energyDepletion += Math.pow(organism.velocity * organism.maxVelocity + 1, 2) * WorldModel.speedEnergyDepletionFactor;

        organism.energy -= energyDepletion;
        organism.energySpentSinceLastEating += energyDepletion;

        while(organism.energy < 1) { //convert weight back into energy
            organism.energy += 9.0;
            organism.weight -= 1.0;
        }
    }

    private void handleReproduction(BaseOrganism organism){
        //check if organism meets the energy and weight requirements
        if (organism.energy > organism.energyNeededToReproduce && organism.weight > organism.weightNeededToReproduce) {
            BaseOrganism closestOrganism = null;
            double closestDistance = -1;
            double distance;
            BaseOrganism organism2;
            BaseOrganism kid = organism;
            //select the other parent by selecting the closest organism TODO better selection mechanism
            Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
            while (organismIterator.hasNext()){
                organism2 = organismIterator.next();
                if (closestDistance < 0) {
                    closestDistance = organism.position.distanceTo(organism2.position);
                }
                distance = organism.position.distanceTo(organism2.position);
                if (distance < closestDistance && !organism.equals(organism2)) { //select closest
                    closestDistance = distance;
                    closestOrganism = organism2;
                }
            }
            kid = organism.reproduce(closestOrganism);
            organism.recentChild = kid;
            organism.energy -= organism.energyNeededToReproduce;
            organism.weight -= organism.weightNeededToReproduce;
        }

        if(organism.energyNeededToReproduce > organism.maxEnergy) {
            //System.out.println("ERROR  WorldController.handleReproduction()  Organism cannot reproduce because the energy it needs it higher than its maximum");
        }
    }

    private void removeDeadEntities(){
        Iterator<Food> foodIterator = worldModel.getFoods().iterator();
        while (foodIterator.hasNext()){
            Food food = foodIterator.next();
            if (food.value < 1) {
                foodIterator.remove();
            }
            else if (worldModel.useLifespan && food.age > worldModel.food_lifespan) {
                foodIterator.remove();
            }
        }

        
        Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
        while (organismIterator.hasNext()) {
            BaseOrganism organism = organismIterator.next();
            if (organism.energy < 1) {
                organismIterator.remove();
            }
            else if (organism.weight < 1) {
                organismIterator.remove();
            }
            else if (worldModel.useLifespan && organism.age > organism.maxAge) {
                organismIterator.remove();
            }
        }
        
    }

    private void createMoreFood(int tickCount) {
        if(tickCount % worldModel.ticksPer_FoodSpawn == 0 
            && worldModel.getFoods().size() < worldModel.maxFoodAmount) {
                for(int i = 0; i < worldModel.foodSpawnedPerEvent; i++){   
                    worldModel.addFood(new Food(
                        new Pos(worldModel.getWidth() * Math.random(), worldModel.getHeight() * Math.random()), 
                        worldModel.maxFoodEnergy, 
                        3));
                }
        }
    }

    private void increaseAges(){
        for (BaseOrganism o : worldModel.getOrganisms()) {
            o.age++;
        }
        for (Food f : worldModel.getFoods()) {
            f.age++;
        }
    }

    // Expand / shrink world over time
    public void changeWorldArea(){
        //System.out.println("Changing world area size"); //Debug
        if (worldModel.width < worldModel.desiredWidth){
            worldModel.width += 1.75;
            worldModel.height += 1;

            worldModel.maxFoodAmount = (int) ((worldModel.width * worldModel.height) * worldModel.foodDensity);
        }
        else if (worldModel.width > worldModel.desiredWidth){
            worldModel.width -= 1.75;
            worldModel.height -= 1;

            worldModel.maxFoodAmount = (int) ((worldModel.width * worldModel.height) * worldModel.foodDensity);
        }
    }

    // Increase / descrease max food amount over time
    public void changeWorldFoodAmount(){
        //System.out.println("Changing world food amount"); //Debug
        if (worldModel.maxFoodAmount < worldModel.desiredMaxFoodAmount) {
            worldModel.maxFoodAmount++;
            worldModel.foodDensity = worldModel.maxFoodAmount / (worldModel.width * worldModel.height);
        }
        else if (worldModel.maxFoodAmount > worldModel.desiredMaxFoodAmount) {
            worldModel.maxFoodAmount--;
            worldModel.foodDensity = worldModel.maxFoodAmount / (worldModel.width * worldModel.height);
        }
    }

    // Increase / decrease food energy over time
    public void changeWorldFoodEnergy(){
        //System.out.println("Changing world food aenergy"); //Debug
        if (worldModel.maxFoodEnergy < worldModel.desiredMaxFoodEnergy) {
            worldModel.maxFoodEnergy++;
        }
        else if (worldModel.maxFoodEnergy > worldModel.desiredMaxFoodEnergy) {
            worldModel.maxFoodEnergy--;
        }
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
