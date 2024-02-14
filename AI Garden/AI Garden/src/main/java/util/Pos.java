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

    /*
     * Additional methods for the Point class could be added below
     */

    // Method to calculate the distance between this point and another point
    public double distanceTo(Pos otherPoint) {
        double deltaX = otherPoint.xCoord - this.xCoord;
        double deltaY = otherPoint.yCoord - this.yCoord;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

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
