package main.java.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import main.java.model.BaseOrganism;
import main.java.model.Food;
import main.java.model.WorldModel;

public class Statistics {
    private final WorldModel worldModel;
    private final List<LogElement> log;
    private double lastTimeUpdated = 0;

    // Statistics about the organisms (copied from LogElement)
    public int population;
    public double timeHours;

    public double averageEnergy;
    public double averageWeight;
    public double heaviestOrganismWeight;

    public int averageAge;
    public int oldestOrganismAge;
    public int averageGeneration;
    public int newestGeneration;
    public int oldestGeneration;
    public int averageNumChildren;
    public int mostNumChildrenAmount;

    public double averageVelocity;
    public int averageNumFoodEaten;
    public int mostNumFoodEaten;
    public double averageEnergySpentPerFood;
    public double lowestEnergySpentPerFoodValue;

    // Statistics about the world elements
    public int foodCount;
    public int maxFoodAmount;
    public int averageFoodAge;
    public int oldestFoodAge;

    public Statistics(WorldModel worldModel) {
        this.worldModel = worldModel;
        log = new ArrayList<>();
    }

    @Deprecated
    public void update(double deltaTime) {
        lastTimeUpdated += deltaTime;

        if (lastTimeUpdated >= 1) {
            lastTimeUpdated = 0;

            //add logElement to log
        }
    }

    public void update(ExecutorService executor) {
        // Create a countdown latch to synchronize AI calculations
        CountDownLatch calculationLatch = new CountDownLatch(4);

        executor.submit(() -> {
            try {
                velocityAndEnergySpent();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                calculationLatch.countDown(); // Signal completion of calculation 1
            }
        });
        executor.submit(() -> {
            try {
                population();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                calculationLatch.countDown(); // Signal completion of calculation 2
            }
        });
        executor.submit(() -> {
            try {
                energyAndWeight();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                calculationLatch.countDown(); // Signal completion of calculation 3
            }
        });
        executor.submit(() -> {
            try {
                ageAndChildren();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                calculationLatch.countDown(); // Signal completion of calculation 4
            }
        });

        // Wait for all AI calculations to complete
        try {
            calculationLatch.await();
            recordNewStatElement();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void recordNewStatElement(){
        log.add(new LogElement(
            population,
            timeHours,
            averageEnergy, 
            averageWeight, 
            heaviestOrganismWeight, 
            averageAge, 
            oldestOrganismAge, 
            averageGeneration, 
            newestGeneration, 
            oldestGeneration, 
            averageVelocity, 
            averageNumChildren, 
            mostNumChildrenAmount,
            averageNumFoodEaten, 
            mostNumFoodEaten, 
            averageEnergySpentPerFood, 
            lowestEnergySpentPerFoodValue, 
            foodCount, 
            maxFoodAmount, 
            averageFoodAge, 
            oldestFoodAge
            ));
    }

    public void population(){
        List<Food> food = worldModel.getFoods();
        population = worldModel.getOrganisms().size();
        timeHours = log.size() / 60.0 / 60.0;
        foodCount = food.size();
        maxFoodAmount = worldModel.maxFoodAmount;
        averageFoodAge = 0;
        oldestFoodAge = 0;
        for (Food f : food) {
            averageFoodAge += f.age;
            if(f.age > oldestFoodAge) {
                oldestFoodAge = f.age;
            }
        }
        averageFoodAge = averageFoodAge / foodCount;
    }

    public void energyAndWeight(){
        List<BaseOrganism> list = worldModel.getOrganisms();
        if (list.size() < 1) {return;}
        averageEnergy = 0.0;
        averageWeight = 0.0;
        heaviestOrganismWeight = 0.0;
        for (BaseOrganism o : list) {
            if (o.weight > heaviestOrganismWeight) {
                heaviestOrganismWeight = o.weight;
            }
            averageEnergy += o.energy;
            averageWeight += o.weight;
        }
        averageEnergy = averageEnergy / list.size();
        averageWeight = averageWeight / list.size();
    }

    public void ageAndChildren(){
        List<BaseOrganism> list = worldModel.getOrganisms();
        if (list.size() < 1) {return;}
        //Age
        averageAge = 0;
        oldestOrganismAge = 0;
        //Generation
        averageGeneration = 0;
        newestGeneration = 0;
        //Children
        averageNumChildren = 0;
        mostNumChildrenAmount = 0;
        oldestGeneration = list.get(0).generation;
        for (BaseOrganism o : list) {
            averageAge += o.age;
            averageGeneration += o.generation;
            averageNumChildren += o.numChildren;
            if (o.age > oldestOrganismAge) {
                oldestOrganismAge = o.age;
            }
            if (o.generation < oldestGeneration) {
                oldestGeneration = o.generation;
            }
            if (o.generation > newestGeneration) {
                newestGeneration = o.generation;
            }
            if (o.numChildren > mostNumChildrenAmount) {
                mostNumChildrenAmount = o.numChildren;
            }
        }
        averageAge = averageAge / list.size();
        averageGeneration = averageGeneration / list.size();
        averageNumChildren = averageNumChildren / list.size();
    }

    public void velocityAndEnergySpent(){
        List<BaseOrganism> list = worldModel.getOrganisms();
        if (list.size() < 1) {return;}
        averageVelocity = 0.0;
        averageNumFoodEaten = 0;
        mostNumFoodEaten = 0;
        averageEnergySpentPerFood = 0.0;
        lowestEnergySpentPerFoodValue = list.get(0).getAverageEnergySpent();
        double individualAverageEnergySpent;
        for (BaseOrganism o : list) {
            averageVelocity += (o.velocity * o.maxVelocity);
            averageNumFoodEaten += o.numFoodEaten;

            individualAverageEnergySpent = o.getAverageEnergySpent(); //returns the average energy an individual organisms spends while searching for food
            averageEnergySpentPerFood += individualAverageEnergySpent;
            if (o.numFoodEaten > mostNumFoodEaten) {
                mostNumFoodEaten = o.numFoodEaten;
            }
            if (individualAverageEnergySpent < lowestEnergySpentPerFoodValue && individualAverageEnergySpent != 0
             && o.energySpendingLog.size() > 4) {
                lowestEnergySpentPerFoodValue = individualAverageEnergySpent;
            }
        }
        averageVelocity = averageVelocity / list.size();
        averageNumFoodEaten = averageNumFoodEaten / list.size();
        averageEnergySpentPerFood = averageEnergySpentPerFood / list.size();
    }
    

    public LogElement getTopLogElement() {
        return log.get(log.size() - 1);
    }


    public void print() { //prints the latest log data to the consol. Useful for testing
        System.out.println("Statistics Report");
        LogElement l = getTopLogElement();
        System.out.println(l.toString());
    }


    public void saveBestOrganism(File file) throws IOException{
        FileWriter writer = new FileWriter(file);
        try {
            //chose an organism
            BaseOrganism organism = worldModel.getOrganisms().get(0); //TODO save best organism rather than first organism

            //write organism to file
            writer.write(organism.getSerialization(file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            writer.close();
        }
    }
}
