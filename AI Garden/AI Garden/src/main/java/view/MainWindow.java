package main.java.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import main.java.controller.WorldController;
import main.java.model.WorldModel;
import main.java.statistics.Statistics;

public class MainWindow extends JFrame implements ActionListener, ChangeListener {
    private WorldView view;
    private Statistics statistics;
    private WorldController controller;
    private WorldModel model;

    private JSplitPane splitPane;
    private boolean isExternalUpdate = false;

    // Text labels and widgets
    private JLabel textLabel;
    private JLabel speedLabel;
    private JSlider speedSlider;
    private JLabel maxFoodLabel;
    private JSpinner maxFoodSpinner;

    // Menus
    private JMenuBar mainMenuBar;
    // File Menu
    private JMenu fileMenu;
    private JMenuItem exportStatisticsItem;
    private JMenuItem closeItem;
    // World Menu
    private JMenu worldMenu;
    private JMenuItem createWorldItem;
    private JMenuItem createCreatureItem;
    private JMenuItem createCreaturesItem;
    // Statistics Menu
    private JMenu statisticsMenu;
    private JMenuItem showStatisticsItem;


    //Constructor
    public MainWindow(WorldView view, WorldController controller, Statistics statistics, WorldModel model) {
        this.view = view;
        this.controller = controller;
        this.statistics = statistics;
        this.model = model;

        initUI(view);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setTitle("AI Garden");

        setResizable(true);
        setVisible(true);
    }

    // Methods
    private void initUI(WorldView view){
        this.view = view;

        JPanel rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.PAGE_AXIS));

        textLabel = new JLabel("Organisms");
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        speedLabel = new JLabel("Speed");
        speedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        speedSlider = new JSlider(0, 15, 1);
        speedSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        speedSlider.setMajorTickSpacing(5);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(this);

        JPanel maxFoodContainer = new JPanel();
        maxFoodContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        maxFoodContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        maxFoodLabel = new JLabel("Max Food (Max 100,000)");
        SpinnerModel maxFoodSpinnerModel = new SpinnerNumberModel(model.maxFoodAmount, 0, 100000, 1);
        maxFoodSpinner = new JSpinner(maxFoodSpinnerModel);
        maxFoodSpinner.setEditor(new JSpinner.NumberEditor(maxFoodSpinner, "#"));
        maxFoodSpinner.addChangeListener(this);

        maxFoodContainer.add(maxFoodLabel);
        maxFoodContainer.add(maxFoodSpinner);

        // Add components and labels to the right container
        rightContainer.add(textLabel);
        rightContainer.add(Box.createVerticalStrut(10));
        rightContainer.add(speedLabel);
        rightContainer.add(speedSlider);
        rightContainer.add(maxFoodContainer);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, view, rightContainer);
        splitPane.setResizeWeight(0.8);

        add(splitPane);

        mainMenuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        exportStatisticsItem = new JMenuItem("Export Statistics");
        exportStatisticsItem.addActionListener(this);
        closeItem = new JMenuItem("Close");
        closeItem.addActionListener(this);
        fileMenu.add(exportStatisticsItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        worldMenu = new JMenu("Creation");
        createWorldItem = new JMenuItem("Create World");
        createWorldItem.addActionListener(this);
        worldMenu.add(createWorldItem);
        worldMenu.addSeparator();
        createCreatureItem = new JMenuItem("Create Creature");
        createCreatureItem.addActionListener(this);
        worldMenu.add(createCreatureItem);
        createCreaturesItem = new JMenuItem("Create Creatures");
        createCreaturesItem.addActionListener(this);
        worldMenu.add(createCreaturesItem);

        statisticsMenu = new JMenu("Statistics");
        showStatisticsItem = new JMenuItem("Show Statistics");
        showStatisticsItem.addActionListener(this);
        statisticsMenu.add(showStatisticsItem);

        mainMenuBar.add(fileMenu);
        mainMenuBar.add(worldMenu);
        mainMenuBar.add(statisticsMenu);

        setJMenuBar(mainMenuBar);
    }

    public void update(double delta) {
        // TODO get statistics and update the text labels for various attributes such as population
        String displayString = "";
        displayString += "Organisms: " + model.getOrganisms().size() + "       " + 
                        "Food: "  + model.getFoods().size() + "       " +
                        "Food Spawning: " + model.ticksPer_FoodSpawn + "*" + model.foodSpawnedPerEvent;
        textLabel.setText(displayString);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == speedSlider && !isExternalUpdate) {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();

            controller.setSpeed(value);
        } else if (e.getSource() == maxFoodSpinner) {
            JSpinner source = (JSpinner) e.getSource();
            model.maxFoodAmount = (int) source.getValue();
        }
    }

    public void setSpeedSlider(double d) {
        isExternalUpdate = true;
        speedSlider.setValue((int) d);
        isExternalUpdate = false;
    }

    public void setViewInputFocus() {
        view.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeItem) {
            System.exit(0);
        } else if (e.getSource() == exportStatisticsItem) {
            // TODO export statistics to a csv file
        } else if (e.getSource() == createWorldItem) {
            //new CreateWorldView(worldCreator);
        } else if (e.getSource() == createCreatureItem) {
            //new CreateCreatureView(controller, worldModel.getWidth(), worldModel.getHeight());
        } else if (e.getSource() == createCreaturesItem) {
            //new CreateCreaturesView(controller, worldModel.getWidth(), worldModel.getHeight());
        } else if (e.getSource() == showStatisticsItem) {
            
        }
    }

    public void setView(WorldView view) {
        this.view = view;
        splitPane.setLeftComponent(view);
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void setController(WorldController controller) {
        this.controller = controller;
    }


    public void setWorldModel(WorldModel worldModel) {
        this.model = worldModel;
    }
}

