package com.example.hatsnative.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hatsnative.helpers.CommandDialog;
import com.example.hatsnative.R;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;

public class HomeFragment extends Fragment implements CommandDialog.CommandDialogListener {

    private RippleBackground rippleBackground;

    private AssetManager assetManager;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetManager = getResources().getAssets();
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
        // TODO: Get the command here and complete the function
        Toast.makeText(getActivity(), command, Toast.LENGTH_SHORT).show();
        predict(command);
    }

    private void predict(String command) {
//        assetManager.openFd("stop_words.txt").getFileDescriptor();
        String str = predictNative(command, assetManager);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    // Native Method declarations
    private native String predictNative(String command, AssetManager assetManager);
}