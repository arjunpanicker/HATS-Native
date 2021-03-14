package com.example.hatsnative.models.ml;

import com.example.hatsnative.helpers.services.ml.net.Neuron;

import java.util.Vector;

public interface INeuron {

    double sumDOW(Vector<Neuron> nextLayer);
    void feedForward(Vector<Neuron> prevLayer, boolean isOutputLayer);
    void calcOutputGradients(double targetVal);
    void calcHiddenGradients(Vector<Neuron> nextLayer);
    void updateInputWeights(Vector<Neuron> prevLayer);

    static double transferFunction(double x) {
        return Math.tanh(x);
    }
    static double transferFunctionDerivative(double x) {
        return 1 - (x*x);
    }

}
