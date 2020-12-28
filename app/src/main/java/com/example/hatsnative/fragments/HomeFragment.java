package com.example.hatsnative.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hatsnative.utility.CommandDialog;
import com.example.hatsnative.R;
import com.skyfishjy.library.RippleBackground;

public class HomeFragment extends Fragment implements CommandDialog.CommandDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RippleBackground rippleBackground;


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
        // TODO: Get the command here and complete the function
        Toast.makeText(getActivity(), command, Toast.LENGTH_SHORT).show();
    }
}