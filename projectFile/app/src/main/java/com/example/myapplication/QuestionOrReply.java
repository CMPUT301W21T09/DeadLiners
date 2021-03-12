package com.example.myapplication;

import java.sql.Date;

public interface QuestionOrReply {
    public String getDescription();
    public User getPublisher();
    public Date getTime();
    public String getText();
}
