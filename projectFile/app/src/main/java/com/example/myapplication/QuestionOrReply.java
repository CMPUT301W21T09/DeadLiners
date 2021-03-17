package com.example.myapplication;

import java.io.Serializable;
import java.sql.Date;

public interface QuestionOrReply extends Serializable {
    public String getDescription();
    public String getPublisher();
    public Date getTime();
    public String getText(int position);
    public int getID();
}
