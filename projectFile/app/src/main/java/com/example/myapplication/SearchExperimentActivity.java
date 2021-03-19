package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchExperimentActivity extends AppCompatActivity {

    private ListView expSearchList;
    private ArrayAdapter<Experiment> expSearchAdapter;
    private ArrayList<Experiment> expSearchDataList;

    public String currentUid;

    private ArrayList<Experiment> results;

    private SearchExpCustomList customList;

    final String TAG = "Sample";
    Button searchExperimentButton;
    EditText searchExperimentEditText;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_experiment);

        Intent intent = getIntent();
        currentUid = intent.getStringExtra("uid");

        expSearchList = findViewById(R.id.experiment_list);
        searchExperimentButton = findViewById(R.id.search_experiment_button);
        searchExperimentEditText = findViewById(R.id.search_experiment_field);

        results = new ArrayList<>();

        expSearchDataList = new ArrayList<>();
        expSearchAdapter = new SearchExpCustomList(this, expSearchDataList);
        expSearchList.setAdapter(expSearchAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        Button back = findViewById(R.id.search_exp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = searchExperimentEditText.getText().toString();
                if(inputName.length() > 0) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            expSearchDataList.clear();
                            for(QueryDocumentSnapshot doc: value) {
                                String expName = (String) doc.getData().get("Name");
                                if(expName.toUpperCase().contains(inputName.toUpperCase())) {
                                    String name = doc.getId();
                                    String category = (String) doc.getData().get("category");
                                    String description = (String) doc.getData().get("description");
                                    String minimumTrails = (String) doc.getData().get("minimum_trails");
                                    String region = (String) doc.getData().get("region");
                                    String uid = (String) doc.getData().get("Owner");
                                    String ownerName = (String) doc.getData().get("OwnerName");
                                    Experiment experiment = new Experiment(name, description,category,region,minimumTrails,uid);
                                    experiment.setOwnerName(ownerName);
                                    expSearchDataList.add(experiment);
                                }
                            }
                            expSearchAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            expSearchDataList.clear();
                            for(QueryDocumentSnapshot doc: value) {
                                String name = doc.getId();
                                String category = (String) doc.getData().get("category");
                                String description = (String) doc.getData().get("description");
                                String minimumTrails = (String) doc.getData().get("minimum_trails");
                                String region = (String) doc.getData().get("region");
                                String uid = (String) doc.getData().get("Owner");
                                String ownerName = (String) doc.getData().get("OwnerName");
                                Experiment experiment = new Experiment(name, description,category,region,minimumTrails,uid);
                                experiment.setOwnerName(ownerName);
                                expSearchDataList.add(experiment);
                            }
                            expSearchAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });

        expSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Experiment experiment = (Experiment) expSearchList.getItemAtPosition(position);
                String owner = experiment.getOwner();
                if (owner.equals(currentUid)) {
                    Intent intent = new Intent(SearchExperimentActivity.this, experimentInfo_owner.class);
                    intent.putExtra("experiment",experiment);
                    intent.putExtra("uid",currentUid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchExperimentActivity.this, experimentInfo_user.class);
                    intent.putExtra("experiment",experiment);
                    intent.putExtra("uid",currentUid);
                    startActivity(intent);
                }
            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                expSearchDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String name = doc.getId();
                    String category = (String) doc.getData().get("category");
                    String description = (String) doc.getData().get("description");
                    String minimumTrails = (String) doc.getData().get("minimum_trails");
                    String region = (String) doc.getData().get("region");
                    String uid = (String) doc.getData().get("Owner");
                    String ownerName = (String) doc.getData().get("OwnerName");
                    Experiment experiment = new Experiment(name, description,category,region,minimumTrails,uid);
                    experiment.setOwnerName(ownerName);
                    expSearchDataList.add(experiment);
                }
                expSearchAdapter.notifyDataSetChanged();
            }
        });


    }

}