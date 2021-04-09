package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double longi;
    private String expType;
    private CollectionReference collectionReference;
    private ArrayList<LatLng> locList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference experimentCollectionReference = db.collection("Experiments");
    CollectionReference countCollectionReference = db.collection("CountDataset");
    CollectionReference binomialCollectionReference = db.collection("BinomialDataSet");
    CollectionReference intCountCollectionReference = db.collection("IntCountDataset");
    CollectionReference measurementCollectionReference = db.collection("MeasurementDataset");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        expType = intent.getStringExtra("cType");

        if (expType.equals("measurement")){
            collectionReference = measurementCollectionReference;
        }else if (expType.equals("intCount")){
            collectionReference = intCountCollectionReference;
        }else if (expType.equals("binomial")){
            collectionReference = binomialCollectionReference;
        }else if (expType.equals("count")){
            collectionReference = countCollectionReference;
        }

        locList = new ArrayList<LatLng>();
        /*collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value) {
                    lat = (Double) doc.getData().get("lat");
                    longi = (Double) doc.getData().get("longi");
                    LatLng loc = new LatLng(lat, longi);
                    locList.add(loc);

                }
            }
        });*/


        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lat = (Double) document.getData().get("lat");
                                longi = (Double) document.getData().get("longi");
                            }
                        } else {
                        }
                    }
                });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        int i;
        for(i=0; i<locList.size();i++){
            LatLng nloc = locList.get(i);
            mMap.addMarker(new MarkerOptions().position(nloc).title("Experiment location"));

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locList.get(i)));

    }


}