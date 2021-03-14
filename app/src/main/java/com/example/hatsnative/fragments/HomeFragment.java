package com.example.hatsnative.fragments;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.hatsnative.R;
import com.example.hatsnative.helpers.CommandDialog;
import com.example.hatsnative.helpers.services.DatasetHandler;
import com.example.hatsnative.helpers.services.Utility;
import com.example.hatsnative.helpers.services.ml.FasttextHandler;
import com.example.hatsnative.helpers.services.PreprocessingHandler;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class HomeFragment extends Fragment implements CommandDialog.CommandDialogListener {

    private RippleBackground rippleBackground;
    private AssetManager assetManager;
    private final String fasttextModelFileName = "ft_model.ftz";
    private final String stopwordsFilename = "stop_words.txt";
    private final String shorttextFilename = "sms_translations.csv";
    private final String homeAutoDatasetFilename = "home_auto_dataset.csv";


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
            InputStream ft_inputstream = assetManager.open(fasttextModelFileName);
            InputStream stopwords_inputstream = assetManager.open(stopwordsFilename);
            InputStream shorttext_inputstream = assetManager.open(shorttextFilename);
            InputStream homedata_inputstream = assetManager.open(homeAutoDatasetFilename);

            HashMap<String, Vector<String>> dataset = DatasetHandler.readCSV(homedata_inputstream);
            Vector<Vector<Integer>> y =  Utility.OneHotEncoder(dataset.get("label"));

            PreprocessingHandler preprocessingHandler =
                    new PreprocessingHandler(stopwords_inputstream, shorttext_inputstream);
            String preprocessedCommand = preprocessingHandler.pipeline(command);

            FasttextHandler fasttextHandler = FasttextHandler.getInstance(ft_inputstream);
            String prediction = fasttextHandler.getPrediction(preprocessedCommand);

            List<fasttext.Vector> sentVectors = fasttextHandler.getSentenceVector(dataset.get("commands"));
            
            new AlertDialog.Builder(getActivity())
                    .setTitle("Prediction Result")
                    .setMessage("Predicted: " + prediction)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Personal", "Prediction displayed!!");
                        }
                    }).show();
            
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
//        predict(command);
//        predictClass(command);
    }

    private void predictClass(String command) {
//        InputStream is = getResources().openRawResource(R.raw.home_auto_dataset);
//        ArrayList<String> csvResult = DatasetHandler.readCsv(is);
//        loadCsvData(csvResult);
    }

    private void predict(String command) {
        String str = predictNative(command, assetManager);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    // Native Method declarations
    private native String predictNative(String command, AssetManager assetManager);
    private native void loadCsvData(ArrayList<String> csvData);
}