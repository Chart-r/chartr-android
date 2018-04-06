package com.example.mac.chartr.activities;

import android.app.Activity;
import android.content.Intent;

import com.example.mac.chartr.CommonDependencyProvider;

import junit.framework.Assert;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael Rush on 3/4/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }

    /**
     * PENDING ACTIVATION:  Currently, Mocking the main activity without a prior login
     * breaks assumptions about the precondition to the MainActivity.
     * We will update this method as we find solutions to that problem.
     */

    @Test
    public void stub() {
        Assert.assertTrue(true);
    }

//    @Test
//    public void ExitTest(){
//        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
//        Intent intent = mock(Intent.class);
//
//        activity.exit();
//        Assert.assertTrue(activity.isFinishing());
//    }
}
