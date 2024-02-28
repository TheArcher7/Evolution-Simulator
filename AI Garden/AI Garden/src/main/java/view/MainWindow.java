package main.java.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
    private JLabel populationStatisticsLabel;
    private JLabel speedLabel;
    private JSlider speedSlider;
    private JLabel maxFoodLabel;
    private JSpinner maxFoodSpinner;

    // Menus
    private JMenuBar mainMenuBar;
    // File Menu
    private JMenu fileMenu;
    private JMenuItem exportStatisticsItem;
    private JMenuItem saveOrganismToFile;
    private JMenuItem saveWorldStateToFile;
    private JMenuItem closeItem;
    // World Menu
    private JMenu worldMenu;
    private JMenuItem createOrganism; // spawns a randomly generated organism
    private JMenuItem loadOrganismFromFile;
    private JMenuItem loadWorldFromFile;
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

        /*
         * Menu Bar
         */
        mainMenuBar = new JMenuBar();

        //File menu (saving and exiting)
        fileMenu = new JMenu("File");
        // Export statistics
        exportStatisticsItem = new JMenuItem("Export Statistics");
        exportStatisticsItem.addActionListener(this);
        fileMenu.add(exportStatisticsItem);
        fileMenu.addSeparator();
        // Save organism
        saveOrganismToFile = new JMenuItem("Save Organism To File");
        saveOrganismToFile.addActionListener(this);
        fileMenu.add(saveOrganismToFile);
        // Save world
        saveWorldStateToFile = new JMenuItem("Save World To File");
        saveWorldStateToFile.addActionListener(this);
        fileMenu.add(saveWorldStateToFile);
        fileMenu.addSeparator();
        // Close
        closeItem = new JMenuItem("Close");
        closeItem.addActionListener(this);
        fileMenu.add(closeItem);

        // World menu (loading from file and generating)
        worldMenu = new JMenu("Creation");
        // Create Organism
        createOrganism = new JMenuItem("Create Randomized Organism");
        createOrganism.addActionListener(this);
        worldMenu.add(createOrganism);
        worldMenu.addSeparator();
        // Load Organism
        loadOrganismFromFile = new JMenuItem("Load Organism From File");
        loadOrganismFromFile.addActionListener(this);
        worldMenu.add(loadOrganismFromFile);
        // Load World
        loadWorldFromFile = new JMenuItem("Load World From File");
        loadWorldFromFile.addActionListener(this);
        worldMenu.add(loadWorldFromFile);
        
        // Statistics Menu
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
                        "Rate: " + model.foodSpawnedPerEvent + " Food /" + model.ticksPer_FoodSpawn + " Ticks       " +
                        "Time: " + String.format("%3d", controller.hours) + ":" + 
                        String.format("%02d", controller.minutes) + ":" + 
                        String.format("%02d", controller.seconds);
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
        // TODO convert this to a switch structure
        if (e.getSource() == closeItem) {

            //debug printing statistics to consol
            statistics.print();

            //test printing the nn to the consol
            //String nnOut = model.getOrganisms().get(0).ai.neuralNetwork.getSerialization();
            //System.out.println(nnOut);

            System.exit(0);
        } 
        
        else if (e.getSource() == exportStatisticsItem) {
            System.out.println("Export statistics button clicked");
        }

        else if (e.getSource() == saveOrganismToFile) {
            // Uses a file choosing API
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
            fileChooser.setFileFilter(txtFilter);
        
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (fileChooser.getFileFilter() == txtFilter &&
                        !FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("txt")) {
                    file = new File(file.toString() + ".txt");
                }
                try {
                    statistics.saveBestOrganism(file); //write an organism's data to the file chosen
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        else if (e.getSource() == saveWorldStateToFile) {
            System.out.println("Save world to file button clicked");
        }
        
        
        else if (e.getSource() == createOrganism) {
            //new CreateCreatureView(controller, worldModel.getWidth(), worldModel.getHeight());
            // TODO create a random organism, preferably in the center of the screen to allow for user "placement"
            System.out.println("Create organism button clicked");
        } 

        else if (e.getSource() == loadOrganismFromFile) {
            System.out.println("Load organism from file button clicked");
        }

        else if (e.getSource() == loadWorldFromFile) {
            System.out.println("Load world from file button clicked");
        }

        else if (e.getSource() == showStatisticsItem) {
            System.out.println("Show statistics button clicked");
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

