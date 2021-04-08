package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.WriterException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

//import androidmads.library.qrgenearator.QRGContents;
//import androidmads.library.qrgenearator.QRGEncoder;

//import androidmads.library.qrgenearator.QRGContents;
//import androidmads.library.qrgenearator.QRGEncoder;

import static java.lang.System.currentTimeMillis;

public class experimentInfo_owner extends AppCompatActivity {
    private static final String TAG = "experiment";
    private Experiment experiment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference experimentCollectionReference = db.collection("Experiments");
    CollectionReference countCollectionReference = db.collection("CountDataset");
    CollectionReference binomialCollectionReference = db.collection("BinomialDataSet");
    CollectionReference intCountCollectionReference = db.collection("IntCountDataset");
    CollectionReference measurementCollectionReference = db.collection("MeasurementDataset");
    private String uid;
    private String choose;
    private String data;


    private Button qrCode;
    private Button subscribe;
    private Button questionForum;
    private Button viewTrails;
    private Button addTrail;
    private Button back;
    private Button unPublish;
    private Button end;
    private Button barCode;

    private TextView experimentName;
    private TextView description;
    private TextView owner;
    private TextView category;
    private TextView region;
    private TextView status;
    private Switch aSwitch;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Bitmap bitmap;
//    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experimentinfo_owner);

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
        barCode = findViewById(R.id.barcode);
        subscribe = findViewById(R.id.subscribe);
        viewTrails = findViewById(R.id.View_Trials);
        addTrail = findViewById(R.id.Add_Trial);
        back = findViewById(R.id.back);
        unPublish = findViewById(R.id.Unpublish);
        end = findViewById(R.id.End);
        aSwitch = findViewById(R.id.Geo_enable);

        if (experiment.getGeoState().equals("1")) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        experimentName.setText(experiment.getExpName());
        description.setText(experiment.getDescription());
        owner.setText(experiment.getOwnerName());
        category.setText(experiment.getCategory());
        region.setText(experiment.getRegion());
        status.setText(experiment.getPublished());

        String expName = experiment.getExpName();

        viewTrails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(experimentInfo_owner.this, TrialsActivity.class);
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

                    Toast.makeText(experimentInfo_owner.this,"Increment the count by 1!",Toast.LENGTH_SHORT).show();
                }
                else if (experiment.getCategory().equals("binomial") && (experiment.getPublished().equals("open"))){
                    // record how many pass and fail
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_owner.this).setTitle("Pass or Fail?")
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
                    final EditText editText = new EditText(experimentInfo_owner.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_owner.this).setTitle("How many counts you got?").setView(editText)
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
                    final EditText editText = new EditText(experimentInfo_owner.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_owner.this).setTitle("What is the measurement you got?").setView(editText)
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
                    Toast.makeText(experimentInfo_owner.this,"This experiment is ended",Toast.LENGTH_SHORT).show();
                }
            }
        });

        barCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expName = experiment.getExpName();
                String category = experiment.getCategory();
                if (category.equals("binomial") || category.equals("count")){
                    data = expName + " | " + category;
                    if (category.equals("binomial")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(experimentInfo_owner.this).setTitle("Pass or Fail?")
                                .setPositiveButton("Pass", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        choose = "1";
                                        data = data + " | " + choose;
                                        Intent intent = new Intent(experimentInfo_owner.this, barcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        choose = "0";
                                        data = data + " | " + choose;
                                        Intent intent = new Intent(experimentInfo_owner.this, barcodeView.class);
                                        intent.putExtra("exp",data);
                                        startActivity(intent);
                                    }
                                });
                        builder.create().show();
                    }
                    if (category.equals("count")){
                        data = data + " | 1";
                        Intent intent = new Intent(experimentInfo_owner.this, barcodeView.class);
                        intent.putExtra("exp",data);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(experimentInfo_owner.this, "This type of experiment currently does not support generate the barCode", Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aSwitch.setClickable(false);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentCollectionReference
                        .document(experiment.getExpName())
                        .delete();
                finish();
            }
        });

        unPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> expStatus = new HashMap<>();
                expStatus.put("Status", "end");
                experiment.setPublishedToFalse();
                experimentCollectionReference
                        .document(experiment.getExpName())
                        .set(expStatus, SetOptions.merge());
                finish();
            }
        });

//        qrCode.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//
//                qrgEncoder = new QRGEncoder("test", null, QRGContents.Type.TEXT, 350);
//                try {
//                    bitmap = qrgEncoder.encodeAsBitmap();
//
//                } catch (
//                        WriterException e) {
//                    Log.e("Tag", e.toString());
//                }
//
//                QRFragment qrFragment = QRFragment.newInstance(bitmap);
//                qrFragment.show(getSupportFragmentManager(), "qrfrag");
//            }
//        });

        Button questionButton = findViewById(R.id.Question_Forum);
        questionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showQuestionInfo();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                locationManager.removeUpdates(locationListener);
                //Toast.makeText(experimentInfo_owner.this, "update", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(experimentInfo_owner.this, MapsActivity.class);
                intent1.putExtra("lati", location.getLatitude());
                intent1.putExtra("longi", location.getLongitude());
                startActivity(intent1);
            }
        };
    }

    ////////////

    void showQuestionInfo() {
        Intent intent = new Intent(this, QuestionListActivity.class);
        intent.putExtra("experiment", experiment);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    public void showMap(View view) {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(experimentInfo_owner.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);

                //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, );
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            }

        }else{
            showAlert();
        }
    }



    private void showAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is currently disabled")
                .setCancelable(false)
                .setPositiveButton("Enable GPS service",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}