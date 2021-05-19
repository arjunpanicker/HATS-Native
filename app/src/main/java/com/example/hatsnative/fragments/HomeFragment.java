package com.example.hatsnative.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.hatsnative.R;
import com.example.hatsnative.helpers.CommandDialog;
import com.example.hatsnative.helpers.Constants;
import com.example.hatsnative.helpers.services.PreprocessingHandler;
import com.example.hatsnative.helpers.services.ml.FasttextHandler;
import com.example.hatsnative.helpers.services.ml.net.DNN;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements CommandDialog.CommandDialogListener {

    private RippleBackground rippleBackground;
    private AssetManager assetManager;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView ivCommandImage = fragView.findViewById(R.id.iv_commandImage);
        ivCommandImage.setOnClickListener(this::enterCommandHandler);

        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rippleBackground = getView().findViewById(R.id.rippleButton);
        rippleBackground.startRippleAnimation();
    }

    public void enterCommandHandler(View view) {
        CommandDialog commandDialog = new CommandDialog();
        commandDialog.setTargetFragment(HomeFragment.this, 300);
        commandDialog.show(getFragmentManager(), "command dialog");
    }

    @Override
    public void applyText(String command) {
        assetManager = getActivity().getAssets();
        try {
            InputStream ft_inputstream = assetManager.open(Constants.FT_MODEL_FILE);
            InputStream stopwords_inputstream = assetManager.open(Constants.STOP_WORDS_FILE);
            InputStream shorttext_inputstream = assetManager.open(Constants.SHORT_TEXT_FILE);

            PreprocessingHandler preprocessingHandler =
                    new PreprocessingHandler(stopwords_inputstream, shorttext_inputstream);
            String preprocessedCommand = preprocessingHandler.pipeline(command);

            // Load fasttext model and create an instance of fasttext class
            FasttextHandler fasttextHandler = FasttextHandler.getInstance(ft_inputstream);

//            JSONObject weights = DatasetHandler.readWeights(getActivity());
            // Predict using neural network
            DNN dnnObject = DNN.getInstance(null);
            String predictionNeuralNet = dnnObject.predict(fasttextHandler.getSentenceVector(preprocessedCommand));
            
            new AlertDialog.Builder(getActivity())
                    .setTitle("Prediction Result")
                    .setMessage(predictionNeuralNet)
                    .setPositiveButton("OK", (dialog, which) -> Log.d("Personal", "Prediction displayed!!")).show();
            
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Native Method declarations
    private native String predictNative(String command, AssetManager assetManager);
    private native void loadCsvData(ArrayList<String> csvData);
}