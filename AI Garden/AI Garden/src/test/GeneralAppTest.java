package test;


import org.junit.Test;
import static org.junit.Assert.*;

import main.java.ai.AI;
import main.java.model.BaseOrganism;
import main.java.util.Pos;


public class GeneralAppTest {

    @Test
    public void testBaseOrganism_UpdateHitbox() {
        // Instantiate BaseOrganism at coordinates (0,0)
        BaseOrganism organism = new BaseOrganism(new Pos(0, 0));
        organism.setSize(4);

        organism.updateHitbox();

        // Assert hitbox points
        Pos[] hitbox = organism.hitbox;
        assertEquals(new Pos(-4, -4), hitbox[0]);
        assertEquals(new Pos(4, -4), hitbox[1]);
        assertEquals(new Pos(-4, 4), hitbox[2]);
        assertEquals(new Pos(4, 4), hitbox[3]);
    }

    @Test
    public void testBaseOrganism_UpdateVisionPoints() {
        // Instantiate BaseOrganism at coordinates (0,0)
        BaseOrganism organism = new BaseOrganism(new Pos(0, 0));
        organism.setThetaDirection(90);
        organism.setPhiVisionDirection(new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0});
        organism.setVisionRadius(3);

        organism.updateVisionPoints();

        // Assert visionPoints with an accuracy of 3 decimals
        Pos[] visionPoints = organism.getVisionPoints();
        assertEquals(roundPoint(new Pos(2.298, 1.928)), roundPoint(visionPoints[0]));
        //TODO add tests
    }
    
    // Helper method to round a Point to 3 decimal places
    private Pos roundPoint(Pos point) {
        double roundedX = Math.round(point.xCoord * 1000.0) / 1000.0;
        double roundedY = Math.round(point.yCoord * 1000.0) / 1000.0;
        return new Pos(roundedX, roundedY);
    }


    @Test
    public void testDistanceFormula(){
        Pos pos = new Pos(0, 0);
        Pos pos2 = new Pos(3, 4);

        double distance = pos.distanceTo(pos2); //should be 5.0
        assertEquals(5.0, distance);
    }

    @Test
    public void outsideOfRangeTest(){
        boolean b = AI.outsideOfRange(1, 2, 3, 4);
        System.out.println(b); //should be true

        b = AI.outsideOfRange(0, 0, 0, 0);
        System.out.println(b); //should be false

        b = AI.outsideOfRange(0, 3, 2, 4);
        System.out.println(b); //should be false

        b = AI.outsideOfRange(2, 3, 0, 4);
        System.out.println(b); //should be false

        b = AI.outsideOfRange(3, 4, 0, 2);
        System.out.println(b); //should be true
    }

}  