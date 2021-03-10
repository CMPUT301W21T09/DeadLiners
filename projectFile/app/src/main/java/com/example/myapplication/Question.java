package com.example.myapplication;

import java.sql.Date;
import java.util.ArrayList;

public class Question {
    private String description;
    private User owner;
    private Date time;
    private ArrayList<Reply> replies;

    public Question(String description, Date time) {
        this.description = description;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public Date getTime() {
        return time;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }
}
