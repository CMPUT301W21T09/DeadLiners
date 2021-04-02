package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.WriterException;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class experimentInfo_user extends AppCompatActivity {
    private static final String TAG = "experiment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Experiment experiment;
    private String uid;
    CollectionReference countCollectionReference = db.collection("CountDataset");
    private String count ;
    private int intCount;
    private int passCount;
    private int failCount;

    private Button qrCode;
    private Button subscribe;
    private Button questionForum;
    private Button viewTrails;
    private Button addTrail;
    private Button back;

    private TextView experimentName;
    private TextView description;
    private TextView owner;
    private TextView category;
    private TextView region;
    private TextView status;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experimentinfo_user);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");
        uid = intent.getStringExtra("uid");

        experimentName = findViewById(R.id.experimentName);
        description = findViewById(R.id.Description);
        owner = findViewById(R.id.Owner);
        category = findViewById(R.id.category);
        region = findViewById(R.id.Region);
        status = findViewById(R.id.Status);

        qrCode = findViewById(R.id.QR_code);
        subscribe = findViewById(R.id.Subscribe);
        viewTrails = findViewById(R.id.View_Trials);
        addTrail = findViewById(R.id.Add_Trial);
        back = findViewById(R.id.Back);

        experimentName.setText(experiment.getExpName());
        description.setText(experiment.getDescription());
        owner.setText(experiment.getOwnerName());
        category.setText(experiment.getCategory());
        region.setText(experiment.getRegion());
        status.setText(experiment.getPublished());

        String expName = experiment.getExpName();
        DocumentReference countRef = countCollectionReference.document(expName);
        countRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        count = document.getString("count");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        addTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experiment.getCategory().equals("count") && (experiment.getPublished().equals("open"))){
                    HashMap<String, String> countData = new HashMap<>();
                    countData.put("count",(Integer.parseInt(count) + 1) +"");
                    countRef.set(countData,SetOptions.merge());
                    finish();
                }
                else if (experiment.getCategory().equals("binomial") && (experiment.getPublished().equals("open"))){
                    // record how many pass and fail
                    finish();
                }
                else if (experiment.getCategory().equals("intCount") && (experiment.getPublished().equals("open"))){
                    // record a integer
                    finish();
                }
                else if (experiment.getCategory().equals("measurement") && (experiment.getPublished().equals("open"))){
                    // record a double
                    finish();
                } else {
                    Toast.makeText(experimentInfo_user.this,"This experiment is ended",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                qrgEncoder = new QRGEncoder("test", null, QRGContents.Type.TEXT, 350);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();

                } catch (
                        WriterException e) {
                    Log.e("Tag", e.toString());
                }

                QRFragment qrFragment = QRFragment.newInstance(bitmap);
                qrFragment.show(getSupportFragmentManager(),"qrfrag");
            }
        });

        Button questionButton = findViewById(R.id.Question_Forum);
        questionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showQuestionInfo(); }
        });
    }

    void showQuestionInfo()
    {
        Intent intent = new Intent(this, QuestionListActivity.class);
        intent.putExtra("experiment",experiment);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

}