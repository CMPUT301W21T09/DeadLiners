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
    public void click_add_exp(){

    }
}
