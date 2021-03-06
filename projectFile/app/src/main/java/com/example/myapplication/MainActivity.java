package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener, AddSearchFragment.OnFragmentInteractionListener {
    public ArrayList<Experiment> experimentsArrayList;
    public ArrayAdapter<Experiment> experimentsArrayAdapter;
    private ListView mainScrollView;

    ListView customList;
    public Boolean login = false;
    private String uid;
    private String username;
    private final String TAG = "Sample";
    private ImageButton button_user;
    private ImageButton button_add;
    private ImageButton button_scan;
    private ImageButton button_search;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference experimentCollectionReference = db.collection("Experiments");
    CollectionReference countCollectionReference = db.collection("CountDataset");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// go to login page
        if( login == false ){
            Intent intent = new Intent().setClass(MainActivity.this, LoginActivity.class);
            intent.putExtra("login",login);
            startActivityForResult(intent, 0);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// setup buttons and listview
        button_add = findViewById(R.id.imageButton_add);
        button_user = findViewById(R.id.imageButton_user);
        button_search = findViewById(R.id.imageButton_search);
        button_scan = findViewById(R.id.camera);

        mainScrollView = findViewById(R.id.mainScrollView);

        experimentsArrayList = new ArrayList<Experiment>();
        experimentsArrayAdapter = new mainCustomList(this, experimentsArrayList);
        mainScrollView.setAdapter(experimentsArrayAdapter);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddExperimentFragment(uid).show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });


        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddSearchFragment(uid).show(getSupportFragmentManager(),"SEARCH");
            }
        });


        mainScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Experiment experiment = (Experiment) mainScrollView.getItemAtPosition(position);
                String owner = experiment.getOwner();
                if (owner.equals(uid)) {
                    Intent intent = new Intent(MainActivity.this, experimentInfo_owner.class);
                    intent.putExtra("experiment",experiment);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, experimentInfo_user.class);
                    intent.putExtra("experiment",experiment);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }
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
                    String uid = (String) doc.getData().get("Owner");
                    String ownerName = (String) doc.getData().get("OwnerName");
                    String status = (String) doc.getData().get("Status");
                    String geoEnable = (String) doc.getData().get("GeoState");
                    Experiment experiment = new Experiment(expName, description,category,region,minimumTrails,uid);
                    experiment.setStatus(status);
                    experiment.setOwnerName(ownerName);
                    experiment.setGeoState(geoEnable);
                    experimentsArrayList.add(experiment);
                }
                experimentsArrayAdapter.notifyDataSetChanged();
            }
        });


    }


// go to profile activity
    public void GoProfile(View view) {
        if(login == true){
            Intent intent = new Intent().setClass(MainActivity.this, UserProfileActivity.class);
            intent.putExtra("login_uid",uid);
            startActivityForResult(intent, 0);;
        }
    }

// pass the uid between main activity and profile activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 1){
                Bundle bundle = data.getExtras();
                String back1 = bundle.getString("return1");
                String back2 = bundle.getString("return2");
                Toast.makeText(MainActivity.this,"Welcome "+ back2,Toast.LENGTH_SHORT).show();
                uid = back1;
                username = back2;
                login = true;
            }
        }
    }



    @Override
    public void onOkPressed(Experiment newExperiment) {
        newExperiment.setOwnerName(username);
        String expName = newExperiment.getExpName();
        HashMap<String,String> expNameField = new HashMap<>();
        HashMap<String,String> expStatus = new HashMap<>();
        HashMap<String,String> expOwner = new HashMap<>();
        HashMap<String,String> expOwnerName = new HashMap<>();
        HashMap<String,String> expCategory = new HashMap<>();
        HashMap<String,String> expDes = new HashMap<>();
        HashMap<String,String> expMinimumTrail = new HashMap<>();
        HashMap<String,String> expRegion = new HashMap<>();
        HashMap<String,String> geoState = new HashMap<>();

        expNameField.put("Name",expName);
        expStatus.put("Status","open");
        expOwner.put("Owner",uid);
        expOwnerName.put("OwnerName", newExperiment.getOwnerName());
        expCategory.put("category", newExperiment.getCategory());
        expDes.put("description", newExperiment.getDescription());
        expMinimumTrail.put("minimum_trails", newExperiment.getMinimum_trails());
        expRegion.put("region", newExperiment.getRegion());
        geoState.put("GeoState", newExperiment.getGeoState());

        experimentCollectionReference
                .document(expName)
                .set(expCategory);
        experimentCollectionReference
                .document(expName)
                .set(expNameField,SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expOwnerName,SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expOwner, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expDes, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expMinimumTrail, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expRegion, SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(expStatus,SetOptions.merge());
        experimentCollectionReference
                .document(expName)
                .set(geoState,SetOptions.merge());


    }

}
