package com.analytics;

import com.analytics.gui.AnalyticsGUI;

import javax.swing.SwingUtilities;

/**
 * Predictive Analysis Project
 * This is a simple project that analyzes CSV files and builds prediction models
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("Starting analysis...");

        //Launch the GUI
        SwingUtilities.invokeLater(() -> {
            try {
                new AnalyticsGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error launching GUI: " + e.getMessage());
            }
        });

    }
}
