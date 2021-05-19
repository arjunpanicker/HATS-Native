package com.example.hatsnative.helpers.services.ml.net;

import com.example.hatsnative.models.ml.EKernelWeightInitializer;
import com.example.hatsnative.models.ml.IConnection;

public class Connection implements IConnection {
    private double weight;
    private double deltaWeight;
    private int m_myIndex;

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Connection(int index) {
        this.weight = IConnection.init_weights(EKernelWeightInitializer.GLOROT_UNIFORM);
        this.m_myIndex = index;
    }

    public String toJson() {
        return this.m_myIndex + ": " + this.getWeight();
    }
}
