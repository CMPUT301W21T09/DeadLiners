package com.example.myapplication;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.sql.Date;

public class Reply implements QuestionOrReply {

    private String description;
    String publisher_uid;
    private Date time;
    private int id;

    public Reply(String description,String publisher_uid) {
        this.description = description;
        this.publisher_uid = publisher_uid;
        this.time = new Date(System.currentTimeMillis()); //current date
        //TODO get ID from firebase
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher_uid;
    }

    public Date getTime() {
        return time;
    }

    public String getText(int position){ return String.format("Reply %d by %s", position, publisher_uid ); }

    public int getID() { return id; }
}
