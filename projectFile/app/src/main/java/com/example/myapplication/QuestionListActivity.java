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
    private String user_uid;
    private Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("experiment");

        user_uid = "test_user";

        Question q1 = new Question("description A",user_uid);
        Question q2 = new Question("description B",user_uid);
        questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        // TODO delete test area

        questionList = findViewById(R.id.questionList);
        questionAdapter = new QACustomList(this, questions);
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
        questions.add(new Question(description,user_uid));
        questionAdapter.notifyDataSetChanged();
    }

    private void showQuestionInfo(QuestionOrReply question_clicked) {
        Intent new_intent = new Intent(this, QuestionInfoActivity.class);
        //new_intent.putExtra("question", question_clicked);
        startActivity(new_intent);
    }
}
