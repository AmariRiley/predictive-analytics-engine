package com.analytics;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.NumericToNominal;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles data loading, cleaning, and basic analysis
 */

public class DataProcessor {
    private Instances data;
    private Map<String, String> dataStats;

    public DataProcessor() {
        dataStats = new HashMap<>();
    }

    //Create a method that loads the data from the CSV file
    public boolean loadData(String filePath) {
        try {
            CSVLoader load = new CSVLoader();
            load.setSource(new File(filePath));
            data = load.getDataSet();

            if (data.numInstances() == 0) {
                System.err.println("No data found in the file");
                return false;
            }

            System.out.println("Loaded " + data.numInstances() + " rows and " + data.numAttributes() + " columns");
            return true;

        } catch (IOException exception) {
            System.err.println("Error loading data: " + exception.getMessage());
            return false;
        }
    }

//Create a method to clean the data by handling missing values

    public Instances cleanData() {
        if (data == null) {
            return null;
        }
        try {
            //Replace the missing values with mean/mode
            ReplaceMissingValues replace = new ReplaceMissingValues();
            replace.setInputFormat(data);
            Instances cleaned = Filter.useFilter(data, replace);

            System.out.println("Data cleaned successfully");
            return cleaned;
        } catch (Exception e) {
            System.err.println("Error cleaning data: " + e.getMessage());
            return data;
        }
    }

    //Create a method that generates basic statistics about the dataset
    public Map<String, String> generateStats() {
        if (data == null) {
            return dataStats;
        }
        dataStats.clear();
        dataStats.put("Total Rows", String.valueOf(data.numInstances()));
        dataStats.put("Total Columns", String.valueOf(data.numAttributes()));

        //Count the numeric and categorical attributes
        int numeric = 0;
        int categorical = 0;

        for (int i = 0; i < data.numAttributes(); i++) {
            if (data.attribute(i).isNumeric()) {
                numeric++;
            } else {
                categorical++;
            }
        }

        dataStats.put("Numeric Columns", String.valueOf(numeric));
        dataStats.put("Categorical Columns", String.valueOf(categorical));

        //List the column names
        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < data.numAttributes(); i++) {
            columns.append(data.attribute(i).name());
            if (i < data.numAttributes() - 1) {
                columns.append(", ");
            }
        }
        dataStats.put("Column Names", columns.toString());

        return dataStats;
    }

    //Create a method to set the target column for a prediction
    public void setTargetColumn(String target) {
        if (data == null) {
            return;
        }

        for (int i = 0; i < data.numAttributes(); i++) {
            if (data.attribute(i).name().equals(target)) {
                data.setClassIndex(i);
                System.out.println("Target column set to: " + target);
                return;
            }
        }
        System.err.println("Column not found: " + target);
    }

    public Instances getData() {
        return data;
    }
}