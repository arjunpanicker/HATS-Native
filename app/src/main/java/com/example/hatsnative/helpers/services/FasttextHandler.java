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
        Log.i(LOG_TAG, "Loading fasttext model to convert");

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
            default:
                return "None";
        }
    }
}
