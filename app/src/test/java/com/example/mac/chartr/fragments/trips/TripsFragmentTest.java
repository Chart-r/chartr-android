package com.example.mac.chartr.fragments.trips;

import android.support.v4.app.FragmentTabHost;
import android.view.View;

import com.example.mac.chartr.BuildConfig;
import com.example.mac.chartr.activities.PostTripActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;

/**
 * Main test class for testing the Trips fragment
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TripsFragmentTest {

    @Test
    public void shouldNotBeNull() throws Exception {
        TripsFragment fragment = new TripsFragment();
        SupportFragmentTestUtil.startFragment(fragment, PostTripActivity.class);
        assertNotNull(fragment);
    }

    @Test
    public void testSetupFragmentTabHost() {
        TripsFragment fragment = new TripsFragment();
        SupportFragmentTestUtil.startFragment(fragment, PostTripActivity.class);
        View root = fragment.getView();

        FragmentTabHost tabHost = root.findViewById(android.R.id.tabhost);

        assertNotNull(tabHost);
    }

}
