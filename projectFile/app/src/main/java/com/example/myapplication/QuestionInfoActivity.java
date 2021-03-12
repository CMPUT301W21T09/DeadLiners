package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        Intent intent = getIntent(); // TODO find user and question by id on firebase

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

        replyList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionOrReply reply_clicked = replyAdapter.getItem(position);
                showReplyInfo( reply_clicked );
            }
        });

    }

    public void onOKPressed(String description) {
        replies.add(new Reply(description,user));
        replyAdapter.notifyDataSetChanged();
    }

    private void showReplyInfo(QuestionOrReply reply_clicked){
        Intent new_intent = new Intent(this, ReplyInfoActivity.class);
        new_intent.putExtra("UID", user.getUid());
        new_intent.putExtra("RID",reply_clicked.getID()); //TODO
        startActivity(new_intent);
    }
}
