package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

public class QuestionInfoActivity extends AppCompatActivity
        implements AddQAFragment.OnAddFragmentInteractionListener
{
    private QuestionOrReply question;
    private ArrayList<QuestionOrReply> replies;
    private ListView replyList;
    private ArrayAdapter<QuestionOrReply> replyAdapter;
    private String user_uid;
    private TextView description;
    private TextView username;

    FirebaseFirestore db;
    CollectionReference replyReference;
    String TAG = "Add Reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questioninfo);

        Intent intent = getIntent();
        question = (QuestionOrReply) intent.getSerializableExtra("question");
        user_uid = intent.getStringExtra("uid");

        description = findViewById(R.id.descriptionContent);
        description.setText(question.getDescription());
        username = findViewById(R.id.ownerNameTextView);
        username.setText(question.getPublisher_uid());

        replies = new ArrayList<>();
        replyList = findViewById(R.id.replyList);
        replyAdapter = new QACustomList(this, replies);
        replyList.setAdapter(replyAdapter);

        db = FirebaseFirestore.getInstance();
        replyReference = db.collection("Replies");

        FloatingActionButton addExpButton = findViewById(R.id.add_reply_button);
        addExpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AddQAFragment().show(getSupportFragmentManager(),"Reply the Question");
            }
        });

        replyList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionOrReply reply_clicked = replyAdapter.getItem(position);
                showReplyInfo( reply_clicked );
            }
        });

        replyReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                replies.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String parent = (String) doc.getData().get("parent");
                    if(question.getUniqueName().equals(parent)) {
                        String description = (String) doc.getData().get("description");
                        String publisher = (String) doc.getData().get("publisher_uid");
                        String time = (String) doc.getData().get("time");
                        replies.add(new QuestionOrReply(description, publisher, time, false));
                    }
                }
                replyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onOKPressed(String description) {
        String current_time = String.format("%d",currentTimeMillis());

        HashMap<String, Object> data = new HashMap<>();
        data.put("description",description);
        data.put("publisher_uid",user_uid);
        data.put("time",current_time);
        data.put("parent",question.getUniqueName());

        QuestionOrReply new_reply = new QuestionOrReply(description,user_uid, current_time,false);

        replyReference
                .document(new_reply.getUniqueName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        replies.add(new_reply);
        replyAdapter.notifyDataSetChanged();
    }

    private void showReplyInfo(QuestionOrReply reply_clicked){
        Intent new_intent = new Intent(this, ReplyInfoActivity.class);
        new_intent.putExtra("Reply", reply_clicked);
        startActivity(new_intent);
    }
}
