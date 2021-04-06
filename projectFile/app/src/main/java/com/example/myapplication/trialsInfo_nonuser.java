package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class trialsInfo_nonuser extends AppCompatActivity {

    private String value;
    private String experimenter;
    private String time;

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials_info_nonuser);

        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        experimenter = intent.getStringExtra("experimenter");
        time = intent.getStringExtra("time");


        Button back = findViewById(R.id.back_button);

        TextView value_text = findViewById(R.id.trial_content);
        TextView publisher_text = findViewById(R.id.trial_publisher_content);
        TextView time_text = findViewById(R.id.trial_time_content);

        value_text.setText(value);
        publisher_text.setText(experimenter);
        time_text.setText(time);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}