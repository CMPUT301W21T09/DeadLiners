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
    private Question question;
    private ArrayList<QuestionOrReply> replies;
    private ListView replyList;
    private ArrayAdapter<QuestionOrReply> replyAdapter;
    private String user_uid;
    private TextView description;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questioninfo);

        Intent intent = getIntent();

        //question = (Question) intent.getSerializableExtra("question");

        user_uid = "test_reply_user";
        question = new Question("Are you ready?",user_uid);

        description = findViewById(R.id.descriptionContent);
        description.setText(question.getDescription());
        username = findViewById(R.id.ownerNameTextView);
        username.setText(user_uid);

        Reply r1 = new Reply("reply A",user_uid);
        Reply r2 = new Reply("reply B",user_uid);
        replies = new ArrayList<>();
        replies.add(r1);
        replies.add(r2);

        replyList = findViewById(R.id.replyList);
        replyAdapter = new QACustomList(this, replies);
        replyList.setAdapter(replyAdapter);

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

    }

    @Override
    public void onOKPressed(String description) {
        replies.add(new Reply(description,user_uid));
        replyAdapter.notifyDataSetChanged();
    }

    private void showReplyInfo(QuestionOrReply reply_clicked){
        Intent new_intent = new Intent(this, ReplyInfoActivity.class);
        new_intent.putExtra("Reply", reply_clicked);
        startActivity(new_intent);
    }
}
