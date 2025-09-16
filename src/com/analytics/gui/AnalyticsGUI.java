package com.analytics.gui;

import com.analytics.DataProcessor;
import com.analytics.ModelTraining;
import com.analytics.Report;
import weka.core.Instances;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

/**
 * This class creates a simple GUI for the project
 * It will be a window for the users to interact with the prediction tool
 */

public class AnalyticsGUI extends JFrame {
    private DataProcessor processor;
    private ModelTraining train;
    private Report report;

    private JTextArea resultsArea;
    private JTextField text;
    private JComboBox<String> column;
    private JButton loadButton;
    private JButton analyzeButton;
    private JButton trainButton;
    private JButton reportButton;

    public AnalyticsGUI() {
        //Initialize components
        processor = new DataProcessor();
        train = new ModelTraining();
        report = new Report();

        setupGUI();
        setupEventListeners();
        initializeButtons();
    }

    private void setupGUI() {
        setTitle("Predictive Analytics Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        //Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Top panel - File selection
        JPanel topPanel = createFileSelectionPanel();

        //Middle panel - Controls
        JPanel controlPanel = createControlPanel();

        //Bottom Panel - Results
        JPanel resultsPanel = createResultsPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(resultsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupEventListeners() {
        analyzeButton.addActionListener(e -> analyzeData());
        trainButton.addActionListener(e -> trainModels());
        reportButton.addActionListener(e -> generateReport());
    }

    private void initializeButtons() {
        analyzeButton.setEnabled(false);
        trainButton.setEnabled(false);
        reportButton.setEnabled(false);
    }

    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("1. Select Data File"));

        text = new JTextField(30);
        JButton browseButton = new JButton("Browse");
        loadButton = new JButton("Load Data");

        browseButton.addActionListener(e -> browseForFile());
        loadButton.addActionListener(e -> loadData());

        panel.add(new JLabel("File:"));
        panel.add(text);
        panel.add(browseButton);
        panel.add(loadButton);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));

        //Target the column selection
        JPanel target = new JPanel(new FlowLayout(FlowLayout.LEFT));
        target.setBorder(BorderFactory.createTitledBorder("2. Select Target Column"));
        column = new JComboBox<>();
        column.setEnabled(false);
        target.add(new JLabel("Predict: "));
        target.add(column);

        //Create buttons for analysis
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBorder(BorderFactory.createTitledBorder("3. Run Analysis"));

        analyzeButton = new JButton("Analyze Data");
        trainButton = new JButton("Train Models");
        reportButton = new JButton("Generate Report");

        buttons.add(analyzeButton);
        buttons.add(trainButton);
        buttons.add(reportButton);

        panel.add(target);
        panel.add(buttons);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));

        resultsArea = new JTextArea(15, 50);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane pane = new JScrollPane(resultsArea);
        panel.add(pane, BorderLayout.CENTER);

        return panel;
    }

    private void browseForFile() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        if (chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooseFile.getSelectedFile();
            text.setText(file.getAbsolutePath());
        }
    }

    private void loadData() {
        String filePath = text.getText().trim();
        if (filePath.isEmpty()) {
            showMessage("Please select a file first.");
            return;
        }

        resultsArea.setText("Loading data...\n");

        //Disable buttons during loading data
        loadButton.setEnabled(false);
        analyzeButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return processor.loadData(filePath);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        populateTargetColumn();
                        analyzeButton.setEnabled(true);
                        resultsArea.append("Data loaded successfully!\n");
                    } else {
                        resultsArea.append("Failed to load data. Check file format.\n");
                    }
                } catch (Exception e) {
                    resultsArea.append("Error: " + e.getMessage() + "\n");
                } finally {
                    loadButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void populateTargetColumn() {
        column.removeAllItems();
        Instances data = processor.getData();
        if (data != null) {
            for (int i = 0; i < data.numAttributes(); i++) {
                column.addItem(data.attribute(i).name());
            }
            column.setEnabled(true);
        }
    }

    private void analyzeData() {
        resultsArea.append("\n=== DATA ANALYSIS ===\n");

        analyzeButton.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                return null;
            }

            @Override
            protected void done() {
                Map<String, String> stats = processor.generateStats();
                for (Map.Entry<String, String> entry : stats.entrySet()) {
                    resultsArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
                }
                trainButton.setEnabled(true);
                analyzeButton.setEnabled(true);
                resultsArea.append("\nReady to train models!\n");
            }
        };
        worker.execute();
    }

    private void trainModels() {
        String columnTarget = (String) column.getSelectedItem();
        if (columnTarget == null) {
            showMessage("Please select a target column.");
            return;
        }

        resultsArea.append("\n=== MODEL TRAINING ===\n");
        resultsArea.append("Target column: " + columnTarget + "\n");

        //Disable button during training
        trainButton.setEnabled(false);

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                try {
                    publish("Setting target column...\n");
                    processor.setTargetColumn(columnTarget);

                    publish("Cleaning data...\n");
                    Instances cleanData = processor.cleanData();

                    if (cleanData != null) {
                        publish("Training models...\n");
                        train.trainModel(cleanData);
                        publish("Training completed!\n");
                    } else {
                        publish("Error: Could not clean data\n");
                    }
                } catch (Exception e) {
                    publish("Error during training: " + e.getMessage() + "\n");
                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> list) {
                for (String message : list) {
                    resultsArea.append(message);
                }
            }

            @Override
            protected void done() {
                try {
                    Map<String, String> summaries = train.getSummary();
                    for (Map.Entry<String, String> entry : summaries.entrySet()) {
                        resultsArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
                    }

                    resultsArea.append("\nBest Model: " + train.getBestModel() + "\n");
                    reportButton.setEnabled(true);
                } catch (Exception e) {
                    resultsArea.append("Error getting training results: " + e.getMessage() + "\n");
                } finally {
                    trainButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void generateReport() {
        resultsArea.append("\n=== GENERATING REPORT ===\n");

        //Disable button during report generation
        reportButton.setEnabled(false);

        SwingWorker<String, String> worker = new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                    publish("Generating report...\n");
                    return report.generateReport(processor.generateStats(), train.getSummary(), train.getBestModel());
                } catch (Exception e) {
                    publish("Error generating report: " + e.getMessage() + "\n");
                    return null;
                }
            }

            @Override
            protected void process(java.util.List<String> list) {
                for (String message : list) {
                    resultsArea.append(message);
                }
            }

            @Override
            protected void done() {
                try {
                    String reportPath = get();
                    if (reportPath != null) {
                        resultsArea.append("Report saved to: " + reportPath + "\n");
                        resultsArea.append("Analysis complete!\n");
                    } else {
                        resultsArea.append("Failed to generate report.\n");
                    }
                } catch (Exception e) {
                    resultsArea.append("Error: " + e.getMessage() + "\n");
                } finally {
                    reportButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
