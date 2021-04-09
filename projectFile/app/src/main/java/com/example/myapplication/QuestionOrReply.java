package com.example.myapplication;



import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import static java.lang.System.currentTimeMillis;

//class to store a Question or A Reply
public class QuestionOrReply implements Serializable{
    private boolean isQuestion;     //to divide Question and Reply
    private String description;
    private String publisher_uid;
    private long time;

    public QuestionOrReply(String description, String publisher_uid, String time, boolean isQuestion) {
        this.description = description;
        this.publisher_uid = publisher_uid;
        this.time = Long.parseLong(time);
        this.isQuestion = isQuestion;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() { //following instruction https://stackabuse.com/how-to-get-current-date-and-time-in-java/
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    public String getPublisher_uid() {
        return publisher_uid;
    }

    public String getText(int position){ //generate text to display on screen
        return String.format("%s %d by %s",isQuestion?"Question":"Reply",position,publisher_uid);
    }

    public String getUniqueName(){  //use publisher and time to generate a unique name, used for name on firebase
        String time_str=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(time));
        return String.format("%s by %s at %s",isQuestion?"Question":"Reply",publisher_uid,time_str);
    }
}
