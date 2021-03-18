package com.example.hatsnative.models.ml;

import com.example.hatsnative.helpers.services.ml.net.Neuron;

import java.util.Vector;

public interface INet {
    void feedForward(Vector<Double> inputVals);
    void backProp(Vector<Integer> targetVals);
    Vector<Double> getResults();
    void getWeights(Vector<Double> weightVals);

    static void ouputLayerSoftmaxFunction(Vector<Neuron> outputLayer) {
        Vector<Double> expValues = new Vector<>();
        double sumExp = 0.0;

        for (int n = 0; n < outputLayer.size() - 1; ++n) {
            double expVal = Math.exp(outputLayer.get(n).getOutputVal());
            sumExp += expVal;
            expValues.add(expVal);
        }

        for (int n = 0; n < outputLayer.size() - 1; ++n) {
            outputLayer.get(n).setOutputVal(expValues.get(n) / sumExp);
        }
    }
}
