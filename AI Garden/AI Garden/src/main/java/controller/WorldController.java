package main.java.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;
import main.java.util.Pos;
import main.java.view.MainWindow;
import main.java.view.WorldView;

public class WorldController {
    private WorldModel worldModel;
    private WorldView worldView;
    private MainWindow mainWindow;
    private ExecutorService executor;

    private ArrayList<BaseOrganism> childrenToAddToTheSimulation = new ArrayList<>();

    private int ticks = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

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
        if (ticks % 10 == 0){updateAI();}//triggers 5 times per second
        //TODO process AI on a rotation

        //moves the organism and calculates their energy depletion
        moveOrganisms();

        //eating, reproduction
        runEnergyCalculations();

        // food and creature removal due to energy depletion
        removeDeadEntities();

        // TODO create more food
        createMoreFood(ticks);

        // TODO create logs every 1 second

        // TODO adjust world values to match, such as 
        // lower the mutation rate over time
        // adjust the maximum lifespan to AVERAGE_LIFESPAN * 1.6

        
        ticks++;
        if (ticks > 49) { //triggers every 1 second
            seconds++;
            ticks = 0;

            // Optional expand world over time
            if (worldModel.width < 10500){
                worldModel.width += 1.75;
                worldModel.height += 1;
            }
            worldModel.maxFoodAmount = (int) (worldModel.width * worldModel.height / worldModel.foodDensity);
        }
        if (seconds > 58) { //Triggers every 1 minute
            minutes++;
            seconds = 0;
            System.out.println(worldModel.width + " " + worldModel.height);

            //optional decrease food energy over time
            if (worldModel.maxFoodEnergy > 500) {
                worldModel.maxFoodEnergy--;
            }

            //TODO increase the age of entities
            
        }
        if (minutes > 58) { //triggers every 1 hour
            hours++;
            minutes = 0;

            // Optional, decrease food density over time
            if (worldModel.foodDensity > 50) {
                worldModel.foodDensity--;
            }
        }
    }


    /*
     * Methods for calculating the organisms and interractions in the world
     */

    private void updateAI() {
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

    private void moveOrganisms(){
        // Create a countdown latch to synchronize the movement calculations
        CountDownLatch movementCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // Update the positions of the organisms in the world based on the output of their AI
        Iterator<BaseOrganism> organismPositionIterator = worldModel.getOrganisms().iterator();
        while (organismPositionIterator.hasNext()){
            BaseOrganism organism = organismPositionIterator.next();

            executor.submit(() -> {
                organism.ai.handleMoving();
                handleEnergyDepletion(organism);
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

    private void runEnergyCalculations(){
        // Create a countdown latch to synchronize the energy expenditure and replenishing calculations
        CountDownLatch energyCalculationLatch = new CountDownLatch(worldModel.getOrganisms().size());

        // TODO Handle eating, energy depletion,, reproduction, and death
        Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
        while (organismIterator.hasNext()){
            BaseOrganism organism = organismIterator.next();

            executor.submit(() -> {
                handleFoodIntake(organism);
                handleReproduction(organism);
                energyCalculationLatch.countDown();
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
            //select the other parent by selecting the closest organism
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
        }

        
        Iterator<BaseOrganism> organismIterator = worldModel.getOrganisms().iterator();
        while (organismIterator.hasNext()) {
            BaseOrganism organism = organismIterator.next();
            if (organism.energy < 1) {
                organismIterator.remove();
            }
            if (organism.weight < 1) {
                organismIterator.remove();
            }
            if (organism.age > organism.maxAge) {
                organismIterator.remove();
            }
        }
        
    }

    private void createMoreFood(int timer) {
        if(timer % worldModel.ticksPer_OneFoodSpawned == 0 
            && worldModel.getFoods().size() < (worldModel.maxFoodAmount - worldModel.getOrganisms().size()*5)) {
            worldModel.addFood(new Food(
                new Pos(worldModel.getWidth() * Math.random(), worldModel.getHeight() * Math.random()), 
                worldModel.maxFoodEnergy, 
                3));
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
