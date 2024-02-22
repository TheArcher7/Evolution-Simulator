package main.java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import main.java.controller.WorldController;
import main.java.model.BaseOrganism;
import main.java.model.WorldModel;
import main.java.statistics.Statistics;
import main.java.util.Pos;
import main.java.util.WorldFactory;
import main.java.view.ExampleOrganismView;
import main.java.view.MainWindow;
import main.java.view.WorldInputListener;
import main.java.view.WorldView;

public class App implements ActionListener{
    // Variables for timekeeping
    private final int REFRESH_TIME = 15;
    private Timer timer;
    private double lastTime;

    // Master variables
    private WorldModel worldModel;
    private WorldView worldView;
    private WorldController controller;
    private WorldInputListener listener;
    private Statistics statistics;
    private MainWindow mainWindow;

    // main method
    public static void main(String[] args) throws Exception {
        new App();

        /* Example organism usage
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
        */
    }

    // Constructor
    public App(){
        setLookAndFeel();
        initProgram();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initProgram() {
        if (timer != null && timer.isRunning()) {timer.stop();}

        //set up world
        worldModel = WorldFactory.createTestWorld();
        worldView = new WorldView(worldModel);
        statistics = new Statistics(worldModel);
        controller = new WorldController(worldModel, worldView, statistics);

        //listeners
        listener = new WorldInputListener(controller);
        worldView.addKeyListener(listener);
        worldView.addMouseListener(listener);
        worldView.addMouseWheelListener(listener);

        //set up main window
        mainWindow = new MainWindow(worldView, controller, statistics, worldModel);
        controller.setMainWindow(mainWindow);

        //set up timer
        timer = new Timer(REFRESH_TIME, this);
        timer.start();
        lastTime = System.nanoTime();
    }

    // Main loop of whole program
    @Override
    public void actionPerformed(ActionEvent e) { 
        double currentTime = System.nanoTime();
        double delta = (System.nanoTime() - lastTime) / 1E9;
        lastTime = currentTime;

        listener.handlePressedKeys();
        listener.handlePressedMouseButtons(worldView.getZoomFactor());

        worldView.repaint();
        mainWindow.update(delta);
        controller.updateWorld(delta);
    }

}