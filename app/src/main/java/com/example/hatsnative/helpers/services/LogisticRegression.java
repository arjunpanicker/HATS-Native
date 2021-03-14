package com.example.hatsnative.helpers.services;

import android.util.Log;

import com.example.hatsnative.helpers.InvalidLengthException;

import java.util.Vector;

public class LogisticRegression {
    private double learning_rate;
    private int iterations;
    private int train_len = 0, num_features = 0;
    private Vector<Double> weights = new Vector<>();
    private double bias = 0;
    private Vector<Vector<Double>> X = new Vector<>();
    private Vector<Vector<String>> y = new Vector<>();

    private static final String TAG_NAME = LogisticRegression.class.getName();


    public LogisticRegression(double learning_rate, int iterations) {
        this.learning_rate = learning_rate;
        this.iterations = iterations;
    }

    public void fit(Vector<Vector<Double>> X, Vector<Vector<String>> y) {
        // Number of training samples, features (default word embedding size is 150)
        this.train_len = X.size();
        this.num_features = 150;

        // Initialize the weight vector with all random in range [0,1] for length = num_features
        for (int i = 0; i < num_features; i++) {
            this.weights.add(Math.random());
        }

        this.X = X;
        this.y = y;

        for (int i = 0; i < this.iterations; i++) {

        }
    }

    private void updateWeights() {
        for (int i = 0; i < this.X.size(); i++) {
            try {
                // Transfer function: Z = X*W + b
                double Z = dotProduct(this.X.get(i), this.weights) + this.bias;

                // Activation function: Softmax
            } catch (InvalidLengthException invalidLengthException) {
                Log.e(TAG_NAME, invalidLengthException.getMessage());
            }

        }
    }

    private double dotProduct(Vector<Double> v1, Vector<Double> v2) throws InvalidLengthException {
        if (v1.size() != v2.size()) {
            throw new InvalidLengthException("The lengths of the two Vectors do not match..!!");
        }

        double sum = 0d;

        for (int i = 0; i < v1.size(); i++) {
            sum += (v1.get(i) * v2.get(i));
        }

        return sum;
    }
}
