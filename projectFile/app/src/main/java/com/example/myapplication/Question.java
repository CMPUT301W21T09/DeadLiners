package com.example.myapplication;

import android.location.Location;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Question extends Reply implements QuestionOrReply
{
    private ArrayList<Reply> replies;

    public Question(String description, String publisher_uid) {
        super(description, publisher_uid);
        replies = new ArrayList<Reply>();
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public String getText(int position){ return String.format("Question %d by %s", position, publisher_uid ); }
}
