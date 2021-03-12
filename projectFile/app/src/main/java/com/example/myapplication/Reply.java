package com.example.myapplication;

import java.sql.Date;

public class Reply implements QuestionOrReply {

    private String description;
    private User publisher;
    private Date time;

    public Reply(String description,User publisher) {
        this.description = description;
        this.publisher = publisher;
        this.time = new Date(System.currentTimeMillis()); //current date
    }

    public String getDescription() {
        return description;
    }

    public User getPublisher() {
        return publisher;
    }

    public Date getTime() {
        return time;
    }

    public String getText(){ return String.format("Reply  by %s", publisher.getUsername() ); }
}
