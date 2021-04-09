package com.example.myapplication;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class StartPageTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void click_to_start(){
        //solo.waitForText("Dead",1,2000);
        //solo.assertCurrentActivity("Wrong Activity", LoginActivity);
        solo.clickOnView(solo.getView(R.id.start));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void click_profile(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_user));
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.profile_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void click_add_exp_fragment(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_add));
        solo.waitForText("Add Experiment",1,2000);
    }

    @Test
    public void click_search(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_search));
        solo.clickOnView(solo.getView(R.id.button_user));
        solo.assertCurrentActivity("Wrong Activity",SearchUserActivity.class);
    }

    @Test
    public void edit_profile(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_user));
        solo.waitForText("Default Username",1,20000);
        solo.enterText((EditText) solo.getView(R.id.profile_username), "DeadLiners");
        solo.clickOnView(solo.getView(R.id.profile_edit));
        solo.clickOnView(solo.getView(R.id.imageButton_user));
        solo.waitForText("DeadLiners",1,2000);
        String username = ((EditText) solo.getView(R.id.profile_username)).getText().toString();
        assertEquals("Default UsernameDeadLiners",username);
    }

    @Test
    public void add_and_delete_exp(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_add));
        solo.enterText((EditText) solo.getView(R.id.Name_editText), "Android Test");
        solo.enterText((EditText) solo.getView(R.id.description_editText), "This is for android test");
        solo.clickOnView(solo.getView(R.id.Count));
        solo.enterText((EditText) solo.getView(R.id.Region_editText), "China");
        solo.enterText((EditText) solo.getView(R.id.Number_Of_Trails_editText), "10");
        solo.clickOnView(solo.getView(R.id.NotRequired));
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("Android Test",1,2000));
        solo.clickOnText("Android Test");
        solo.clickOnView(solo.getView(R.id.End));
        assertFalse(solo.waitForText("Android Test",1,2000));
    }

    @Test
    public void make_search(){
        solo.clickOnView(solo.getView(R.id.start));
        solo.clickOnView(solo.getView(R.id.imageButton_add));
        solo.enterText((EditText) solo.getView(R.id.Name_editText), "Android Test");
        solo.enterText((EditText) solo.getView(R.id.description_editText), "This is for android test");
        solo.clickOnView(solo.getView(R.id.Count));
        solo.enterText((EditText) solo.getView(R.id.Region_editText), "China");
        solo.enterText((EditText) solo.getView(R.id.Number_Of_Trails_editText), "10");
        solo.clickOnView(solo.getView(R.id.NotRequired));
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.imageButton_search));
        solo.clickOnView(solo.getView(R.id.button_exp));
        assertTrue(solo.waitForText("Android Test",1,2000));
    }
}
