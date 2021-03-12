package com.example.myapplication;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    public ArrayList<User> userArrayList = new ArrayList<User>();
    public ArrayList<String> uidList = new ArrayList<String>();
    String uid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    CollectionReference userCollectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //userArrayList = new ArrayList<User>();
        userCollectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DOC", document.getId() + " => " + document.getData());
                                //Toast.makeText(LoginActivity.this,document.getId() + "=> " + document.getData(),Toast.LENGTH_SHORT).show();
                                uidList.add(document.getId());
                                //Toast.makeText(LoginActivity.this, uidList.size(),Toast.LENGTH_SHORT).show();
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
        for (int i=0; i < uidList.size(); i++){
            Toast.makeText(LoginActivity.this, uidList.get(i),Toast.LENGTH_SHORT).show();
        }
        if(uidList.isEmpty()){
            user.setText("u001");
        }else{
            String str = null;
            str = String.format("u%03d",uidList.size()+1);
            user.setText(str);
        }
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                uid = user.getText().toString();
                intent.putExtra("return", uid);
                setResult(1,intent);
                HashMap<String,String> email = new HashMap<>();
                HashMap<String,String> username = new HashMap<>();
                email.put("Email","Default Email");
                username.put("Username","default Username");
                userCollectionReference.document(uid)
                        .set(email);
                userCollectionReference.document(uid)
                        .set(username, SetOptions.merge());
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
