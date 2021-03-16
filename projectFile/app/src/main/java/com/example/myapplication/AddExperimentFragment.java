package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddExperimentFragment extends DialogFragment {
    private EditText expname;
    private EditText description;
    private EditText category;
    private EditText region;
    private EditText numOfTrails;
    private OnFragmentInteractionListener listener;
    private Button count;
    private Button binomial;
    private Button intCount;
    private Button measurement;
    private String uid;

    AddExperimentFragment(String uid){
        this.uid = uid;
    }
    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_experiment_fragment_layout, null);
        expname = view.findViewById(R.id.Name_editText);
        description = view.findViewById(R.id.description_editText);
        category = view.findViewById(R.id.category_editText);
        region = view.findViewById(R.id.Region_editText);
        numOfTrails = view.findViewById(R.id.Number_Of_Trails_editText);

        Button count = (Button) view.findViewById(R.id.Count);
        Button binomial = (Button) view.findViewById(R.id.Binomial);
        Button intCount = (Button) view.findViewById(R.id.IntCount);
        Button measurement = (Button) view.findViewById(R.id.Measure);

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("count");
            }
        });
        binomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("binomial");
            }
        });
        intCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("intCount");
            }
        });
        measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("measurement");
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String expName = expname.getText().toString();
                        String experimentDescription = description.getText().toString();
                        String expCategory = category.getText().toString();
                        String expRegion = region.getText().toString();
                        String expNumOfTrails = numOfTrails.getText().toString();
                        listener.onOkPressed(new Experiment(expName, experimentDescription,expCategory,expRegion,expNumOfTrails,uid));
                    }}).create();
    }
}
