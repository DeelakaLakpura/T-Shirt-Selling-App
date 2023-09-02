package com.dsmini.Shirtify.Activity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.android.shoppingzoo.R;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignupUserActivityTest extends TestCase {


    private String name="testing@gmail.com";
    private String email="testing@gmail.com";
    private String pass="11223344";
    private String address="Singapore";



    @Rule
    public ActivityTestRule<SignupUserActivity> mActivityRule = new ActivityTestRule<SignupUserActivity>(SignupUserActivity.class);

    public void setUp() throws Exception {
        super.setUp();
    }


    @Test
    public void userInputScenario(){
        //input some text in the edit text of login and password
        Espresso.onView(withId(R.id.signup_username)).perform(typeText(name));
        Espresso.onView(withId(R.id.signup_email)).perform(typeText(email));
        Espresso.onView(withId(R.id.signp_pass)).perform(typeText(pass));
        Espresso.onView(withId(R.id.location_et)).perform(typeText(address));
        //close the soft keyboard
        Espresso.closeSoftKeyboard();
        //perform click on login button
        Espresso.onView(withId(R.id.signup_btn)).perform(click());
    }

    public void tearDown() throws Exception {
    }
}