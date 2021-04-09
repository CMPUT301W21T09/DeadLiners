package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrialsActivity extends AppCompatActivity {

    public String currentUid;
    public String currentOwner;
    public String exp_category;
    public String exp_name;

    public int trailNum = 0;

    private ListView trialsList;
    private ArrayAdapter<Trial> trialsAdapter;
    private ArrayList<Trial> trialsDataList;

    final String TAG = "Sample";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trials);

        Intent intent = getIntent();
        currentUid = intent.getStringExtra("uid");
        currentOwner = intent.getStringExtra("owner");
        exp_category = intent.getStringExtra("exp_category");
        exp_name = intent.getStringExtra("exp_name");

        trialsList = findViewById(R.id.trials_list);

        trialsDataList = new ArrayList<>();
        trialsAdapter = new TrialsCustomList(this, trialsDataList);
        trialsList.setAdapter(trialsAdapter);

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


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                trialsDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String expName = (String) doc.getData().get("expName");
                    if(expName.equals(exp_name)) {
                        trailNum += 1;
                        String experimenter = (String) doc.getData().get("experimenter");
                        String time = (String) doc.getData().get("time");
                        Boolean ignore = (Boolean) doc.getData().get("ignore");
                        String variable;
                        if(exp_category.equals("count")) {
                            variable = "";
                        }
                        else {
                            variable = (String) doc.getData().get("value");
                        }
                        Trial trial = new Trial(experimenter, expName, time, variable, ignore);
                        trialsDataList.add(trial);
                    }
                }
                trialsAdapter.notifyDataSetChanged();
            }
        });

        trialsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Trial trial = (Trial) trialsList.getItemAtPosition(position);
                String trialsUid = trial.getExperimenter();
                // means the user is owner
                if (currentOwner.equals(currentUid)) {
                    Intent intent = new Intent(TrialsActivity.this, trialsInfo_owner.class);
                    intent.putExtra("value", trial.getValue());
                    intent.putExtra("expName", trial.getExpName());
                    intent.putExtra("experimenter", trial.getExperimenter());
                    intent.putExtra("exp_category", exp_category);
                    intent.putExtra("ignore", trial.getIgnore());
                    intent.putExtra("time", trial.getTime());
                    startActivity(intent);
                } else if (currentUid.equals(trialsUid)) {
                    Intent intent = new Intent(TrialsActivity.this, trialsInfo_user.class);
                    intent.putExtra("value", trial.getValue());
                    intent.putExtra("expName", trial.getExpName());
                    intent.putExtra("experimenter", trial.getExperimenter());
                    intent.putExtra("exp_category", exp_category);
                    intent.putExtra("ignore", trial.getIgnore());
                    intent.putExtra("time", trial.getTime());
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(TrialsActivity.this, trialsInfo_nonuser.class);
                    intent.putExtra("value", trial.getValue());
                    intent.putExtra("expName", trial.getExpName());
                    intent.putExtra("experimenter", trial.getExperimenter());
                    intent.putExtra("exp_category", exp_category);
                    intent.putExtra("ignore", trial.getIgnore());
                    intent.putExtra("time", trial.getTime());
                    startActivity(intent);
                }
            }
        });

        statistics.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trailNum > 0) {
                    Intent intent = new Intent(TrialsActivity.this, StatisticsActivity.class);
                    intent.putExtra("exp_name", exp_name);
                    intent.putExtra("exp_category", exp_category);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(TrialsActivity.this, "Since there is no trials, no statistics available!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        histogram.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrialsActivity.this, BarActivity.class);
                intent.putExtra("exp_category", exp_category);
                intent.putExtra("exp_name", exp_name);
                startActivity(intent);

            }
        });

        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrialsActivity.this, PlotActivity.class);
                intent.putExtra("exp_category", exp_category);
                intent.putExtra("exp_name", exp_name);
                startActivity(intent);
            }
        });

    }

}