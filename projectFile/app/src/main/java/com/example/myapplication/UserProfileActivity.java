package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {
    public User userinfo;
    public String uid;
    public TextView uid_show;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get the uid from main activity
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("login_uid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        //set the TextViews
        uid_show = (TextView)findViewById(R.id.profile_uid);
        uid_show.setText(uid);
        EditText show_username = (EditText) findViewById(R.id.profile_username);
        EditText show_phone = (EditText)findViewById(R.id.profile_phone);
        EditText show_email = (EditText)findViewById(R.id.profile_email);
        final String[] username = new String[1];
        final String[] email = new String[1];
        final String[] phone = new String[1];


        //get the profile information from firebase
        userCollectionReference.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DOC", "DocumentSnapshot data: " + document.getData());
                        username[0] = document.getString("Username");
                        email[0] = document.getString("Email");
                        phone[0] = document.getString("Phone");
                        show_username.setText(username[0]);
                        show_email.setText(email[0]);
                        show_phone.setText(phone[0]);
                    } else {
                        Log.d("DOC", "No such document");
                    }
                } else {
                    Log.d("DOC", "get failed with ", task.getException());
                }
            }
        });


        //set the back button
        Button back = (Button)findViewById(R.id.profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //set the edit button and update the information to firebase
        Button edit = (Button)findViewById(R.id.profile_edit);
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(1,intent);

                username[0] = String.valueOf(show_username.getText());
                email[0] = String.valueOf(show_email.getText());
                phone[0] = String.valueOf(show_phone.getText());
                HashMap<String,String> email_hash = new HashMap<>();
                HashMap<String,String> username_hash = new HashMap<>();
                HashMap<String,String> phone_hash = new HashMap<>();
                email_hash.put("Email",email[0]);
                username_hash.put("Username",username[0]);
                phone_hash.put("Phone",phone[0]);
                userCollectionReference.document(uid)
                        .set(email_hash);
                userCollectionReference.document(uid)
                        .set(username_hash, SetOptions.merge());
                userCollectionReference.document(uid)
                        .set(phone_hash,SetOptions.merge());
                intent.putExtra("return2", String.valueOf(show_username.getText()));
                intent.putExtra("return1", uid);
                finish();
            }
        });
    }


}
