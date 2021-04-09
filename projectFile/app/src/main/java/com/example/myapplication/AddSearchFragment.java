package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddSearchFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private Button userSearch;
    private Button expSearch;
    private String uid;


    AddSearchFragment(String uid){
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_search_fragment_layout, null);

        userSearch = (Button) view.findViewById(R.id.button_user);
        expSearch = (Button) view.findViewById(R.id.button_exp);
        Button intCount = (Button) view.findViewById(R.id.IntCount);
        Button measurement = (Button) view.findViewById(R.id.Measure);

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getActivity(), SearchUserActivity.class);
                startActivity(intent);
            }
        });
        expSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getActivity(), SearchExperimentActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("SEARCH")
                .setNegativeButton("Cancel", null).create();
    }
}
