package com.example.mac.chartr.activities;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.mac.chartr.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityInstrumentedTest {

    /*
            BECAUSE of how Cognito works, we need to deactivate this instrumented test
             until we can find a way to eliminate the dependence between the login activity
             and the main activity. Currently, this will break because it breaks the precondition
             of a user being signed in before this activity is started
     */
//    @Rule
//    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    public void firstGeneralTest() {
//        onView(withId(R.id.ic_nearby)).check(matches(withText("Nearby")));
//    }

    @Test
    public void firstGeneralTest() {

    }

}
