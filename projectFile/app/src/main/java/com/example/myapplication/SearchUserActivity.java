package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity implements UserDisplayFragment.OnFragmentInteractionListener {

    private ListView userSearchList;
    private ArrayAdapter<String> userSearchAdapter;
    private ArrayList<String> userSearchDataList;

    private SearchUserCustomList customList;

    final String TAG = "Sample";
    Button searchUserButton;
    EditText searchUserEditText;
    FirebaseFirestore db;

    @Override
    public void onOkPressed() {
    }

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
        userSearchAdapter = new SearchUserCustomList(this,userSearchDataList);
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
                                    userSearchDataList.add(userName);
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
                                userSearchDataList.add(userName);
                            }
                            userSearchAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });

        userSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = userSearchDataList.get(position);
                collectionReference.whereEqualTo("Username", selectedUser)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String email = (String) document.get("Email");
                                        String phone = (String) document.get("Phone");
                                        new UserDisplayFragment(selectedUser, email, phone).show(getSupportFragmentManager(), "USER_INFORMATION");
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userSearchDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String userName  = (String) doc.getData().get("Username");
                    userSearchDataList.add(userName);
                }
                userSearchAdapter.notifyDataSetChanged();
            }
        });



    }
}
