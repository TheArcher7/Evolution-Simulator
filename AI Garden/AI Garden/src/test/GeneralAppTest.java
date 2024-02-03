package test;


import org.junit.Test;
import static org.junit.Assert.*;

import main.java.model.BaseOrganism;
import main.java.util.Point;


public class GeneralAppTest {

    @Test
    public void testBaseOrganismHitbox() {
        // Instantiate BaseOrganism at coordinates (0,0)
        BaseOrganism organism = new BaseOrganism(new Point(0, 0));

        // Assert hitbox points
        Point[] hitbox = organism.getHitbox();
        assertEquals(new Point(-4, -4), hitbox[0]);
        assertEquals(new Point(4, -4), hitbox[1]);
        assertEquals(new Point(-4, 4), hitbox[2]);
        assertEquals(new Point(4, 4), hitbox[3]);
    }

    @Test
    public void testBaseOrganismVisionPoint() {
        // Instantiate BaseOrganism at coordinates (0,0)
        BaseOrganism organism = new BaseOrganism(new Point(0, 0));

        // Assert visionPoints with an accuracy of 3 decimals
        Point[] visionPoints = organism.getVisionPoints();
        assertEquals(roundPoint(new Point(2.298, 1.928)), roundPoint(visionPoints[0]));
    }
    
    // Helper method to round a Point to 3 decimal places
    private Point roundPoint(Point point) {
        double roundedX = Math.round(point.xCoord * 1000.0) / 1000.0;
        double roundedY = Math.round(point.yCoord * 1000.0) / 1000.0;
        return new Point(roundedX, roundedY);
    }


}  //TODO add tests