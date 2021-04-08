package com.example.myapplication;

import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    public ArrayList<String> uidList = new ArrayList<String>();
    public Boolean login;
    String uid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        login = bundle.getBoolean("login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);
        Button OK = (Button)findViewById(R.id.login);
        TextView user = (TextView)findViewById(R.id.uid);

        // Save uuid as a unique uid
        SharedPreferences preference = this.getSharedPreferences("identity",Context.MODE_PRIVATE);
        String identity;
        identity = preference.getString("identity",null);
        if (identity == null) {
            identity = java.util.UUID.randomUUID().toString();
            user.setText(identity);
            preference.edit().putString("identity", identity).apply();

        }


        //set the username text
        user.setText(identity);
        uid = identity;
        final String[] username = new String[1];
        final Boolean[] is_new = {true};
        TextView username_view = (TextView)findViewById(R.id.login_username);
        userCollectionReference.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DOC", "DocumentSnapshot data: " + document.getData());
                        username_view.setText(document.getString("Username"));
                        username[0] = document.getString("Username");
                        is_new[0] = false;
                    } else {
                        Log.d("DOC", "No such document");
                    }
                } else {
                    Log.d("DOC", "get failed with ", task.getException());
                }
            }
        });

        // login button
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                uid = user.getText().toString();
                intent.putExtra("return1", uid);
                setResult(1,intent);


                if(is_new[0] == true) {
                    HashMap<String, String> email = new HashMap<>();
                    HashMap<String, String> username = new HashMap<>();
                    HashMap<String, String> phone = new HashMap<>();
                    HashMap<String, Location> location = new HashMap<>();

                    email.put("Email", "Default Email");
                    username.put("Username", "Default Username");
                    phone.put("Phone", "Default Phone number");
                    //location.put("Location",(0,0));
                    userCollectionReference.document(uid)
                            .set(email);
                    userCollectionReference.document(uid)
                            .set(username, SetOptions.merge());
                    userCollectionReference.document(uid)
                            .set(phone, SetOptions.merge());
                    intent.putExtra("return2", "Defalt Username");
                    finish();
                }else{
                    intent.putExtra("return2", username[0]);
                    finish();
                }
            }
        });


    }
}
