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

        // Assert visionPoints
        Point[] visionPoints = organism.getVisionPoints();
        assertEquals(new Point(-2.298, 1.928), visionPoints[0]);
    }
}  //TODO add tests