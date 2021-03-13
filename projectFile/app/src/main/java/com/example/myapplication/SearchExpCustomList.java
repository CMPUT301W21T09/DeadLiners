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

public class SearchExpCustomList extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;

    public SearchExpCustomList(Context context, ArrayList<Experiment> experiments){
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_exp_content, parent,false);
        }

        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.exp_name_text);

        experimentName.setText(experiment.getExpName());

        return view;

    }
}
