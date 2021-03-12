package com.example.myapplication;

import android.app.AppComponentFactory;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity {
    public ArrayList<User> userArrayList;
    public ArrayList<String> uidList;
    String uid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    CollectionReference userCollectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userArrayList = new ArrayList<User>();
        userCollectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DOC", document.getId() + " => " + document.getData());
                                //Toast.makeText(LoginActivity.this,document.getId() + "=> " + document.getData(),Toast.LENGTH_SHORT).show();
                                User user = new User(document.getId());
                                uidList.add(document.getId());
                            }
                        } else {
                            Log.d("DOC", "Error getting documents: ", task.getException());
                        }
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);
        Button OK = (Button)findViewById(R.id.login);
        EditText user = (EditText)findViewById(R.id.uid);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                uid = user.getText().toString();
                intent.putExtra("return", uid);
                setResult(1,intent);
                finish();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
