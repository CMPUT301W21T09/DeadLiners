package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsActivity extends AppCompatActivity {

    TextView mean_text;
    TextView median_text;
    TextView stdev_text;
    TextView quartile1_text;
    TextView quartile2_text;
    TextView quartile3_text;

    private String exp_name;
    private String exp_category;

    public ArrayList<Integer> results = new ArrayList<Integer>();
    public ArrayList<Float> measurement_result = new ArrayList<>();

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mean_text = findViewById(R.id.mean_content);
        median_text = findViewById(R.id.median_content);
        stdev_text = findViewById(R.id.stdev_content);
        quartile1_text = findViewById(R.id.quartile1_content);
        quartile2_text = findViewById(R.id.quartile2_content);
        quartile3_text = findViewById(R.id.quartile3_content);

        Button back = findViewById(R.id.back_button);

        Intent intent = getIntent();
        exp_name = intent.getStringExtra("exp_name");
        exp_category = intent.getStringExtra("exp_category");

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



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


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value) {
                    String expName = (String) doc.getData().get("expName");
                    Boolean ignore = (Boolean) doc.getData().get("ignore");
                    if(expName.equals(exp_name) && !ignore) {
                        String variable = (String) doc.getData().get("value");
                        if(exp_category.equals("binomial") && variable.equals("pass")) {
                            results.add(1);
                        }
                        else if (exp_category.equals("binomial") && variable.equals("fail")) {
                            results.add(0);
                        }
                        else if (exp_category.equals("intCount")) {
                            results.add(Integer.parseInt(variable));
                        }
                        else if (exp_category.equals("measurement")) {
                            measurement_result.add(Float.parseFloat(variable));
                        }
                    }
                }

                if (exp_category.equals("measurement")) {
                    Collections.sort(measurement_result);
                    float sum = 0;
                    float stdev = 0;
                    double quartile1 = 0;
                    double quartile3 = 0;
                    double median = 0;
                    for(int i = 0; i < measurement_result.size(); i++) {
                        sum += measurement_result.get(i);
                    }
                    for(int i = 0; i < measurement_result.size(); i++) {
                        stdev += Math.pow(measurement_result.get(i) - (sum/measurement_result.size()), 2);
                    }
                    mean_text.setText(String.valueOf(sum/measurement_result.size()));
                    stdev_text.setText(String.valueOf(Math.sqrt(stdev/measurement_result.size())));

                    if(measurement_result.size() % 2 == 0) {
                        median = (measurement_result.get(measurement_result.size()/2 - 1) + measurement_result.get(measurement_result.size()/2))*1.0/2;
                        median_text.setText(String.valueOf(median));
                        quartile2_text.setText(String.valueOf(median));
                    }
                    else {
                        median = measurement_result.get(measurement_result.size()/2);
                        median_text.setText(String.valueOf(median));
                        quartile2_text.setText(String.valueOf(median));
                    }

                    ArrayList<Float> lowerList = new ArrayList<Float>();
                    ArrayList<Float> higherList = new ArrayList<Float>();
                    if(measurement_result.size() % 2 == 0) {
                        for(int i = 0; i <= measurement_result.size() / 2 - 1; i++) {
                            lowerList.add(measurement_result.get(i));
                        }
                        for(int i = measurement_result.size() / 2 ; i <= measurement_result.size() - 1; i++) {
                            higherList.add(measurement_result.get(i));
                        }
                    }
                    else {
                        for(int i = 0; i <= measurement_result.size() / 2; i++) {
                            lowerList.add(measurement_result.get(i));
                        }
                        for(int i = measurement_result.size() / 2 + 1; i <= measurement_result.size() - 1; i++) {
                            higherList.add(measurement_result.get(i));
                        }
                    }

                    if(lowerList.size() % 2 == 0) {
                        quartile1 = (lowerList.get(lowerList.size()/2 - 1) + lowerList.get(lowerList.size()/2))*1.0/2;
                        quartile1_text.setText(String.valueOf(quartile1));
                    }
                    else {
                        quartile1 = (lowerList.get(lowerList.size()/2));
                        quartile1_text.setText(String.valueOf(quartile1));
                    }
                    if(higherList.size() % 2 == 0) {
                        quartile3 = (higherList.get(higherList.size()/2 - 1) + higherList.get(higherList.size()/2))*1.0/2;
                        quartile3_text.setText(String.valueOf(quartile3));
                    }
                    else {
                        quartile3 = (higherList.get(higherList.size()/2));
                        quartile3_text.setText(String.valueOf(quartile3));
                    }

                }
                else if(exp_category.equals("binomial") || exp_category.equals(("intCount"))) {
                    Collections.sort(results);
                    float sum = 0;
                    float stdev = 0;
                    double quartile1 = 0;
                    double quartile3 = 0;
                    double median = 0;
                    for(int i = 0; i < results.size(); i++) {
                        sum += results.get(i);
                    }
                    for(int i = 0; i < results.size(); i++) {
                        stdev += Math.pow(results.get(i) - (sum/results.size()), 2);
                    }
                    mean_text.setText(String.valueOf(sum/results.size()));
                    stdev_text.setText(String.valueOf(Math.sqrt(stdev/results.size())));

                    if(results.size() % 2 == 0) {
                        median = (results.get(results.size()/2 - 1) + results.get(results.size()/2))*1.0/2;
                        median_text.setText(String.valueOf(median));
                        quartile2_text.setText(String.valueOf(median));
                    }
                    else {
                        median = results.get(results.size()/2);
                        median_text.setText(String.valueOf(median));
                        quartile2_text.setText(String.valueOf(results.get(results.size()/2)));
                    }


                    ArrayList<Integer> lowerList = new ArrayList<Integer>();
                    ArrayList<Integer> higherList = new ArrayList<Integer>();
                    if(results.size() % 2 == 0) {
                        for(int i = 0; i <= results.size() / 2 - 1; i++) {
                            lowerList.add(results.get(i));
                        }
                        for(int i = results.size() / 2 ; i <= results.size() - 1; i++) {
                            higherList.add(results.get(i));
                        }
                    }
                    else {
                        for(int i = 0; i <= results.size() / 2; i++) {
                            lowerList.add(results.get(i));
                        }
                        for(int i = results.size() / 2 + 1; i <= results.size() - 1; i++) {
                            higherList.add(results.get(i));
                        }
                    }

                    if(lowerList.size() % 2 == 0) {
                        quartile1 = ((lowerList.get(lowerList.size()/2 - 1) + lowerList.get(lowerList.size()/2)) * 1.0 / 2);
                        quartile1_text.setText(String.valueOf(quartile1));
                    }
                    else {
                        quartile1 = (lowerList.get(lowerList.size()/2));
                        quartile1_text.setText(String.valueOf(quartile1));
                    }
                    if(higherList.size() % 2 == 0) {
                        quartile3 = (higherList.get(higherList.size()/2 - 1) + higherList.get(higherList.size()/2))*1.0/2;
                        quartile3_text.setText(String.valueOf(quartile3));
                    }
                    else {
                        quartile3 = (higherList.get(higherList.size()/2));
                        quartile3_text.setText(String.valueOf(quartile3));
                    }

                }

            }
        });

    }
}