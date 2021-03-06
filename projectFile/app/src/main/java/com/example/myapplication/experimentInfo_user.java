package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;
// user cannot end and unpublish the experiment, else are same as owner's class
public class experimentInfo_user extends AppCompatActivity {
    private static final String TAG = "experiment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Experiment experiment;
    private String uid;
    CollectionReference countCollectionReference = db.collection("CountDataset");
    CollectionReference binomialCollectionReference = db.collection("BinomialDataSet");
    CollectionReference intCountCollectionReference = db.collection("IntCountDataset");
    CollectionReference measurementCollectionReference = db.collection("MeasurementDataset");
    private String choose;
    private String data;
    public double latitude;
    public double longitude;
    private String uniqueTrailId;

    private Button qrCode;
    private Button subscribe;
    private Button barCode;
    private Button viewTrails;
    private Button addTrail;
    private Button back;
    private Button seeMap;
    private Switch aSwitch;

    private TextView experimentName;
    private TextView description;
    private TextView owner;
    private TextView category;
    private TextView region;
    private TextView status;

    FusedLocationProviderClient fusedLocationProviderClient;
    private CollectionReference userCollectionReference;
    Gson gson = new Gson();

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

        barCode = findViewById(R.id.barcode2);
        qrCode = findViewById(R.id.QR_code);
        subscribe = findViewById(R.id.subscribe);
        viewTrails = findViewById(R.id.View_Trials);
        addTrail = findViewById(R.id.Add_Trial);
        back = findViewById(R.id.Back);
        seeMap = findViewById(R.id.seeMap_user);
        subscribe = findViewById(R.id.subscribe);

        if (experiment.getGeoState().equals("0")){
            seeMap.setVisibility(View.INVISIBLE);
        }

        experimentName.setText(experiment.getExpName());
        description.setText(experiment.getDescription());
        owner.setText(experiment.getOwnerName());
        category.setText(experiment.getCategory());
        region.setText(experiment.getRegion());
        status.setText(experiment.getPublished());
        aSwitch = findViewById(R.id.geo_switch);

        String expName = experiment.getExpName();
        userCollectionReference = db.collection("Users");

