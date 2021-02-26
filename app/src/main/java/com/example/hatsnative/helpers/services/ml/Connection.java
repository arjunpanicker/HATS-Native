package com.example.hatsnative.helpers.services.ml;

import com.example.hatsnative.models.ml.EKernelWeightInitializer;
import com.example.hatsnative.models.ml.IConnection;

public class Connection implements IConnection {
    private double weight;
    private double deltaWeight;

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

    public Connection() {
        this.weight = IConnection.init_weights(EKernelWeightInitializer.GLOROT_UNIFORM);
    }
}
