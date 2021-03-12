package com.example.myapplication;

import android.location.Location;

import java.sql.Date;
import java.util.ArrayList;

public class Question extends Reply implements QuestionOrReply
{
    private ArrayList<Reply> replies;

    public Question(String description, User publisher) {
        super(description, publisher);
        replies = new ArrayList<Reply>();
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }
}
