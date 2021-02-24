package com.example.hatsnative.fragments;

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
import androidx.fragment.app.Fragment;

import com.example.hatsnative.R;
import com.example.hatsnative.helpers.CommandDialog;
import com.example.hatsnative.helpers.services.DatasetHandler;
import com.example.hatsnative.helpers.services.FasttextHandler;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements CommandDialog.CommandDialogListener {

    private RippleBackground rippleBackground;
    private AssetManager assetManager;
    private final String fasttextModelFileName = "ft_model.ftz";


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
            InputStream inputStream = assetManager.open(fasttextModelFileName);
            String prediction = FasttextHandler.getPrediction(inputStream, command);
            Log.i("Personal", prediction);
            Toast.makeText(getActivity(), prediction, Toast.LENGTH_LONG).show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
//        predict(command);
//        predictClass(command);
    }

    private void predictClass(String command) {
        InputStream is = getResources().openRawResource(R.raw.home_auto_dataset);
        ArrayList<String> csvResult = DatasetHandler.readCsv(is);
        loadCsvData(csvResult);
    }

    private void predict(String command) {
        String str = predictNative(command, assetManager);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    // Native Method declarations
    private native String predictNative(String command, AssetManager assetManager);
    private native void loadCsvData(ArrayList<String> csvData);
}