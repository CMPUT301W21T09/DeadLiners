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

//Availability https://www.geeksforgeeks.org/how-to-add-multiple-markers-on-google-maps-in-android/
//Title "How to Add Multiple Markers on Google Maps in Android?"
//Accessed on Apr.8

public class SeeMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    final String TAG = "Sample";
    FirebaseFirestore db;

    private String exp_category;
    private String exp_name;

    private ArrayList<LatLng> locationArrayList;
    private LatLng[] locationList;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        exp_name = intent.getStringExtra("exp_name");
        exp_category = intent.getStringExtra("exp_category");

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference;

        locationArrayList = new ArrayList<>();
        locationList = new LatLng[10000];

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
                int i = 0;
                for(QueryDocumentSnapshot doc: value) {
                    String expName = (String) doc.getData().get("expName");
                    if(expName.equals(exp_name)) {
                        Double lat  = (Double) doc.getData().get("lat");
                        Double lon  = (Double) doc.getData().get("longi");
                        //Toast.makeText(SeeMapActivity.this, String.valueOf(lat), Toast.LENGTH_LONG).show();
                        LatLng location = new LatLng(lat, lon);
                        //locationArrayList.add(location);
                        locationList[i] = location;
                        //Toast.makeText(SeeMapActivity.this, String.valueOf(locationList[i].latitude), Toast.LENGTH_LONG).show();
                        i++;
                    }
                }
                length = i;
                Toast.makeText(SeeMapActivity.this, String.valueOf(length), Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // inside on map ready method
        // we will be displaying all our markers.
        // for adding markers we are running for loop and
        // inside that we are drawing marker on our map

        for (int i = 0; i < length; i++) {

            // below line is use to add marker to each location of our array list.
            //mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));
            //Toast.makeText(SeeMapActivity.this, String.valueOf(locationList[i].latitude), Toast.LENGTH_LONG).show();
            mMap.addMarker(new MarkerOptions().position(locationList[i]).title("Marker"));

            // below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationList[i]));
        }

    }
}