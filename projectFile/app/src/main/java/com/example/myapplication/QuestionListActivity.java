package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

public class QuestionListActivity extends AppCompatActivity
        implements AddQAFragment.OnAddFragmentInteractionListener
{

    private ArrayList<QuestionOrReply> questions; // TODO should use the array in an experiment
    private ListView questionList;
    private ArrayAdapter<QuestionOrReply> questionAdapter;
    private String user_uid;
    private Experiment experiment;

    FirebaseFirestore db;
    CollectionReference questionReference;
    String TAG = "Add Question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");
        user_uid = intent.getStringExtra("uid");

        questions = new ArrayList<>();

        questionList = findViewById(R.id.questionList);
        questionAdapter = new QACustomList(this, questions);
        questionList.setAdapter(questionAdapter);

        db = FirebaseFirestore.getInstance();
        questionReference = db.collection("Questions");

        FloatingActionButton addExpButton = findViewById(R.id.add_question_button);
        addExpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AddQAFragment().show(getSupportFragmentManager(),"new Question");
            }
        });

        questionList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionOrReply question_clicked = questionAdapter.getItem(position);
                showQuestionInfo( question_clicked );
            }
        });

        questionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                questions.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String parent = (String) doc.getData().get("parent");
                    if(experiment.getExpName().equals(parent)) {
                        String description = (String) doc.getData().get("description");
                        String publisher = (String) doc.getData().get("publisher_uid");
                        String time = (String) doc.getData().get("time");
                        questions.add(new QuestionOrReply(description, publisher, time, true));
                    }
                }
                questionAdapter.notifyDataSetChanged();
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
        data.put("parent",experiment.getExpName());

        QuestionOrReply new_question = new QuestionOrReply(description,user_uid, current_time,true);

        questionReference
                .document(new_question.getUniqueName())
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

        questions.add(new_question);
        questionAdapter.notifyDataSetChanged();
    }

    private void showQuestionInfo(QuestionOrReply question_clicked) {
        Intent new_intent = new Intent(this, QuestionInfoActivity.class);
        new_intent.putExtra("question", question_clicked);
        new_intent.putExtra("uid",user_uid);
        startActivity(new_intent);
    }
}
