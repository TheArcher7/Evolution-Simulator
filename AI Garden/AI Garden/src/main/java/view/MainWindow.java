package main.java.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOExceptionList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.java.controller.WorldController;
import main.java.model.BaseOrganism;
import main.java.model.WorldModel;
import main.java.statistics.LogElement;
import main.java.statistics.Statistics;
import main.java.util.FileLoader;
import main.java.util.Strings;

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


    // UI for world control
    private JLabel maxFoodLabel;
    private JLabel maxFoodSpinnerLabel;
    private JSpinner maxFoodSpinner;
    private JLabel foodspawnTicksLabel;
    private JLabel foodspawnTicksSpinner;
    private JLabel foodspawnCountLabel;
    private JLabel foodspawnCountSpinner;
    //
    private JLabel worldSizeLabel;
    private JLabel worldSizeSliderLabel;
    private JSlider worldSizeSlider;
    //
    private JCheckBox doAgingToggle;


    // UI for statistics display
    private JLabel averagesStatisticsLabel;
    private JLabel averageEnergy;
    private JLabel averageWeight;
    private JLabel averageFoodEaten;
    private JLabel averageNumChildren;
    private JLabel averageEnergySpentPerFood;
    private JLabel averageAge;
    private JLabel newestGeneration;
    //
    private JLabel bestOrganismStatisticsLabel;
    private JCheckBox box_mostFoodEaten;
    private JCheckBox box_mostChildren;
    private JCheckBox box_mostEfficient;
    private JCheckBox box_oldest;
    private JCheckBox box_oldestGeneration;
    //
    private JCheckBox debugToggle;

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
    private JMenuItem clearOrganisms;
    private JMenuItem clearFood;
    private JMenuItem pausePlay;
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

        /*
         * Speed slider and label (Not needed anymore)
         */
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

        /*
         * World Size label and slider
         */
        worldSizeLabel = new JLabel(Strings.worldSizeLabelText);
        //
        worldSizeSliderLabel = new JLabel(Strings.worldSizeSliderLabelText);
        //
        worldSizeSlider = new JSlider(3000, 15000, (int) model.desiredWidth);
        worldSizeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        worldSizeSlider.setMajorTickSpacing(3000);
        worldSizeSlider.setMinorTickSpacing(500);
        worldSizeSlider.setSnapToTicks(true);
        worldSizeSlider.setPaintLabels(true);
        worldSizeSlider.setPaintTicks(true);
        worldSizeSlider.addChangeListener(this);

        /*
         * Food spinner and text label
         */
        maxFoodLabel = new JLabel();
        maxFoodLabel.setText(model.maxFoodAmount + " Maximum Food ");
        JPanel maxFoodContainer = new JPanel();
        maxFoodContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        maxFoodContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        //food label
        maxFoodSpinnerLabel = new JLabel("Desired Max Food Amount");
        //food spinner
        SpinnerModel maxFoodSpinnerModel = new SpinnerNumberModel(model.desiredMaxFoodAmount, 0, 100000, 10);
        maxFoodSpinner = new JSpinner(maxFoodSpinnerModel);
        maxFoodSpinner.setEditor(new JSpinner.NumberEditor(maxFoodSpinner, "#"));
        maxFoodSpinner.addChangeListener(this);
        //add both to container
        maxFoodContainer.add(maxFoodSpinner);
        maxFoodContainer.add(maxFoodSpinnerLabel);

        /*
         * Toggles for aging and debug
         */
        doAgingToggle = new JCheckBox(" Kill organisms older than ", model.useLifespan);
        doAgingToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        doAgingToggle.addChangeListener(this);
        //
        debugToggle = new JCheckBox(" Show Debug View ", view.DEBUG_MODE);
        debugToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        debugToggle.addChangeListener(this);

        /*
         * Statistics for averages
         */
        averagesStatisticsLabel = new JLabel("Statistics");
        averageEnergy = new JLabel(Strings.averageEnergyText);
        averageWeight = new JLabel(Strings.averageWeightText);
        averageFoodEaten = new JLabel(Strings.averageFoodEatenText);
        averageNumChildren = new JLabel(Strings.averageNumChildrenText);
        averageEnergySpentPerFood = new JLabel(Strings.averageEnergySpentPerFoodText);
        averageAge = new JLabel(Strings.averageAgeText);
        newestGeneration = new JLabel(Strings.newestGenerationText);
        
        /*
         * Statistics for individual organisms check boxes
         */
        bestOrganismStatisticsLabel = new JLabel(Strings.bestOrganismStatisticsLabelText);
        //
        box_mostFoodEaten = new JCheckBox(Strings.mostFoodEatenText, view.showGluttony);
        box_mostFoodEaten.setAlignmentX(Component.LEFT_ALIGNMENT);
        box_mostFoodEaten.addChangeListener(this);
        //
        box_mostChildren = new JCheckBox(Strings.mostChildrenText, view.showLust);
        box_mostChildren.setAlignmentX(Component.LEFT_ALIGNMENT);
        box_mostChildren.addChangeListener(this);
        //
        box_mostEfficient = new JCheckBox(Strings.mostEfficientText, view.showSloth);
        box_mostEfficient.setAlignmentX(Component.LEFT_ALIGNMENT);
        box_mostEfficient.addChangeListener(this);
        //
        box_oldest = new JCheckBox(Strings.oldestText, view.showPride);
        box_oldest.setAlignmentX(Component.LEFT_ALIGNMENT);
        box_oldest.addChangeListener(this);
        //
        box_oldestGeneration = new JCheckBox(Strings.oldestGenerationText, view.showEnvy);
        box_oldestGeneration.setAlignmentX(Component.LEFT_ALIGNMENT);
        box_oldestGeneration.addChangeListener(this);


        /*
         * Add components and labels to the right container
         */
        rightContainer.add(textLabel);
        rightContainer.add(Box.createVerticalStrut(10));
        //
        rightContainer.add(worldSizeLabel);
        rightContainer.add(worldSizeSliderLabel);
        rightContainer.add(worldSizeSlider);
        rightContainer.add(Box.createVerticalStrut(10));
        //
        rightContainer.add(averagesStatisticsLabel);
        rightContainer.add(averageEnergy);
        rightContainer.add(averageWeight);
        rightContainer.add(averageFoodEaten);
        rightContainer.add(averageNumChildren);
        rightContainer.add(averageEnergySpentPerFood);
        rightContainer.add(averageAge);
        rightContainer.add(newestGeneration);
        rightContainer.add(Box.createVerticalStrut(10));
        //
        rightContainer.add(bestOrganismStatisticsLabel);
        rightContainer.add(box_mostFoodEaten);
        rightContainer.add(box_mostChildren);
        rightContainer.add(box_mostEfficient);
        rightContainer.add(box_oldest);
        rightContainer.add(box_oldestGeneration);
        rightContainer.add(Box.createVerticalStrut(10));
        //
        rightContainer.add(maxFoodLabel);
        rightContainer.add(maxFoodContainer);
        //
        rightContainer.add(doAgingToggle);
        rightContainer.add(debugToggle);

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
        worldMenu = new JMenu("World");
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
        worldMenu.addSeparator();
        // Clear world organisms
        clearOrganisms = new JMenuItem("Clear Organisms");
        clearOrganisms.addActionListener(this);
        worldMenu.add(clearOrganisms);
        // Clear world food
        clearFood = new JMenuItem("Clear Food");
        clearFood.addActionListener(this);
        worldMenu.add(clearFood);
        // Pause Play
        pausePlay = new JMenuItem("Pause/Resume");
        pausePlay.addActionListener(this);
        worldMenu.add(pausePlay);

        
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

        String widthFormatted = String.format("%.2f", model.width);
        String heightFormatted = String.format("%.2f", model.height);
        String labelText = String.format("%s (%s wide, %s high)", Strings.worldSizeLabelText, widthFormatted, heightFormatted);
        worldSizeLabel.setText(labelText);

        String desiredWidthLabel;
        if (model.width > (model.desiredWidth + 1.0)) {
            desiredWidthLabel = "(Shrinking to Desired Width " + String.format("%.2f", model.desiredWidth) + ")";
        }
        else if (model.width < (model.desiredWidth - 1.0)) {
            desiredWidthLabel = "(Expanding to Desired Width " + String.format("%.2f", model.desiredWidth) + ")";
        }
        else {
            desiredWidthLabel = "(Reached Desired Width " + String.format("%.2f", model.desiredWidth) + ")";
        }
        worldSizeSliderLabel.setText(Strings.worldSizeSliderLabelText + desiredWidthLabel);

        maxFoodLabel.setText(model.maxFoodAmount + " Maximum Food");

        doAgingToggle.setText(" Kill organisms older than " + model.lifespan);

        LogElement topLog;
        try{
            topLog = statistics.getTopLogElement();
            averageEnergy.setText(Strings.averageEnergyText + String.format("%.3f", topLog.averageEnergy));
            averageWeight.setText(Strings.averageWeightText + String.format("%.3f", topLog.averageWeight));
            averageFoodEaten.setText(Strings.averageFoodEatenText + topLog.averageNumFoodEaten);
            averageNumChildren.setText(Strings.averageNumChildrenText + topLog.averageNumChildren);
            averageEnergySpentPerFood.setText(Strings.averageEnergySpentPerFoodText + String.format("%.3f", topLog.averageEnergySpentPerFood));
            averageAge.setText(Strings.averageAgeText + topLog.averageAge);
            newestGeneration.setText(Strings.newestGenerationText + topLog.newestGeneration);
        }catch (Exception e) {
            //System.out.println("Error with basic statistics labels. Mainwindow { public void update(){}}");
        }
        try{
            box_mostFoodEaten.setText(Strings.mostFoodEatenText + statistics.getTopLogElement().mostNumFoodEaten);
            box_mostChildren.setText(Strings.mostChildrenText + statistics.getTopLogElement().mostNumChildrenAmount);
            box_mostEfficient.setText(Strings.mostEfficientText + String.format("%.3f", statistics.getTopLogElement().lowestEnergySpentPerFoodValue));
            box_oldest.setText(Strings.oldestText + statistics.getTopLogElement().oldestOrganismAge);
            box_oldestGeneration.setText(Strings.oldestGenerationText + statistics.getTopLogElement().oldestGeneration);
        } catch (Exception e) {
            //System.out.println("Error with checkboxes. MainWindow { public void update(){}}");
        } 
        //Note: These try-catch blocks throw a lot of errors upon startup because there are no logElements in the statistics object until later FIXME
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == speedSlider && !isExternalUpdate) { //TODO remove
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();

            controller.setSpeed(value);
        } else if (e.getSource() == maxFoodSpinner) {
            JSpinner source = (JSpinner) e.getSource();
            model.maxFoodAmount = (int) source.getValue();
        }

        else if (e.getSource() == worldSizeSlider) {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            model.desiredWidth = value;
        }

        else if (e.getSource() == box_mostFoodEaten) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.showGluttony = source.isSelected();
        }
        else if (e.getSource() == box_mostChildren) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.showLust = source.isSelected();
        }
        else if (e.getSource() == box_mostEfficient) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.showSloth = source.isSelected();
        }
        else if (e.getSource() == box_oldest) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.showPride = source.isSelected();
        }
        else if (e.getSource() == box_oldestGeneration) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.showEnvy = source.isSelected();
        }

        else if (e.getSource() == doAgingToggle) {
            JCheckBox source = (JCheckBox) e.getSource();
            model.useLifespan = source.isSelected();
        }
        else if (e.getSource() == debugToggle) {
            JCheckBox source = (JCheckBox) e.getSource();
            view.DEBUG_MODE = source.isSelected();
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
        } 
        
        else if (e.getSource() == exportStatisticsItem) {
            System.out.println("Export statistics button clicked. Not Implemented");
            //TODO
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
            System.out.println("Save world to file button clicked. Not implemented.");
            //TODO
        }
        
        
        else if (e.getSource() == createOrganism) {
            //new CreateCreatureView(controller, worldModel.getWidth(), worldModel.getHeight());
            // TODO create a random organism, preferably in the center of the screen to allow for user "placement"
            System.out.println("Create organism button clicked");
        } 

        else if (e.getSource() == loadOrganismFromFile) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a text file");

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BaseOrganism o = FileLoader.readOrganismFromFile(selectedFile, model);
                    controller.addOrganism(o);
                    System.out.println("Loaded a new organism into world.");
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        else if (e.getSource() == clearOrganisms) {
            System.out.println("Clear organisms button clicked");
            controller.clearOrganisms();
        }

        else if (e.getSource() == clearFood) { //TODO maybe replace with a restart world command
            System.out.println("Clear food button clicked");
        }

        else if (e.getSource() == pausePlay) {
            controller.togglePaused();
        }

        else if (e.getSource() == loadWorldFromFile) {
            System.out.println("Load world from file button clicked");
        }

        else if (e.getSource() == showStatisticsItem) {
            new StatisticsView(new ArrayList<>(statistics.getLog()));
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

