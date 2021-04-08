package com.example.myapplication;

import java.sql.Date;

public class Reply {

    private String content;
    private User publisher;
    private Date time;

    public Reply(String content, Date time) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }
}
