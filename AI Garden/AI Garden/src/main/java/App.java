package main.java;

import javax.swing.SwingUtilities;

import main.java.model.BaseOrganism;
import main.java.util.Pos;
import main.java.view.ExampleOrganismView;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");


        // Example organism usage
        Pos position = new Pos(0, 0);
        double thetaDirection = 90.0;
        double[] phiVisionDirection = new double[]{50.0, 35.0, 20.0, 10.0, 0, -10.0, -20.0, -35.0, -50.0};
        double visionRadius = 60.0;
        double size = 10.0;

        BaseOrganism organism = new BaseOrganism(
            position, 
            thetaDirection, 
            phiVisionDirection, 
            visionRadius, 
            size);

        SwingUtilities.invokeLater(() -> {
            ExampleOrganismView exampleOrganismView = new ExampleOrganismView(organism);
            exampleOrganismView.setVisible(true);
        });
    }
}
