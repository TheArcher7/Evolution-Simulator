import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/*
 * 
 * This will be a simple evolution simulator.
 * There will be rabbits, and pellets
 * field will be a 100,000 by 100,000 grid
 * 
 * 4/21/2023
 */



class driver{

    public static void main(String[] args) {
        int x_gridSize = 100000;
        int y_gridSize = 100000;
        final int startingPopulation = 40;
        final int simulationDays = 140; //2,000,000,000 number of days to simulate //TODO fix age simulation day bug

        double pelletEnergyAmount = 8000.0; //number of energy points each pellet has
        int numPelletsEachDay = (x_gridSize * y_gridSize) / (x_gridSize + y_gridSize); //each day of the simulation will have the same number of pellets randomly distributed //in the future, maybe decrease number of pellets according to a function for better competition

        Random randGen = new Random();
        RabbitArrivalComparator ArriveOrder = new RabbitArrivalComparator();

        ArrayList<log> logReport = new ArrayList<>();
        ArrayList<Rabbit> rabbitList = new ArrayList<>();
        ArrayList<Pellet> pelletList = new ArrayList<>();
        ArrayList<Pellet> calculateClosestPellets = new ArrayList<>();

        for(int i = 0; i < startingPopulation; i++){
            //initialize rabbits with random stats and starting positions
            rabbitList.add(new Rabbit(randGen, x_gridSize, y_gridSize));
            rabbitList.get(i).age = 0;
        }






        //begin the day
        int currDay = 0;
        do{

            //all pellets from previous day are destroyed.
            for(Pellet p: pelletList){
                p.rabbitsComingToPellet.clear();
            }
            for(Rabbit r: rabbitList){
                //clearpellets
            }
            pelletList.clear();
            //randomly create new pellets
            CreateNewPellets :
            for(int i = 0; i < numPelletsEachDay; i++){
                pelletList.add(new Pellet(randGen, x_gridSize, y_gridSize));
            } //number of pellets decrease in proportion to the number of rabbits so there aren't crazy populations





            //for the first rabbit in the array, take its coordinate position and compare it to the coordinate position of the pellets to create a sorted pellet_array where the closest pellets are at the top.
            //the rabbit makes a calculation to go to one of the closest three pellets depending upon how many rabbits are already going to the pellet, how much energy the rabbit has, and how much distance there is between it. It can even stay still to avoid consuming energy if the distance is too far.
            //once the rabbit chooses the pellet, the rabbit is added to the pellet as well as the distance calculation so that the computer doesn’t have to recalculate it.
            //move on and calculate the next rabbit
            //TODO make into a method for turning into threads
            Pellet closestPellet = pelletList.get(0);
            double minDistance;
            RabbitMovesToPelletOrPoint :
            for (Rabbit r : rabbitList){
                //reset everything before calculations
                r.numCalculations = 0;
                calculateClosestPellets.clear();

                FindPelletsWithinRanges : 
                for(Pellet p : pelletList){ //find pellets within ranges
                    if(p.rabbitsComingToPellet.size() < r.maxPreferedPoulationDensity){
                        //calculate distance between rabbit and the pellet
                        p.distance = Math.sqrt(Math.pow(p.x_coordinate - r.x_coordinate, 2) + 
                            Math.pow(p.y_coordinate - r.y_coordinate, 2));
                        if(p.distance < r.maxDistance){
                            //if the pellet is within the rabbits prefered distance, then add the pellet to the list
                            calculateClosestPellets.add(p); 
                        }
                        r.numCalculations++;
                        if(r.numCalculations > r.maxCalculations)
                            break FindPelletsWithinRanges;
                    }
                }

                minDistance = r.maxDistance;
                FindClosestPelletInRange : 
                for(Pellet p : calculateClosestPellets){ //find closest pellet
                    if(r.numCalculations > r.maxCalculations)
                        break FindClosestPelletInRange;
                    if(p.distance < minDistance){
                        closestPellet = p;
                        minDistance = p.distance;
                    }
                    r.numCalculations++;
                }
                
                if (calculateClosestPellets.size() == 0){ //travel
                    //rabbit stays still and doesn't spend unnecessary energy
                    r.travelDistance(0.0); //TODO rabbit moves at random
                }
                else{
                    r.travelToPellet(closestPellet, minDistance); 
                    closestPellet.comingToPellet(r);
                    //rabbit goes to the pellet
                    //the distance to the pellet is stored in the rabbit in r.distanceToCalculate
                    //a reference to the rabbit is given to the pellet in the rabbitsComingToPellet arraylist
                    //rabbit's xy coordinates are changed to match the pellet
                }
                //time = distance / speed
                r.calculateTravelTime();
                //energyCost = speed * weight * time
                r.energyCostForJourney();
            }

            




            //consume energy of rabbits
            //If a rabbit has less energy than is possible to make it to the pellet, kill the rabbit.
            for(Rabbit r : rabbitList){
                r.energy = r.energy - r.energyCost;
                if(r.energy < 0){
                    r.deathFlag = true;
                    continue;
                }
            }

            


            

            //Once all the rabbits have been calculated, loop through the list of pellets.
            //If one rabbit is going to a pellet, replenish its energy according to how much the pellet provides, then destroy the pellet
            //If multiple rabbits are going to the same pellet, consume the energy of the rabbits the same, 
            //then replenish energy by dividing the pellet based on arrival time. Destroy the pellet.
            int numRabbitsEatingPellet;
            for (Pellet p : pelletList){
                if( p.rabbitsComingToPellet.size() > 0){
                    numRabbitsEatingPellet = 0;
                    Collections.sort(p.rabbitsComingToPellet, ArriveOrder);
                    for(Rabbit r : p.rabbitsComingToPellet){
                        if(r.deathFlag){continue;} //skips the rest of the code in the loop and continues to the next rabbit
                        numRabbitsEatingPellet++;
                        r.energy = r.energy + (pelletEnergyAmount / numRabbitsEatingPellet); //algorithm for replenishing rabbit energy
                    }
                }
            }
            


            //the day is now over!
            //Rabbits require an amount of energy to sleep through the night in proportion to their age and maxCalculations. Older rabbits require more and more energy to sleep through the night. Perhaps more weight also allows them to spend less energy. Loop through the rabbits and decrease energy of all rabbits according to the calculation, destroying rabbits as needed
            AgeEnergySleepCost :
            for(Rabbit r : rabbitList){
                r.energy = r.energy - Math.pow(r.age, 1.1); //TODO fix algorithm
                r.age++;
                if(r.energy < 0){
                    r.deathFlag = true;
                    continue;
                }
            }


            //to stop rapid reproduction after consumption of a pellet, rabbits convert an amount of energy to weight. Once weight has reached their threshold, they reproduce.
            //rabbits reproduce and children are distributed to random places in the map. Their new stats are derived from the parent rabbit 
            //Parents lose half their weight with a single reproduction, but can birth multiple children. Each child is given energy depending on the weight.

            ArrayList<Rabbit> nextGenRabbitList = new ArrayList<>();
            for(Rabbit r : rabbitList){
                r.energyToWeight();
                if (r.energy <= 0 || r.weight <= 0){
                    r.deathFlag = true;
                }
                if(r.canReproduce() && r.deathFlag == false){
                    int children = r.numChildrenPerLitter;
                    r.weight = r.weight / 2;

                    for(int j = 0; j < children; j++){
                        //System.out.println("debug 2 =" + j);
                        Rabbit childRabbit = new Rabbit(r, randGen);
                        childRabbit.weight = r.weight / children;
                        childRabbit.energy = r.weight / children * 1000;
                        nextGenRabbitList.add(childRabbit);

                    }
                }
            }
            //System.out.println(nextGenRabbitList.size() + " rabbits born");
            rabbitList.addAll(nextGenRabbitList);
            
            //
            Iterator<Rabbit> iterator = rabbitList.iterator();
            removeDeadRabbitsFromSimulation :
            while (iterator.hasNext()) {
                Rabbit r = iterator.next();
                if (r.deathFlag == true) {
                    iterator.remove();
                }
            }

            //
            //create a log report with a randomly-sampled surviving rabbit
            //
            if(rabbitList.size() == 0){break;}

            int youngest, oldest, newestGen, oldestGen;
            youngest = rabbitList.get( randGen.nextInt(rabbitList.size()) ).age;
            oldest = youngest;
            newestGen = rabbitList.get( randGen.nextInt(rabbitList.size()) ).generation;
            oldestGen = newestGen;
            for(Rabbit r : rabbitList){
                if(r.age < youngest){youngest = r.age;} //the youngest rabbit
                if(r.age > oldest){oldest = r.age;} //the oldest rabbit
                if(r.generation > newestGen){newestGen = r.generation;} //the newest generation
                if(r.generation < oldestGen){oldestGen = r.generation;} //the oldest generation
            }

            logReport.add(new log(
                currDay,
                rabbitList.get( randGen.nextInt(rabbitList.size()) ).toString(),
                rabbitList.size(),
                pelletList.size(),
                oldest,
                oldestGen, newestGen
            ));

            //output checking
            System.out.print("Day:" + currDay);
            System.out.println("  Population:"+ rabbitList.size());
            System.out.println();


            //check to see if the number of “days” required of the simulation has been reached.
            currDay++;
        }while (currDay < simulationDays);







        //print logs reports for the user and possibly an option to save the data to a file
        // Write log data to file
        try {
            FileWriter writer = new FileWriter("Report.txt");
            for (log report : logReport) {
                writer.write(report.toString() + "\n");
            }
            writer.close();
            System.out.println("Report saved to file.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
            e.printStackTrace();
        }
        
    }
}