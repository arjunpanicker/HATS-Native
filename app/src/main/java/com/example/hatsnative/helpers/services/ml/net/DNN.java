package com.example.hatsnative.helpers.services.ml.net;

import android.util.Log;

import com.example.hatsnative.helpers.Constants;
import com.example.hatsnative.helpers.services.Utility;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DNN {
    private int inputDims;
    private int outputDims;
    private Vector<Integer> topology = new Vector<>();

    private static DNN instance = null;

    private DNN(int inputDims, int outputDims) {
        this.inputDims = inputDims;
        this.outputDims = outputDims;
        this.topology.add(inputDims);
        this.topology.add(outputDims);
    }

    public static DNN getInstance() {
        if (instance == null) {
            instance = new DNN(Constants.inputDims, Constants.outputDims);
        }

        return instance;
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

        // Save the weights of the model
        saveModelWeights(myNet);
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


        if (checkPredictionThreshold(outputResults)) {
            labelOheVec.set(Utility.argmax(outputResults), 1);
            return reverseLabelMap.get(labelOheVec);
        } else {
            return "Other";
        }
    }

    private static boolean checkPredictionThreshold(Vector<Double> dlist) {
        double maxVal = dlist.get(0);
        for (int i = 1; i < dlist.size(); ++i) {
            if (dlist.get(i) > maxVal) {
                maxVal = dlist.get(i);
            }
        }

        return maxVal >= Constants.PREDICTION_THRESHOLD;
    }

    /**
     * Saves the model weights into a file to be loaded during the next app startup
     */
    public void saveModelWeights(Net net) {
        LinkedHashMap<Integer, List<List<Double>>> weights = net.getWeights();

    }
}
