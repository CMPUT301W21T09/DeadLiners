package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static java.lang.System.currentTimeMillis;

//Availability https://www.youtube.com/watch?v=AiNi9K94W5c
//Title "Firebase barcode Scanner | QR and Barcode Scanner App | Scan QR and update data in Firebase" By Md Jamal
//Accessed on Apr.7

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    //Button back;

    final String TAG = "Sample";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference BinomialcollectionReference = db.collection("BinomialDataSet");;
    final CollectionReference CountcollectionReference = db.collection("CountDataset");

    private String uid;
    public double latitude;
    public double longitude;
    private String uniqueTrailId;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        //Database initialize here

        //dbref = FirebaseDatabase.getInstance().getReference("Experiments");


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }



    @Override
    public void handleResult(Result rawResult) {
        String data = rawResult.getText().toString();
        String[] arrOfdata = data.split("\\|",4);
        String expName = arrOfdata[0];
        String category = arrOfdata[1];
        String trial = arrOfdata[2];
        String geoState = arrOfdata[3];

        if(category.equals("1")) {

            String currentTime = String.format("%d", currentTimeMillis());
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
            uniqueTrailId = String.format("Trail of %s at %s", uid, currentTime);

            HashMap<String, String> input = new HashMap<>();
            input.put("expName", expName);
            input.put("experimenter", uid);
            input.put("time", currentTime);
            HashMap<String, Boolean> ignore = new HashMap<>();
            ignore.put("ignore", false);

            CountcollectionReference
                    .document(uniqueTrailId)
                    .set(input);
            CountcollectionReference
                    .document(uniqueTrailId)
                    .set(ignore, SetOptions.merge());
            if (geoState.equals("1")){
                getLocation(category);
            }

        }
        else if(category.equals("2")) {

            String currentTime = String.format("%d",currentTimeMillis());
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
            uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

            HashMap<String, String> input = new HashMap<>();

            if(trial.equals("1")) {
                input.put("value","pass");
            } else if (trial.equals("0")) {
                input.put("value","fail");
            }
            input.put("expName",expName);
            input.put("experimenter",uid);
            input.put("time",currentTime);
            HashMap<String,Boolean> ignore = new HashMap<>();
            ignore.put("ignore",false);
            BinomialcollectionReference
                    .document(uniqueTrailId)
                    .set(input);

            BinomialcollectionReference
                    .document(uniqueTrailId)
                    .set(ignore,SetOptions.merge());

            if (geoState.equals("1")){
                getLocation(category);
            }
        }

        Toast.makeText(ScannerActivity.this,"Data added!",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getLocation(String category){
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                ScannerActivity.this
        );

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult){
                                Location location1 = locationResult.getLastLocation();
                                latitude = location1.getLatitude();
                                longitude = location1.getLongitude();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                                ,locationCallback, Looper.myLooper());
                    }

                    HashMap<String, Double> loc = new HashMap<>();
                    loc.put("longi", longitude);
                    loc.put("lat", latitude);

                    if (category.equals("1")) {
                        CountcollectionReference
                                .document(uniqueTrailId)
                                .set(loc,SetOptions.merge());
                    }
                    if (category.equals("2")) {
                        BinomialcollectionReference
                                .document(uniqueTrailId)
                                .set(loc,SetOptions.merge());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
