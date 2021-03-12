package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QuestionCustomList extends ArrayAdapter<Question> {
    private ArrayList<Question> questions;
    private Context context;

    public QuestionCustomList(@NonNull Context context, ArrayList<Question> questions) {
        super(context, 0, questions);
        this.questions = questions;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.simple_one_line_content,parent,false);
        }

        Question question = questions.get(position);

        TextView textView = view.findViewById(R.id.StringTextView);
        //textView.setText(String.format("Question %d by %s",
         //       position,question.getUser().get ));
        textView.setText(String.format("Question %d by %s",
                       position,"user_name TODO" ));
        return view;
    }
}
