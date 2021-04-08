package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static java.lang.System.currentTimeMillis;

public class experimentInfo_user extends AppCompatActivity {
    private static final String TAG = "experiment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Experiment experiment;
    private String uid;
    CollectionReference countCollectionReference = db.collection("CountDataset");
    CollectionReference binomialCollectionReference = db.collection("BinomialDataSet");
    CollectionReference intCountCollectionReference = db.collection("IntCountDataset");
    CollectionReference measurementCollectionReference = db.collection("MeasurementDataset");
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
    private Switch aSwitch;

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
        aSwitch = findViewById(R.id.Geo_enable);

        String expName = experiment.getExpName();

        if (experiment.getGeoState().equals("1")) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        aSwitch.setClickable(false);


        viewTrails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(experimentInfo_user.this, TrialsActivity.class);
                intent.putExtra("exp_category",experiment.getCategory());
                intent.putExtra("exp_name", experiment.getExpName());
                intent.putExtra("uid", uid);
                intent.putExtra("owner", experiment.getOwner());
                startActivity(intent);
            }
        });

        addTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experiment.getGeoState().equals("1")) {
                    Toast.makeText(experimentInfo_user.this,"This experiment requires your Geo-Location!",Toast.LENGTH_SHORT).show();
                }
                if (experiment.getCategory().equals("count") && (experiment.getPublished().equals("open"))){
                    String currentTime = String.format("%d",currentTimeMillis());
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                    String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                    HashMap<String, String> data = new HashMap<>();
                    data.put("expName",expName);
                    data.put("experimenter",uid);
                    data.put("time",currentTime);
                    HashMap<String,Boolean> ignore = new HashMap<>();
                    ignore.put("ignore",false);

                    countCollectionReference
                            .document(uniqueTrailId)
                            .set(data);
                    countCollectionReference
                            .document(uniqueTrailId)
                            .set(ignore,SetOptions.merge());

                    Toast.makeText(experimentInfo_user.this,"Increment the count by 1!",Toast.LENGTH_SHORT).show();
                }
                else if (experiment.getCategory().equals("binomial") && (experiment.getPublished().equals("open"))){
                    // record how many pass and fail
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_user.this).setTitle("Pass or Fail?")
                            .setPositiveButton("Pass", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String currentTime = String.format("%d",currentTimeMillis());
                                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                                    String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();

                                    HashMap<String, Boolean> passOrFail = new HashMap<>();
                                    passOrFail.put("pass",true);


                                    data.put("value", "pass");
                                    data.put("expName",expName);
                                    data.put("experimenter",uid);
                                    data.put("time",currentTime);
                                    HashMap<String,Boolean> ignore = new HashMap<>();
                                    ignore.put("ignore",false);
                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(data);

                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(passOrFail,SetOptions.merge());


                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(ignore,SetOptions.merge());

                                }
                            })
                            .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String currentTime = String.format("%d",currentTimeMillis());
                                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                                    String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();

                                    HashMap<String, Boolean> passOrFail = new HashMap<>();
                                    passOrFail.put("pass",false);


                                    data.put("value", "fail");
                                    data.put("expName",expName);
                                    data.put("experimenter",uid);
                                    data.put("time",currentTime);
                                    HashMap<String,Boolean> ignore = new HashMap<>();
                                    ignore.put("ignore",false);
                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(data);

                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(passOrFail,SetOptions.merge());


                                    binomialCollectionReference
                                            .document(uniqueTrailId)
                                            .set(ignore,SetOptions.merge());

                                }
                            });
                    builder.create().show();
                }
                else if (experiment.getCategory().equals("intCount") && (experiment.getPublished().equals("open"))){
                    // record a integer
                    final EditText editText = new EditText(experimentInfo_user.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_user.this).setTitle("How many counts you got?").setView(editText)
                            .setPositiveButton("Add trails", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String intCount = editText.getText().toString();

                                    String currentTime = String.format("%d",currentTimeMillis());
                                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                                    String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("expName",expName);
                                    data.put("experimenter",uid);
                                    data.put("value",intCount);
                                    data.put("time",currentTime);
                                    HashMap<String,Boolean> ignore = new HashMap<>();
                                    ignore.put("ignore",false);


                                    intCountCollectionReference
                                            .document(uniqueTrailId)
                                            .set(data);
                                    intCountCollectionReference
                                            .document(uniqueTrailId)
                                            .set(ignore,SetOptions.merge());

                                }
                            });
                    builder.create().show();
                }
                else if (experiment.getCategory().equals("measurement") && (experiment.getPublished().equals("open"))){
                    // record a double
                    final EditText editText = new EditText(experimentInfo_user.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_user.this).setTitle("What is the measurement you got?").setView(editText)
                            .setPositiveButton("Add trails", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String measurement = editText.getText().toString();

                                    String currentTime = String.format("%d",currentTimeMillis());
                                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                                    String uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("expName",expName);
                                    data.put("experimenter",uid);
                                    data.put("value",measurement);
                                    data.put("time",currentTime);
                                    HashMap<String,Boolean> ignore = new HashMap<>();
                                    ignore.put("ignore",false);

                                    measurementCollectionReference
                                            .document(uniqueTrailId)
                                            .set(data);
                                    measurementCollectionReference
                                            .document(uniqueTrailId)
                                            .set(ignore,SetOptions.merge());
                                }
                            });
                    builder.create().show();
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