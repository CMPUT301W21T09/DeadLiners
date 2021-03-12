package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity
        implements AddQAFragment.OnAddFragmentInteractionListener
{

    private ArrayList<Question> questions; // TODO should use the array in an experiment
    private ListView questionList;
    private ArrayAdapter<Question> questionAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist);

        // TODO delete test area
        user = new User("sb");
        Question q1 = new Question("description 1",user);
        Question q2 = new Question("description 2",user);
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
    }


    @Override
    public void onOKPressed(String description) {
        questions.add(new Question(description,user));
        questionAdapter.notifyDataSetChanged();
    }
}
