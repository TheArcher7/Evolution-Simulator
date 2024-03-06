package main.java.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import main.java.statistics.LogElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StatisticsView extends JFrame implements ActionListener {
    private JFreeChart chart;
    private JPanel backPanel;
    private ChartPanel chartPanel;
    private JPanel downPanel;
    private JCheckBox allCheckBox;
    private JCheckBox[] checkBoxes;

    private List<LogElement> log;

    private String[] statLabels = {"Population"};
    private Color[] statColors = {new Color(255, 85, 85)};

    // Constructor
    public StatisticsView(List<LogElement> logElements) {
        log = logElements;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setTitle("Statistics");

        initUI(logElements);

        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void initUI(List<LogElement> logElements) {
        backPanel = new JPanel();

        allCheckBox = new JCheckBox("Show All", true);
        allCheckBox.addActionListener(this);
        
        checkBoxes = new JCheckBox[statLabels.length];
        for (int k = 0; k < statLabels.length; k++) {
            checkBoxes[k] = new JCheckBox(statLabels[k], true);
            checkBoxes[k].addActionListener(this);
        }

        XYDataset dataset = createDataset(logElements, false);
        List<Color> colors = getColors();

        chart = createChart(dataset, colors);
        chartPanel = new ChartPanel(chart);

        backPanel.setLayout(new BorderLayout());
        backPanel.add(chartPanel, BorderLayout.CENTER);

        downPanel = new JPanel();
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));
        
        downPanel.add(allCheckBox);
        for (JCheckBox box : checkBoxes) {
            downPanel.add(box);
        }

        backPanel.add(downPanel, BorderLayout.SOUTH);

        setContentPane(backPanel);
    }

    private XYDataset createDataset(List<LogElement> logElements, boolean relative) {
        XYSeries[] allSeries = new XYSeries[statLabels.length];

        for (int k = 0; k < allSeries.length; k++) {
            allSeries[k] = new XYSeries(statLabels[k]);
        }

        double current = 0;
        for (LogElement element : logElements) {
            allSeries[0].add(current, element.population);
            //add more stats here. Also add stat label and new color
            current++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (int k = 0; k < allSeries.length; k++) {
            if (checkBoxes[k].isSelected()) {
                dataset.addSeries(allSeries[k]);
            }
        }

        return dataset;
    }

    private List<Color> getColors() {
        List<Color> colors = new ArrayList<>();

        for (int k = 0; k < checkBoxes.length; k++) {
            if (checkBoxes[k].isSelected()) {
                colors.add(statColors[k]);
            }
        }

        return colors;
    }

    private JFreeChart createChart(final XYDataset dataset, List<Color> colors) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Statistics",
                "Time",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        for (int k = 0; k < colors.size(); k++) {
            plot.getRenderer().setSeriesPaint(k, colors.get(k));
        }

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JCheckBox) {
            JCheckBox selectedBox = (JCheckBox) source;

            if (selectedBox == allCheckBox) {
                for (JCheckBox checkBox : checkBoxes) {
                    checkBox.setSelected(selectedBox.isSelected());
                }
            }

            List<Color> colors = getColors();
            XYDataset dataset = createDataset(log, false);
            chart = createChart(dataset, colors);

            backPanel.remove(chartPanel);

            chartPanel = new ChartPanel(chart);

            backPanel.add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        }
    }
}
