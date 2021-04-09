package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TrialsCustomList extends ArrayAdapter<Trial> {
    private ArrayList<Trial> trial;
    private Context context;

    public TrialsCustomList(Context context, ArrayList<Trial> trial){
        super(context, 0, trial);
        this.trial = trial;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.trials_list_context, parent,false);
        }

        Trial current_trial = trial.get(position);
        String time = current_trial.getTime();
        String experimenter = current_trial.getExperimenter();

        TextView text = view.findViewById(R.id.trials_text);

        text.setText(experimenter + " on " + time);

        return view;

    }
}
