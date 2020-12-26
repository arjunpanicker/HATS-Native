package com.example.hatsnative;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CommandDialog extends AppCompatDialogFragment {

    private EditText editTextCommand;
    private CommandDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.command_dialog, null);



        builder.setView(view)
                .setTitle("Command")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String command = editTextCommand.getText().toString();

                        listener.applyText(command);
                    }
                });

        editTextCommand = view.findViewById(R.id.edit_command);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CommandDialogListener) getTargetFragment();
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + "must implement CommandDialogListener");
        }

    }

    public interface CommandDialogListener {
        void applyText(String command);
    }

}
