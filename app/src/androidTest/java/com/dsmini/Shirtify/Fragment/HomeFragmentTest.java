package com.dsmini.Shirtify.Fragment;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.dsmini.Shirtify.Activity.MainActivity;
import com.android.shoppingzoo.R;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest extends TestCase {

    private String name="top";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void userInputScenario(){
        //input some text in the edit text of login and password
        Espresso.onView(withId(R.id.name_input)).perform(typeText(name));
        //close the soft keyboard
        Espresso.closeSoftKeyboard();
    }

    public void tearDown() throws Exception {
    }
}