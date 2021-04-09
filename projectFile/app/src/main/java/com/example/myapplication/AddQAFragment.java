package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//fragment to generate a new QuestionOrReply object
public class AddQAFragment extends DialogFragment {
    private EditText description;
    private OnAddFragmentInteractionListener listener;

    public interface OnAddFragmentInteractionListener {
        void onOKPressed(String description);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnAddFragmentInteractionListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_qa_fragment, null);
        description = view.findViewById(R.id.descriptionEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
            .setView(view)
            .setTitle("Add a Question/Reply")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String description_str = description.getText().toString();
                    listener.onOKPressed(description_str);
                }
            }).create();
    }
}
