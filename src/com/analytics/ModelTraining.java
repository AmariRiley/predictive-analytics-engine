package com.analytics;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class trains different ML models and compares their performance
 */

public class ModelTraining {
    private Map<String, Double> accuracy;
    private Map<String, String> summary;

    public ModelTraining() {
        accuracy = new HashMap<>();
        summary = new HashMap<>();
    }

    //Trains multiple models and compare their performance
    public Map<String, Double> trainModel(Instances data) {
        if (data == null || data.classIndex() == -1) {
            System.err.println("Data not properly set up for training");
            return accuracy;
        }

        accuracy.clear();
        summary.clear();

        //Determine if this is classification or regression
        boolean isClass = !data.classAttribute().isNumeric();

        System.out.println("Training " + (isClass ? "classification" : "regression") + " models...");

        //Train both models
        if (isClass) {
            trainClassificationModels(data);
        } else {
            trainRegressionModels(data);
        }

        return accuracy;
    }

    //Create a private void method to train classification models (for predicting categories)
    private void trainClassificationModels(Instances data) {
        //Logistic Regression Model
        trainOneModel(new Logistic(), "Logistic Regression", data);

        //Random Forest Model
        trainOneModel(new RandomForest(), "Random Forest", data);
    }

    //Create a private void method for training regression models (for predicting numbers)
    private void trainRegressionModels(Instances data) {
        //Linear Regression Model
        trainOneModel(new LinearRegression(), "Linear Regression", data);

        //Random Forest Model for Regression
        trainOneModel(new RandomForest(), "Random Forest (Regression)", data);
    }

    //Create a private void method to train a single model and evaluate its performance
    private void trainOneModel(Classifier classifier, String name, Instances data) {
        try {
            //Build the model
            classifier.buildClassifier(data);

            //Evaluate using cross-validation (split the data into parts to test)
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(classifier, data, 10, new Random(1));

            //Get the accuracy
            double accurate;
            String summarize;

            if (data.classAttribute().isNumeric()) {
                //For regression, utilize the correlation coefficient
                accurate = eval.correlationCoefficient();
                summarize = String.format("Correlation: %.3f, Mean Error: %.3f", accurate, eval.meanAbsoluteError());
            } else {
                //For classification, utilize a percentage correction
                accurate = eval.pctCorrect();
                summarize = String.format("Accuracy: %.2f%%, Precision: %.3f, Recall: %.3f", accurate, eval.weightedPrecision(), eval.weightedRecall());
            }

            accuracy.put(name, accurate);
            summary.put(name, summarize);

            System.out.println(name + " - " + summarize);
        } catch (Exception e) {
            System.err.println("Error training " + name + ": " + e.getMessage());
            accuracy.put(name, 0.0);
            summary.put(name, "Training failed: " + e.getMessage());
        }
    }

    //Create a method to return the name of the best performing model based on the evaluations
    public String getBestModel() {
        if (accuracy.isEmpty()) {
            return "No models trained, so there is not a best model.";
        }

        String bestModel = "";
        double score = -1;

        for (Map.Entry<String, Double> entry : accuracy.entrySet()) {
            if (entry.getValue() > score) {
                score = entry.getValue();
                bestModel = entry.getKey();
            }
        }

        return bestModel + " (Score: " + String.format("%.3f", score) + ")";
    }

    public Map<String, String> getSummary() {
        return summary;
    }
}
