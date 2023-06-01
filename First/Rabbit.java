import java.util.Random;

public class Rabbit {
    public int generation;
    public int age = 0;
    public boolean deathFlag = false; //all rabbits start out alive

    public double speed;
    public double energy = 10000.0;
    public double energyToWeight = 500.0; //the amount of energy to convert to weight at the end of each day
    public double weight = 10.0;
    public double reproductionWeight = 12.0; //the weight the rabbit needs in order to reproduce
    public double energyCost = 0;

    public int numChildrenPerLitter = 1;

    //TODO rabbits can choose different stages of life to have growth behaviors. These can act as counters
    /*public int age1;
    public int age2;
    public int age3;
    public int age4;*/

    public int numCalculations = 0; //helps to determine how much energy should be spent
    public int maxCalculations;

    public int x_coordinate;
    public int y_coordinate;

    public double maxDistance; //maximum distance a rabbit will travel to get to a pellet
    public double distanceToCalculate; //the distance the rabbit will have to travel to get to the pellet in this iteration
    public double travelTime;
    public int maxPreferedPoulationDensity; //maximum rabits near a pellet the rabit will tolerate


    public Rabbit(Random randGen, int x_gridSize, int y_gridSize){
        generation = 0;
        energy = 10000; //starting energy
        x_coordinate = randGen.nextInt(x_gridSize); //generates a num between 0 (inclusive) and the grid size (exclusive)
        y_coordinate = randGen.nextInt(y_gridSize);
        maxPreferedPoulationDensity = 3;
        maxDistance = randGen.nextInt(x_gridSize) + randGen.nextInt(y_gridSize);
        maxCalculations = x_gridSize * y_gridSize / 2;
        speed = randGen.nextDouble(1) + 0.001;
        numChildrenPerLitter = 1;
    }

    public Rabbit(Rabbit parent, Random randGen){
        //new rabbit is created based on parent rabbit
        generation = parent.generation + 1;

        //new rabbit's coordinates are generated randomly based on parent
        x_coordinate = randGen.nextInt(21) - 10 + parent.x_coordinate;
        y_coordinate = randGen.nextInt(21) - 10 + parent.y_coordinate;
        speed = randGen.nextDouble(3) - 1 + parent.speed;
        //TODO improve coordinate picking

        //new rabbits pellet choosing behavior
        maxPreferedPoulationDensity = randGen.nextInt(3) - 1 + parent.maxPreferedPoulationDensity;
        int mutator = 100; //how different from the parent the child should be
        int mutation = randGen.nextInt(mutator) - (mutator/2);
        maxCalculations = mutation + parent.maxCalculations;

        //new rabbit reproduction behavior
        reproductionWeight = randGen.nextDouble(30) - 10 + parent.reproductionWeight;
        numChildrenPerLitter = randGen.nextInt(3) - 1 + parent.numChildrenPerLitter;
        if(numChildrenPerLitter <= 0){numChildrenPerLitter=1;} //guarantees that all rabbits will reproduce
    }







    public void travelDistance(double distance){
        distanceToCalculate = distance;
        //TODO move rabbit to new xy coordinate
    }
    public void travelToPellet(Pellet p, double distance){
        distanceToCalculate = distance;
        this.x_coordinate = p.x_coordinate;
        this.y_coordinate = p.y_coordinate;
        //p.comingToPellet(this);
    }
    public void calculateTravelTime(){
        if(speed > 0){
            travelTime = distanceToCalculate / speed;
        }
        else{
            travelTime = 1;
        }
    }


    public void energyToWeight(){ //converts rabbit energy to weight
        if(energyToWeight > 0){
            weight = weight + energyToWeight / 1000;
            energy = energy - energyToWeight; //it takes 1000 energy to create 1 weight
        }
    }
    public void energyCostForJourney(){
        energyCost = weight * speed * travelTime;
    }

    public void energyCost(double w, double s, double t){
        energyCost = w * s * t * age / 12;
    }

    public void incrAge(){
        age++;
    }


    public boolean canReproduce(){
        if(weight > reproductionWeight){
            return true;
        }
        return false;
    }


    //toString() method
    @Override
    public String toString(){
        return "Gen:" + generation + " Age:" + age + 
        " Energy:" + energy + " Weight:" + weight +
        " (" + x_coordinate + "," + y_coordinate + ")";
    }
}
