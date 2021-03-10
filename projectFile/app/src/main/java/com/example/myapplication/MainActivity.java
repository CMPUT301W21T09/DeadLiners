package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener{
    public ArrayList<Experiment> experimentsArrayList;
    public ArrayAdapter<Experiment> experimentsArrayAdapter;
    private ListView mainScrollView;

    mainCustomList customList;
    private Boolean login = false;
    private String uid;
    private final String TAG = "Sample";
    private ImageButton button_user;
    private ImageButton button_add;
    private FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    CollectionReference experimentCollectionReference = db.collection("Experiments");
    CollectionReference UserCollectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup buttons and listview
        button_add = findViewById(R.id.imageButton_add);
        button_user = findViewById(R.id.imageButton_user);

        mainScrollView = findViewById(R.id.mainScrollView);

        experimentsArrayList = new ArrayList<Experiment>();
        experimentsArrayAdapter = new mainCustomList(this, experimentsArrayList);
        mainScrollView.setAdapter(experimentsArrayAdapter);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        experimentCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentsArrayList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String expName = doc.getId();
                    String category = (String) doc.getData().get("category");
                    String description = (String) doc.getData().get("description");
                    String minimumTrails = (String) doc.getData().get("minimum_trails");
                    String region = (String) doc.getData().get("region");
                    experimentsArrayList.add(new Experiment(expName, description,category,region,minimumTrails));
                }
                experimentsArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void GoProfile(View view) {
        Intent intent = new Intent().setClass(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 1){
                Bundle bundle = data.getExtras();
                String back = bundle.getString("return");
                Toast.makeText(MainActivity.this,"Welcome "+ back,Toast.LENGTH_SHORT).show();
                uid = back;
                login = true;
            }
        }
    }

    @Override
    public void onOkPressed(Experiment newExperiment) {
        String expName = newExperiment.getExpName();
        HashMap<String,String> expCategory = new HashMap<>();
        HashMap<String,String> expDes = new HashMap<>();
        HashMap<String,String> expMinimumTrail = new HashMap<>();
        HashMap<String,String> expRegion = new HashMap<>();

        expCategory.put("category", newExperiment.getCategory());
        expDes.put("description", newExperiment.getDescription());
        expMinimumTrail.put("minimum_trails", newExperiment.getMinimum_trails());
        expRegion.put("region", newExperiment.getRegion());

        experimentCollectionReference
                .document(expName)
                .set(expCategory);
        experimentCollectionReference
                .document(expName)
                .set(expDes, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expMinimumTrail, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expRegion, SetOptions.merge());
    }
}