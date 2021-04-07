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

public class trialsInfo_user extends AppCompatActivity {

    private String value;
    private String expName;
    private String experimenter;
    private Boolean ignore;
    private String time;
    private String exp_category;

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials_info_user);

        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        expName = intent.getStringExtra("expName");
        experimenter = intent.getStringExtra("experimenter");
        ignore = intent.getBooleanExtra("ignore",false);
        time = intent.getStringExtra("time");
        exp_category = intent.getStringExtra("exp_category");

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference;

        if (exp_category.equals("count")) {
            collectionReference = db.collection("CountDataset");
        }
        else if (exp_category.equals("intCount")) {
            collectionReference = db.collection("IntCountDataset");
        }
        else if (exp_category.equals("binomial")) {
            collectionReference = db.collection("BinomialDataSet");
        }
        else {
            collectionReference = db.collection("MeasurementDataset");
        }

        Button back = findViewById(R.id.back_button);
        Button delete = findViewById(R.id.delete_button);

        Switch ignore = findViewById(R.id.ignore_switch);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectionReference
                        .document("Trail of "+ experimenter + " at " + time)
                        .delete();
                finish();
            }
        });

    }

}