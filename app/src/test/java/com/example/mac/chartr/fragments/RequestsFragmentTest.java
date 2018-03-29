package com.example.mac.chartr.fragments;

import com.example.mac.chartr.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by cygnus on 3/13/18.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RequestsFragmentTest {
    @Test
    public void shouldNotBeNull() throws Exception {
        RequestsFragment fragment = new RequestsFragment();
        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment);
    }
}
