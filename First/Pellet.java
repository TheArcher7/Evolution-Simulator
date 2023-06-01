import java.util.ArrayList;
import java.util.Random;

public class Pellet {
    public int x_coordinate;
    public int y_coordinate;
    public double distance;

    public ArrayList<Rabbit> rabbitsComingToPellet = new ArrayList<>();


    public Pellet(){
        //constructor initializes pellet with set values
    }
    public Pellet(Random randGen){
        //initialize pellet with random generator from main method

    }
    public Pellet(Random randGen, int x_gridSize, int y_gridSize){
        x_coordinate = randGen.nextInt(x_gridSize); //generates a num between 0 (inclusive) and the grid size (exclusive)
        y_coordinate = randGen.nextInt(y_gridSize);
    }

    public void goToPellet(Rabbit r, double d){
        r.distanceToCalculate = d;
        rabbitsComingToPellet.add(r);
    }

    public void comingToPellet(Rabbit r){
        rabbitsComingToPellet.add(r);
    }
}
