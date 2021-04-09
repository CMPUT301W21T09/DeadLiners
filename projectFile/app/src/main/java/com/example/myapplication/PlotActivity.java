package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    private LineChart line;
    List<Entry> list;
    public String exp_category;
    public String exp_name;
    public int processList[] = new int[10000];
    public String dateList[] = new String[10000];
    public float measurementList[] = new float[10000];
    public int length = 0;

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        line = findViewById(R.id.lineChart);
        list = new ArrayList<>();

        TextView note = findViewById(R.id.note);

        Intent intent = getIntent();
        exp_category = intent.getStringExtra("exp_category");
        exp_name = intent.getStringExtra("exp_name");


        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference;

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (exp_category.equals("binomial")) {
            collectionReference = db.collection("BinomialDataSet");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        String variable = (String) doc.getData().get("value");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        if (expName.equals(exp_name) && variable.equals("pass") && !ignore) {
                            processList[length] = 1;
                            String dateString = (String) doc.getData().get("time");
                            dateList[length] = dateString;


                            length++;
                        } else if (expName.equals(exp_name) && variable.equals("fail") && !ignore) {
                            processList[length] = 0;
                            String dateString = (String) doc.getData().get("time");
                            dateList[length] = dateString;

                            length++;
                        }
                    }

                    for(int i = 0; i < length; i++) {
                        for(int j = 0; j < length-i-1; j++) {
                            String d1 = dateList[j];
                            String d2 = dateList[j+1];
                            if (d1.compareTo(d2) > 0) {
                                String temp = dateList[j];
                                dateList[j] = dateList[j+1];
                                dateList[j+1] = temp;
                                int tempInt = processList[j];
                                processList[j] = processList[j+1];
                                processList[j+1] = tempInt;
                            }
                        }
                    }

                    for(int i = 0; i < length; i++) {
                        list.add(new Entry(i, processList[i]));
                    }
                    LineDataSet lineDataSet = new LineDataSet(list, "");
                    lineDataSet.setColor(Color.RED);
                    LineData lineData = new LineData(lineDataSet);
                    line.setData(lineData);
                    line.getXAxis().setLabelCount(5, false);
                    line.getDescription().setEnabled(false);
                    line.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    line.getAxisRight().setEnabled(false);
                }
            });
        } else if (exp_category.equals("intCount")) {

            collectionReference = db.collection("IntCountDataset");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        String variable = (String) doc.getData().get("value");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        if (expName.equals(exp_name) && !ignore) {
                            processList[length] = Integer.parseInt(variable);
                            String dateString = (String) doc.getData().get("time");
                            dateList[length] = dateString;
                            length++;
                        }
                    }
                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < length - i - 1; j++) {
                            String d1 = dateList[j];
                            String d2 = dateList[j + 1];
                            if (d1.compareTo(d2) > 0) {
                                String temp = dateList[j];
                                dateList[j] = dateList[j + 1];
                                dateList[j + 1] = temp;
                                int tempInt = processList[j];
                                processList[j] = processList[j + 1];
                                processList[j + 1] = tempInt;
                            }
                        }
                    }

                    for (int i = 0; i < length; i++) {
                        list.add(new Entry(i, processList[i]));
                    }
                    LineDataSet lineDataSet = new LineDataSet(list, "");
                    lineDataSet.setColor(Color.RED);
                    LineData lineData = new LineData(lineDataSet);
                    line.setData(lineData);
                    line.getXAxis().setLabelCount(5, false);
                    line.getDescription().setEnabled(false);
                    line.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    line.getAxisRight().setEnabled(false);
                }
            });

        } else if (exp_category.equals("measurement")){

            collectionReference = db.collection("MeasurementDataset");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        String variable = (String) doc.getData().get("value");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        if (expName.equals(exp_name) && !ignore) {
                            measurementList[length] = Float.parseFloat(variable);
                            String dateString = (String) doc.getData().get("time");
                            dateList[length] = dateString;
                            length++;
                        }
                    }
                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < length - i - 1; j++) {
                            String d1 = dateList[j];
                            String d2 = dateList[j + 1];
                            if (d1.compareTo(d2) > 0) {
                                String temp = dateList[j];
                                dateList[j] = dateList[j + 1];
                                dateList[j + 1] = temp;
                                float tempFloat = measurementList[j];
                                measurementList[j] = measurementList[j + 1];
                                measurementList[j + 1] = tempFloat;
                            }
                        }
                    }

                    for (int i = 0; i < length; i++) {
                        list.add(new Entry(i, measurementList[i]));
                    }

                    LineDataSet lineDataSet = new LineDataSet(list, "");
                    lineDataSet.setColor(Color.RED);
                    LineData lineData = new LineData(lineDataSet);
                    line.setData(lineData);
                    line.getXAxis().setLabelCount(5, false);
                    line.getDescription().setEnabled(false);
                    line.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    line.getAxisRight().setEnabled(false);

                }
            });
        } else {
            note.setText("It is not meaningful to use plot chart in Count-Based experiment");
        }
    }
}