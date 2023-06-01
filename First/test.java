import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        
        ArrayList<String> myStrings = new ArrayList<>();

        myStrings.add("Hello ");
        myStrings.add("World!");

        for(int i = 0; i < myStrings.size(); i++){
            System.out.print(myStrings.get(i));
        }

        System.out.println("");
        for(String s : myStrings){
            System.out.print(s);
        }

    }
}
