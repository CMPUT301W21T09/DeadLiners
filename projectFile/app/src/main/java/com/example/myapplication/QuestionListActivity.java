package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity
        implements AddQAFragment.OnAddFragmentInteractionListener
{

    private ArrayList<QuestionOrReply> questions; // TODO should use the array in an experiment
    private ListView questionList;
    private ArrayAdapter<QuestionOrReply> questionAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist);

        Intent intent = getIntent(); // TODO use UID to find the user on firebase
        // TODO delete test area
        user = new User("sb");
        Question q1 = new Question("description 1",user);
        Question q2 = new Question("description 2",user);
        questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        // TODO delete test area

        questionList = findViewById(R.id.questionList);
        questionAdapter = new QuestionCustomList(this, questions);
        questionList.setAdapter(questionAdapter);

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
    }

    @Override
    public void onOKPressed(String description) {
        questions.add(new Question(description,user));
        questionAdapter.notifyDataSetChanged();
    }

    private void showQuestionInfo(QuestionOrReply question_clicked){
        Intent new_intent = new Intent(this, QuestionInfoActivity.class);
        new_intent.putExtra("UID", user.getUid());
        new_intent.putExtra("QID",question_clicked.getID()); //TODO
        startActivity(new_intent);
    }
}
