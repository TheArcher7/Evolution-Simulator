package main.java.util;

public class Pos {
    // Public attributes
    public double xCoord;
    public double yCoord;

    // Constructor
    public Pos(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    // Additional methods for the Point class could be added here
    // Equals method to compare two Point objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pos other = (Pos) obj;
        return Double.compare(other.xCoord, xCoord) == 0 &&
               Double.compare(other.yCoord, yCoord) == 0;
    }

    // ToString method to represent Point as a string
    @Override
    public String toString() {
        return "(" + xCoord + ", " + yCoord + ")";
    }
}
