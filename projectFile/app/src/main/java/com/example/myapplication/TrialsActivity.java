package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrialsActivity extends AppCompatActivity {

    public String currentUid;
    public String currentOwner;
    public String exp_category;
    public String exp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials);

        Intent intent = getIntent();
        currentUid = intent.getStringExtra("uid");
        currentOwner = intent.getStringExtra("owner");
        exp_category = intent.getStringExtra("exp_category");
        exp_name = intent.getStringExtra("exp_name");

        Button back = findViewById(R.id.back_button);
        Button statistics = findViewById(R.id.statistics_button);
        Button histogram = findViewById(R.id.histogram_button);
        Button plot = findViewById(R.id.plot_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}