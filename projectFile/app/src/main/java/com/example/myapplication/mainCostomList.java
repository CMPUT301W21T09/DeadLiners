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

public class mainCostomList extends ArrayAdapter<Experiment> {

    private Context context;
    private ArrayList<Experiment> experimentsArrayList;

    public mainCostomList(@NonNull Context context, ArrayList<Experiment> experimentsArrayList) {
        super(context, 0, experimentsArrayList);

        this.context = context;
        this.experimentsArrayList = experimentsArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.experimentcontent, parent, false);
        }

        Experiment experiment = experimentsArrayList.get(position);
        TextView experimentName = convertView.findViewById(R.id.experiment_name);
        TextView description = convertView.findViewById(R.id.description_text);
        TextView trails = convertView.findViewById(R.id.NumberOfTrials_Text);

        experimentName.setText();
        description.setText();
        trails.setText();

        return convertView;
    }
}
