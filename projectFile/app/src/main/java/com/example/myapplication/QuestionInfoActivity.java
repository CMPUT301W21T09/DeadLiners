package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuestionInfoActivity extends AppCompatActivity
        implements AddQAFragment.OnAddFragmentInteractionListener
{
    private ArrayList<QuestionOrReply> replies; // TODO should use the array in an experiment
    private ListView replyList;
    private ArrayAdapter<QuestionOrReply> replyAdapter;
    private User user;
    private TextView description;
    private TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questioninfo);

        Intent intent = getIntent(); // TODO use getExtra to handle User and Question?

        description = findViewById(R.id.descriptionContent);
        username = findViewById(R.id.ownerNameTextView);
        replyList = findViewById(R.id.replyList);
        replyAdapter = new QuestionCustomList(this, replies);
        replyList.setAdapter(replyAdapter);

        FloatingActionButton addExpButton = findViewById(R.id.add_question_button);
        addExpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AddQAFragment().show(getSupportFragmentManager(),"Reply the Question");
            }
        });
    }

    public void onOKPressed(String description) {
        replies.add(new Reply(description,user));
        replyAdapter.notifyDataSetChanged();
    }
}