        if (experiment.getGeoState().equals("1")) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        aSwitch.setClickable(false);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userCollectionReference.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    private Subscribe subscribe;

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("DOC", "DocumentSnapshot data: " + document.getData());
                                String subscribeString = document.getString("Subscribe");
                                subscribe = gson.fromJson(subscribeString, Subscribe.class);
                                if ( subscribe.getSubscribe().size()>0){

                                    if (!subscribe.getSubscribe().contains(experiment.getExpName())){
                                        subscribe.getSubscribe().add(experiment.getExpName());
                                        userCollectionReference.document(uid)
                                                .update(
                                                        "Subscribe", gson.toJson(subscribe)
                                                );
                                        Toast.makeText(experimentInfo_user.this,"Successfully Subscribed！",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.d("DOC", " already Subscribe");
                                        Toast.makeText(experimentInfo_user.this,"This experiment has already been subscribed.",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    subscribe.getSubscribe().add(expName);
                                    userCollectionReference.document(uid)
                                            .update(
                                                    "Subscribe", gson.toJson(subscribe)
                                            );
                                    Toast.makeText(experimentInfo_user.this,"Successfully Subscribed！",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("DOC", "No such document");
                            }
                        } else {
                            Log.d("DOC", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

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

        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(experiment.geoState.equals("1")) {
                    Intent intent = new Intent(experimentInfo_user.this, SeeMapActivity.class);
                    intent.putExtra("exp_category",experiment.getCategory());
                    intent.putExtra("exp_name", experiment.getExpName());
                    startActivity(intent);
                } else {
                    Toast.makeText(experimentInfo_user.this, "There is no map!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experiment.getGeoState().equals("1")) {
                    Toast.makeText(experimentInfo_user.this,"This experiment require your location!",Toast.LENGTH_SHORT).show();
                }
                if (experiment.getCategory().equals("count") && (experiment.getPublished().equals("open"))){
                    String currentTime = String.format("%d",currentTimeMillis());

                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                    uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

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

                    if (experiment.getGeoState().equals("1")) {
                        getLocation();
                    }



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
                                    uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();

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
                                            .set(ignore,SetOptions.merge());
                                    if (experiment.getGeoState().equals("1")) {
                                        getLocation();
                                    }

                                }
                            })
                            .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String currentTime = String.format("%d",currentTimeMillis());
                                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(currentTime)));
                                    uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

                                    HashMap<String, String> data = new HashMap<>();


                                    data.put("value","fail");
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
                                            .set(ignore,SetOptions.merge());

                                    if (experiment.getGeoState().equals("1")) {
                                        getLocation();
                                    }

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
                                    uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

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

                                    if (experiment.getGeoState().equals("1")) {
                                        getLocation();
                                    }

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
                                    uniqueTrailId = String.format("Trail of %s at %s",uid,currentTime);

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

                                    if (experiment.getGeoState().equals("1")) {
                                        getLocation();
                                    }
                                }
                            });
                    builder.create().show();

                } else {
                    Toast.makeText(experimentInfo_user.this,"This experiment is ended",Toast.LENGTH_SHORT).show();
                }
            }
        });

        barCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expName = experiment.getExpName();
                String category = experiment.getCategory();
                String geoState = experiment.getGeoState();
                if (category.equals("binomial") || category.equals("count")){
                    if (category.equals("count")){
                        category = "1";
                    } else {
                        category = "2";
                    }
                    data = expName + "|" + category;
                    if (category.equals("2")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_user.this).setTitle("Pass or Fail?")
                                .setPositiveButton("Pass", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        choose = "1";
                                        data = data + "|" + choose;
                                        if (geoState.equals("0")) {
                                            data = data + "|0";
                                        } else {
                                            data = data + "|1";
                                        }
                                        Intent intent = new Intent(experimentInfo_user.this, barcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        choose = "0";
                                        data = data + "|" + choose;
                                        if (geoState.equals("0")) {
                                            data = data + "|0";
                                        } else {
                                            data = data + "|1";
                                        }
                                        Intent intent = new Intent(experimentInfo_user.this, barcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                });
                        builder.create().show();
                    }
                    if (category.equals("1")){
                        data = data + "|1";
                        if (geoState.equals("0")) {
                            data = data + "|0";
                        } else {
                            data = data + "|1";
                        }
                        Intent intent = new Intent(experimentInfo_user.this, barcodeView.class);
                        intent.putExtra("exp",data);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(experimentInfo_user.this, "This type of experiment currently does not support generate the barCode", Toast.LENGTH_SHORT).show();
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
            @Override
            public void onClick(View v) {
                String expName = experiment.getExpName();
                String category = experiment.getCategory();
                String geoState = experiment.getGeoState();
                if (category.equals("binomial") || category.equals("count")){
                    if (category.equals("count")){
                        category = "1";
                    } else {
                        category = "2";
                    }
                    data = expName + "|" + category;
                    if (category.equals("2")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_user.this).setTitle("Pass or Fail?")
                                .setPositiveButton("Pass", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        choose = "1";
                                        data = data + "|" + choose;
                                        if (geoState.equals("0")) {
                                            data = data + "|0";
                                        } else {
                                            data = data + "|1";
                                        }
                                        Intent intent = new Intent(experimentInfo_user.this, qrcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        choose = "0";
                                        data = data + "|" + choose;
                                        if (geoState.equals("0")) {
                                            data = data + "|0";
                                        } else {
                                            data = data + "|1";
                                        }
                                        Intent intent = new Intent(experimentInfo_user.this, qrcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                });
                        builder.create().show();
                    }
                    if (category.equals("1")){
                        data = data + "|1";
                        if (geoState.equals("0")) {
                            data = data + "|0";
                        } else {
                            data = data + "|1";
                        }
                        Intent intent = new Intent(experimentInfo_user.this, qrcodeView.class);
                        intent.putExtra("exp",data);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(experimentInfo_user.this, "This type of experiment currently does not support generate the QRCode", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button questionButton = findViewById(R.id.Question_Forum);
        questionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showQuestionInfo(); }
        });
    }


    public void getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                experimentInfo_user.this
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

                    if (experiment.getCategory().equals("count")) {
                        countCollectionReference
                                .document(uniqueTrailId)
                                .set(loc,SetOptions.merge());
                    }
                    if (experiment.getCategory().equals("binomial")) {
                        binomialCollectionReference
                                .document(uniqueTrailId)
                                .set(loc,SetOptions.merge());
                    }
                    if (experiment.getCategory().equals("intCount")) {
                        intCountCollectionReference
                                .document(uniqueTrailId)
                                .set(loc,SetOptions.merge());
                    }
                    if (experiment.getCategory().equals("measurement")) {
                        measurementCollectionReference
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

    void showQuestionInfo()
    {
        Intent intent = new Intent(this, QuestionListActivity.class);
        intent.putExtra("experiment",experiment);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

}
