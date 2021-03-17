package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchExperimentActivity extends AppCompatActivity {

    private ListView expSearchList;
    private ArrayAdapter<Experiment> expSearchAdapter;
    private ArrayList<Experiment> expSearchDataList;

    private SearchExpCustomList customList;

    final String TAG = "Sample";
    Button searchExperimentButton;
    EditText searchExperimentEditText;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_experiment);

        expSearchList = findViewById(R.id.experiment_list);
        searchExperimentButton = findViewById(R.id.search_experiment_button);
        searchExperimentEditText = findViewById(R.id.search_experiment_field);

        expSearchDataList = new ArrayList<>();
        expSearchAdapter = new SearchExpCustomList(this, expSearchDataList);
        expSearchList.setAdapter(expSearchAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");


        searchExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String expName = searchExperimentEditText.getText().toString();

                if(expName.length() > 0) {
                    collectionReference
                            .whereArrayContains("NameString", expName)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        expSearchDataList.clear();
                                        for (DocumentSnapshot doc: task.getResult()) {
                                            expSearchDataList.add(new Experiment(doc.getString("Name")));
                                        }
                                        expSearchAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    searchExperimentEditText.setText("");
                }
            }
        });
    }

}