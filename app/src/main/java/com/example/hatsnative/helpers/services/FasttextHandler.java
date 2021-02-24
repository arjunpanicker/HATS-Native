package com.example.hatsnative.helpers.services;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import fasttext.FastText;
import fasttext.FastTextPrediction;

public class FasttextHandler {
    private static final String LOG_TAG = "FasttextHandler";

    public static String getPrediction(InputStream inputStream, String command) {
        FastTextPrediction label = null;

        try {
            FastText model = FastText.loadModel(inputStream);
            label = model.predict(command);
        } catch (IOException ioException) {
            Log.d(LOG_TAG, ioException.getMessage());
        }

        String predictedLabel = labelMapper(label.label());
        return predictedLabel;
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
