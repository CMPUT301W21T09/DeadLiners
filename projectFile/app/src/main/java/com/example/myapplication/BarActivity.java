package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BarActivity extends AppCompatActivity {

    private BarChart bar;
    List<BarEntry> list;
    public String exp_category;
    public String exp_name;
    public int pass;
    public int fail;
    public int countSum;
    public int intMax = 0;
    public int processList[] = new int[10000];

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        bar = findViewById(R.id.barChart);
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


        if (exp_category.equals("count")) {
            collectionReference = db.collection("CountDataset");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        if (expName.equals(exp_name) && !ignore) {
                            countSum += 1;
                        }
                    }
                    list.add(new BarEntry(2, countSum));
                    ValueFormatter countFormatter = new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            if (value == 2) {
                                return "Count";
                            }
                            return "";
                        }
                    };
                    bar.getXAxis().setAxisMaximum(3);
                    bar.getXAxis().setAxisMinimum(1);
                    bar.getXAxis().setValueFormatter(countFormatter);
                    BarDataSet barDataSet = new BarDataSet(list, "");
                    barDataSet.setColor(Color.RED);
                    BarData barData = new BarData(barDataSet);
                    bar.setData(barData);
                    barData.setBarWidth(0.5f);
                    bar.getXAxis().setLabelCount(5, false);
                    bar.getDescription().setEnabled(false);
                    bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    bar.getAxisRight().setEnabled(false);
                }
            });

        } else if (exp_category.equals("binomial")) {
            collectionReference = db.collection("BinomialDataSet");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        String variable = (String) doc.getData().get("value");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        if (expName.equals(exp_name) && variable.equals("pass") && !ignore) {
                            pass += 1;
                        } else if (expName.equals(exp_name) && variable.equals("fail") && !ignore) {
                            fail += 1;
                        }
                    }
                    list.add(new BarEntry(2, pass));
                    list.add(new BarEntry(3, fail));
                    ValueFormatter binomialFormatter = new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            if (value == 2) {
                                return "Pass";
                            }
                            if (value == 3) {
                                return "Fail";
                            }
                            return "";
                        }
                    };
                    bar.getXAxis().setAxisMaximum(4);
                    bar.getXAxis().setAxisMinimum(1);
                    bar.getXAxis().setValueFormatter(binomialFormatter);
                    BarDataSet barDataSet = new BarDataSet(list, "");
                    barDataSet.setColor(Color.RED);
                    BarData barData = new BarData(barDataSet);
                    bar.setData(barData);
                    barData.setBarWidth(0.5f);
                    bar.getXAxis().setLabelCount(5, false);
                    bar.getDescription().setEnabled(false);
                    bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    bar.getAxisRight().setEnabled(false);
                }
            });
        } else if (exp_category.equals("intCount")) {
            collectionReference = db.collection("IntCountDataset");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        String variable = (String) doc.getData().get("value");
                        if (expName.equals(exp_name) && !ignore) {
                            processList[Integer.parseInt(variable)]++;
                            if(intMax < Integer.parseInt(variable)) {
                                intMax = Integer.parseInt(variable);
                            }
                        }
                    }
                    for (int i = 0; i < intMax + 1; i++) {
                        list.add(new BarEntry(i, processList[i]));
                    }


                    ValueFormatter intCountFormatter = new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            if(Math.ceil((double) value) == value) {
                                return String.valueOf(value);
                            }
                            return "";
                        }
                    };

                    bar.getXAxis().setAxisMaximum(intMax+1);
                    bar.getXAxis().setAxisMinimum(-1);
                    bar.getXAxis().setValueFormatter(intCountFormatter);
                    BarDataSet barDataSet = new BarDataSet(list, "");
                    barDataSet.setColor(Color.RED);
                    BarData barData = new BarData(barDataSet);
                    bar.setData(barData);
                    barData.setBarWidth(0.5f);
                    bar.getXAxis().setLabelCount(5, false);
                    bar.getDescription().setEnabled(false);
                    bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    bar.getAxisRight().setEnabled(false);

                }
            });
        } else {
            collectionReference = db.collection("MeasurementDataset");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        String expName = (String) doc.getData().get("expName");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        String variable = (String) doc.getData().get("value");
                        if (expName.equals(exp_name) && !ignore) {

                            processList[Math.round(Float.parseFloat(variable))]++;
                            if(intMax < Math.round(Float.parseFloat(variable))) {
                                intMax = Math.round(Float.parseFloat(variable));
                            }
                        }
                    }
                    for (int i = 0; i < intMax + 1; i++) {
                        list.add(new BarEntry(i, processList[i]));
                    }


                    ValueFormatter intCountFormatter = new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            if(Math.ceil((double) value) == value) {
                                return String.valueOf(value);
                            }
                            return "";
                        }
                    };

                    bar.getXAxis().setAxisMaximum(intMax+1);
                    bar.getXAxis().setAxisMinimum(-1);
                    bar.getXAxis().setValueFormatter(intCountFormatter);
                    BarDataSet barDataSet = new BarDataSet(list, "");
                    barDataSet.setColor(Color.RED);
                    BarData barData = new BarData(barDataSet);
                    bar.setData(barData);
                    barData.setBarWidth(0.5f);
                    bar.getXAxis().setLabelCount(5, false);
                    bar.getDescription().setEnabled(false);
                    bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    bar.getAxisRight().setEnabled(false);

                    note.setText("This the number of measurements within the range of !, for example, 19.1 will be counted as 19, 19.8 will be counted as 20");
                }
            });
        }
    }
}