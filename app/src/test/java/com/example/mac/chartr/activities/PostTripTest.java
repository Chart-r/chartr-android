package com.example.mac.chartr.activities;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.espresso.matcher.ViewMatchers;
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.activities.MainActivity;
import com.example.mac.chartr.activities.PostTripActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


//import static android.support.test.InstrumentationRegistry.getInstrumentation;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.example.mac.chartr.R;
import com.example.mac.chartr.activities.MainActivity;
import com.example.mac.chartr.fragments.trips.TripsFragment;

import junit.framework.Assert;

/**
 * Created by Michael Rush on 3/3/2018.
 */
/*@RunWith(AndroidJUnit4.class)
@LargeTest*/

@RunWith(RobolectricTestRunner.class)
public class PostTripTest {
    final String TAG = "PostTripTest";
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }

    @Test
    public void stub() {
        Assert.assertTrue(true);
    }

    /*@Rule
    public ActivityTestRule<PostTripActivity> mActivityRule = new ActivityTestRule(PostTripActivity.class);

    @Test
    public void decrementSeatsButtonTest() {
//        mActivityRule.getActivity().findViewById()
        Espresso.onView(ViewMatchers.withId(R.id.seatsDecrement))        // withId(R.id.my_view) is a ViewMatcher
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.seatValue)).check(ViewAssertions.matches(ViewMatchers.withText("1")));
    }

    @Test
    public void incrementSeatsButtonTest() {
//        mActivityRule.getActivity().findViewById()
        Espresso.onView(ViewMatchers.withId(R.id.seatsIncrement))        // withId(R.id.my_view) is a ViewMatcher
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.seatValue)).check(ViewAssertions.matches(ViewMatchers.withText("2")));
    }*/

}



