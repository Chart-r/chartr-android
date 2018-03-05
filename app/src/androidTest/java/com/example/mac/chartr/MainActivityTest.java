package com.example.mac.chartr;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.mac.chartr.activities.PostTripActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Michael Rush on 3/4/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    final String TAG = "PostTripTest";
    @Rule
    public ActivityTestRule<PostTripActivity> mActivityRule = new ActivityTestRule(PostTripActivity.class);

    @Test
    public void decrementSeatsButtonTest() {
//        mActivityRule.getActivity().findViewById()
        onView(withId(R.id.seatsDecrement))        // withId(R.id.my_view) is a ViewMatcher
                .perform(click());
        onView(withId(R.id.seatValue)).check(matches(withText("1")));
    }

}
