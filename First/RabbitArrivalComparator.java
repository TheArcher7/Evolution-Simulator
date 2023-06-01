import java.util.Comparator;

public class RabbitArrivalComparator implements Comparator<Rabbit> {
    @Override
    public int compare(Rabbit r1, Rabbit r2){
        if(r1.travelTime == r2.travelTime){
            return 0;
        }
        if(r1.travelTime > r2.travelTime){
            return 1;
        }
        else{
            return -1;
        }
    }
}
