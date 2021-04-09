package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Availability https://www.geeksforgeeks.org/how-to-add-dynamic-markers-in-google-maps-with-firebase-firstore/
//Title "How to Add Dynamic Markers in Google Maps with Firebase Firstore?"
//Accessed on Apr.8

public class SeeMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    final String TAG = "Sample";
    FirebaseFirestore db;

    private String exp_category;
    private String exp_name;

    private ArrayList<LatLng> locationArrayList;
    private LatLng[] locationList;
    private ArrayList<LatLng> processList;
    private int length;

    public SeeMapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Intent intent = getIntent();
        exp_name = intent.getStringExtra("exp_name");
        exp_category = intent.getStringExtra("exp_category");

        mMap = googleMap;

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


        // calling document reference class with on snap shot listener.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value) {
                    String expName = (String) doc.getData().get("expName");
                    if(expName.equals(exp_name)) {
                        Double lat  = (Double) doc.getData().get("lat");
                        Double lon  = (Double) doc.getData().get("longi");
                        LatLng location = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions().position(location).title("Marker"));

                        // below line is use to move camera.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                }
            }
        });
    }
}