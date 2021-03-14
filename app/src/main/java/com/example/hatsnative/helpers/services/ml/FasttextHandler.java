package com.example.hatsnative.helpers.services.ml;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import fasttext.FastText;
import fasttext.FastTextPrediction;

public class FasttextHandler {
    private static final String LOG_TAG = "FasttextHandler";
    private static FastText model = null;

    private static FasttextHandler instance = null;

    private FasttextHandler(InputStream inputStream) {
        loadModel(inputStream);
    }

    public static FasttextHandler getInstance(InputStream inputStream) {
        if (instance == null) {
            instance = new FasttextHandler(inputStream);
        }

        return instance;
    }

    public List<fasttext.Vector> getSentenceVector(List<String> data) {
        List<List<String>> commands = new ArrayList<>();
        for (String str : data) {
            commands.add(Arrays.asList(str.split("\\s")));
        }

        List<fasttext.Vector> sentVec = model.getSentenceVectors(commands);
        return sentVec;
    }

    /**
     * Predicts the class label using only the fasttext model.
     * @param command
     * @return predicted class
     */
    public static String getPrediction(String command) {
        FastTextPrediction label = null;
        label = model.predict(command);

        String predictedLabel = labelMapper(label.label());
        return predictedLabel;
    }

    /**
     * Loads the fasttext model from disk
     * @param inputStream
     */
    private static void loadModel(InputStream inputStream) {
        try {
            model = FastText.loadModel(inputStream);
        } catch (IOException ioException) {
            Log.d(LOG_TAG, ioException.getMessage());
        }

    }

    private static final String labelMapper(String label) {
        switch (label) {
            case "__label__light_on":
                return "light on";
            case "__label__light_off":
                return "light off";
            case "__label__geyser_on":
                return "geyser on";
            case "__label__geyser_off":
                return "geyser off";
            case "__label__ac_on":
                return "ac on";
            case "__label__ac_off":
                return "ac off";
            case "__label__tv_on":
                return "tv on";
            case "__label__tv_off":
                return "tv off";
            case "__label__fan_on":
                return "fan on";
            case "__label__fan_off":
            default:
                return "Other";
        }
    }
}
