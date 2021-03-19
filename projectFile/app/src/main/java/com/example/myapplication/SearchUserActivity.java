package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    private ListView userSearchList;
    private ArrayAdapter<Experiment> userSearchAdapter;
    private ArrayList<Experiment> userSearchDataList;

    private SearchUserCustomList customList;

    final String TAG = "Sample";
    Button searchUserButton;
    EditText searchUserEditText;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Button back = findViewById(R.id.search_user_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userSearchList = findViewById(R.id.user_list);
        searchUserButton = findViewById(R.id.search_user_button);
        searchUserEditText = findViewById(R.id.search_user_field);

        userSearchDataList = new ArrayList<>();
        userSearchAdapter = new SearchExpCustomList(this, userSearchDataList);
        userSearchList.setAdapter(userSearchAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = searchUserEditText.getText().toString();
                if(inputName.length() > 0) {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            userSearchDataList.clear();
                            for(QueryDocumentSnapshot doc: value) {
                                String userName = (String) doc.getData().get("Username");
                                if(userName.toUpperCase().contains(inputName.toUpperCase())) {
                                    String email = (String) doc.getData().get("Email");
                                    String phone = (String) doc.getData().get("Phone");
                                    userSearchDataList.add(new User(userName, email, phone));
                                }
                            }
                            userSearchAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else {
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            userSearchDataList.clear();
                            for(QueryDocumentSnapshot doc: value) {
                                String userName  = (String) doc.getData().get("Username");
                                String email = (String) doc.getData().get("Email");
                                String phone = (String) doc.getData().get("Phone");
                                userSearchDataList.add(new User(userName, email, phone));
                            }
                            userSearchAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userSearchDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String userName  = (String) doc.getData().get("Username");
                    String email = (String) doc.getData().get("Email");
                    String phone = (String) doc.getData().get("Phone");
                    userSearchDataList.add(new User(userName, email, phone));
                }
                userSearchAdapter.notifyDataSetChanged();
            }
        });
    }
}