package com.example.myapplication;

import org.junit.Test;

import static java.lang.System.currentTimeMillis;

public class QuestionReplyTest {

    private String mockUid = "test uid";
    private QuestionOrReply mockQuestion(){
        String time = String.format("%d",currentTimeMillis());
        QuestionOrReply mockQ = new QuestionOrReply("for test", mockUid , time, true);
        return mockQ;
    }

    private QuestionOrReply mockReply(){
        String time = String.format("%d",currentTimeMillis());
        QuestionOrReply mockR = new QuestionOrReply("for test", mockUid , time, true);
        return mockR;
    }


    private QuestionListActivity qListActivity(){


        return new QuestionListActivity();
    }

    @Test
    public void testQListActivity(){
        QuestionListActivity activity = qListActivity();
        //qListActivity().onOKPressed("fortest");
    }
}
