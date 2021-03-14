package com.example.hatsnative.helpers.services.ml.logistic;

import java.util.ArrayList;
import java.util.HashMap;

public class MultinomialLogisticRegression {
    private double learning_rate = 0.005;
    private int max_epochs = 100;
    private ArrayList<ArrayList<Double>> weights;

    MultinomialLogisticRegression(HashMap<String, Double> params,
                                  ArrayList<ArrayList<Double>> weights) {
        this.learning_rate = params.get("learning rate");
        this.max_epochs =  Integer.parseInt(params.get("epochs").toString());

        if (!weights.isEmpty()) {
            this.weights = weights;
        } else {
//
        }
    }
}
