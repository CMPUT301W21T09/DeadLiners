package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class experimentInfo_owner extends AppCompatActivity {
    private Experiment experiment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference experimentCollectionReference = db.collection("Experiments");

    private Button qrCode;
    private Button subscribe;
    private Button questionForum;
    private Button viewTrails;
    private Button addTrail;
    private Button back;
    private Button unPublish;
    private Button end;

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
        setContentView(R.layout.experimentinfo_owner);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");

        experimentName = findViewById(R.id.experimentName);
        description = findViewById(R.id.Description);
        owner = findViewById(R.id.Owner);
        category = findViewById(R.id.category);
        region = findViewById(R.id.Region);
        status = findViewById(R.id.Status);

        qrCode = findViewById(R.id.QR_code);
        subscribe = findViewById(R.id.Subscribe);
        questionForum = findViewById(R.id.Question_Forum);
        viewTrails = findViewById(R.id.View_Trials);
        addTrail = findViewById(R.id.Add_Trial);
        back = findViewById(R.id.back);
        unPublish = findViewById(R.id.Unpublish);
        end = findViewById(R.id.End);

        experimentName.setText(experiment.getExpName());
        description.setText(experiment.getDescription());
        owner.setText(experiment.getOwner());
        category.setText(experiment.getCategory());
        region.setText(experiment.getRegion());
        status.setText(experiment.getPublished());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        unPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experiment.setPublishedToFalse();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentCollectionReference
                        .document(experiment.getExpName())
                        .delete();
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
            public void onClick(View v) {
                Intent intent = new Intent(experimentInfo_owner.this, QuestionListActivity.class);
                intent.putExtra("experiment",experiment);
                startActivity(intent);
            }
        });
    }
}