package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class experimentInfo extends AppCompatActivity {
    private Experiment experiment;

    private Button qrCode;
    private Button subscribe;
    private Button questionForum;
    private Button viewTrails;
    private Button addTrail;
    private Button back;

    private TextView experimentName;
    private TextView description;
    private TextView owner;
    private TextView category;
    private TextView region;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experimentinfo_user);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");

        experimentName = findViewById(R.id.experimentName);
        description = findViewById(R.id.Description);
        owner = findViewById(R.id.Owner);
        category = findViewById(R.id.category);
        region = findViewById(R.id.Region);
        status = findViewById(R.id.Status);

        qrCode = findViewById(R.id.QR_code);
        subscribe = findViewById(R.id.Subscribe);
        questionForum = findViewById(R.id.Question_Forum);
        viewTrails = findViewById(R.id.View_Trials);
        addTrail = findViewById(R.id.Add_Trial);
        back = findViewById(R.id.Back);

        experimentName.setText(experiment.getExpName());
        description.setText(experiment.getDescription());
        //owner.setText(experiment.getOwnerName);
        category.setText(experiment.getCategory());
        region.setText(experiment.getRegion());
        status.setText(experiment.getPublished());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}