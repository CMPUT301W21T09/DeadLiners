package com.example.myapplication;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QuestionReplyTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    void wait_click(String text){
        solo.waitForText(text, 1, 2000);
        solo.clickOnText(text);
    }

    @Test
    public void checkQuestionReplyPart(){
        wait_click("start");
        //main  experiment list
        solo.sleep(2000);
        solo.clickOnView( solo.getView(R.id.imageButton_add) );

        solo.enterText((EditText) solo.getView(R.id.Name_editText), "Android Test Question");
        solo.clickOnText("OK");
        wait_click("Android Test Question");

        //experiment info
        wait_click("Question Forum");
        //experiment list
        solo.clickOnView( solo.getView(R.id.add_question_button) );
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "AndroidTestQuestion1");
        solo.clickOnText("OK");
        wait_click("Question");
        //question info
        solo.waitForText("AndroidTestQuestion1", 1, 2000);
        solo.clickOnView( solo.getView(R.id.add_reply_button) );
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "AndroidTestReply");
        solo.clickOnText("OK");
        wait_click("Reply");
        //reply
        solo.waitForText("AndroidTestReply", 1, 2000);
        //go back to main
        solo.clickOnView( solo.getView(R.id.replyinfo_back) );
        solo.clickOnView( solo.getView(R.id.questioninfo_back_button) );
        solo.clickOnView( solo.getView(R.id.questionlist_back_button) );
        solo.clickOnText("back");
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
