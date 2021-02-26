package com.example.hatsnative.helpers.services.ml;

import java.util.Vector;

public class DNN {
    private int inputDims;
    private int outputDims;

    public DNN(int inputDims, int outputDims) {
        this.inputDims = inputDims;
        this.outputDims = outputDims;
    }

    public Net fit(Vector<Vector<Double>> X_train, Vector<Vector<Integer>> y_train) {
        Vector<Integer> topology = new Vector<Integer>(this.inputDims, this.outputDims);
        Net myNet = new Net(topology);

        int epochs = 10;

        for (int i = 0; i < epochs; ++i) {
            for (int j = 0; j < X_train.size(); ++j) {
                myNet.feedForward(X_train.get(j));

                myNet.backProp(y_train.get(j));
            }
        }

        return myNet;
    }

    public void predict(String command, Net classifier) {

    }
}
