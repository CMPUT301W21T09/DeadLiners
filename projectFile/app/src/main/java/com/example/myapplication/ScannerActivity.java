package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private String uid;

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
        Toast.makeText(ScannerActivity.this, data, Toast.LENGTH_SHORT).show();
        String[] arrOfdata = data.split("|",0);
        String expName = arrOfdata[0];
        String category = arrOfdata[1];
        String trial = arrOfdata[2];
        final CollectionReference collectionReference;

        if(category.equals("count")) {
            collectionReference = db.collection("CountDataset");

            String currentTime = String.format("%d",currentTimeMillis());
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
            String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

            HashMap<String, String> input = new HashMap<>();
            input.put("expName",expName);
            input.put("experimenter",uid);
            input.put("time",currentTime);
            HashMap<String,Boolean> ignore = new HashMap<>();
            ignore.put("ignore",false);

            collectionReference
                    .document(uniqueTrailId)
                    .set(input);
            collectionReference
                    .document(uniqueTrailId)
                    .set(ignore, SetOptions.merge());
        }
        else if(category.equals("binomial")) {
            collectionReference = db.collection("BinomialDataSet");

            String currentTime = String.format("%d",currentTimeMillis());
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
            String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

            HashMap<String, String> input = new HashMap<>();

            HashMap<String, Boolean> passOrFail = new HashMap<>();
            passOrFail.put("pass",false);

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
            collectionReference
                    .document(uniqueTrailId)
                    .set(input);

            collectionReference
                    .document(uniqueTrailId)
                    .set(passOrFail,SetOptions.merge());


            collectionReference
                    .document(uniqueTrailId)
                    .set(ignore,SetOptions.merge());
        }

        /*
        dbref.push().setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ScannerActivity.this,"Trial added successfully!",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });

         */


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