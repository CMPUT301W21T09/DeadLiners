package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Experiment> experimentsArrayList;
    public ArrayAdapter<Experiment> experimentsArrayAdapter;

    private ImageButton button_user;
    private ImageButton button_add;
    private ListView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup buttons and listview
        button_add = findViewById(R.id.imageButton_add);
        button_user = findViewById(R.id.imageButton_user);

        mainScrollView = findViewById(R.id.mainScrollView);

        experimentsArrayList = new ArrayList<Experiment>();
        experimentsArrayAdapter = new mainCostomList(this, experimentsArrayList);

        mainScrollView.setAdapter(experimentsArrayAdapter);

    }

    public void addButtonClicked(View view){
        // do something when add is clicked
    }

    public void userButtonClicked(View view){
        //do something when user is clicked

    }


}