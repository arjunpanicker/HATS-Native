package com.example.hatsnative.helpers.services.ml.net;

import android.util.Log;

import com.example.hatsnative.helpers.services.Utility;

import java.util.Map;
import java.util.Vector;

public class DNN {
    private int inputDims;
    private int outputDims;
    private Vector<Integer> topology = new Vector<>();

    public DNN(int inputDims, int outputDims) {
        this.inputDims = inputDims;
        this.outputDims = outputDims;
        this.topology.add(inputDims);
        this.topology.add(outputDims);
    }

    /**
     * Trains a neural network for classification
     * @param X_train
     * @param y_train
     */
    public void fit(Vector<Vector<Double>> X_train, Vector<Vector<Integer>> y_train) {
        Net myNet = Net.getInstance(this.topology);

        int epochs = 100;

        for (int i = 0; i < epochs; ++i) {
            for (int j = 0; j < X_train.size(); ++j) {
                myNet.feedForward(X_train.get(j));

                myNet.backProp(y_train.get(j));
            }
        }
    }

    /**
     * Predicts the class label
     * @param sentenceVector
     * @return predicted class label (String)
     */
    public String predict(Vector<Double> sentenceVector) {
        Net myNet = Net.getInstance(this.topology);

        // Feed forward and get results
        myNet.feedForward(sentenceVector);
        Vector<Double> outputResults = myNet.getResults();

        Map<Vector<Integer>, String> reverseLabelMap = Utility.reverseLabelMap;

        for (int i = 0; i < outputResults.size(); ++i) {
            Log.d("Personal Result", outputResults.get(i).toString());
        }

        // Initialize an empty label OHE vector with all 0s
        Vector<Integer> labelOheVec = Utility.getEmptyOHEVector();

        Log.d("Personal", labelOheVec.size() + " " + Utility.argmax(outputResults));
        labelOheVec.set(Utility.argmax(outputResults), 1);

        return reverseLabelMap.get(labelOheVec);
    }
}
