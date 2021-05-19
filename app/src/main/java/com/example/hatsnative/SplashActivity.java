package com.example.hatsnative;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hatsnative.helpers.Constants;
import com.example.hatsnative.helpers.services.DatasetHandler;
import com.example.hatsnative.helpers.services.Utility;
import com.example.hatsnative.helpers.services.ml.FasttextHandler;
import com.example.hatsnative.helpers.services.ml.net.DNN;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.contentLoadingProgressBar);
        loadMlModel();
    }

    private void loadMlModel() {
        new MLModelLoad().execute("");
    }

    private class MLModelLoad extends AsyncTask<String, Void, String> {
        private AssetManager assetManager;
        @Override
        protected String doInBackground(String... strings) {
            assetManager = getAssets();

            try {
                InputStream ft_inputstream = assetManager.open(Constants.FT_MODEL_FILE);
                InputStream homedata_inputstream = assetManager.open(Constants.MAIN_DATASET_FILE);

                HashMap<String, Vector<String>> dataset = DatasetHandler.readCSV(homedata_inputstream);
                Vector<Vector<Integer>> y =  Utility.OneHotEncoder(dataset.get("label"));

                // Load fasttext model and create an instance of fasttext class
                FasttextHandler fasttextHandler = FasttextHandler.getInstance(ft_inputstream);

                Vector<Vector<Double>> sentVectors = fasttextHandler.getSentenceVectors(dataset.get("commands"));

                JSONObject weights = DatasetHandler.readWeights(App.getContext());
                // Train the neural network
                DNN dnnObject = DNN.getInstance(weights);

                if (weights == null) {
                    dnnObject.fit(sentVectors, y);
                }

                return "Done";
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
