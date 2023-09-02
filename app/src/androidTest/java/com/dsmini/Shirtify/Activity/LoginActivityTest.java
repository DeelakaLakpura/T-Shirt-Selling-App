package com.dsmini.Shirtify.Activity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
public class LoginActivityTest extends TestCase {

    private String email="testing@gmail.com";
    private String pass="11223344";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void userInputScenario(){
        //input some text in the edit text of login and password
        Espresso.onView(withId(R.id.login_email)).perform(typeText(email));
        Espresso.onView(withId(R.id.login_pass)).perform(typeText(pass));
        //close the soft keyboard
        Espresso.closeSoftKeyboard();
        //perform click on login button
        Espresso.onView(withId(R.id.login_btn)).perform(click());
        Espresso.onView(withId(R.id.login_results)).check(matches(withText("pass")));
    }

    public void tearDown() throws Exception {
    }
}