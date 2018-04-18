package com.example.mac.chartr.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.BuildConfig;
import com.example.mac.chartr.objects.Trip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SearchFragmentTest {
    Geocoder geocoder = mock(Geocoder.class);


    @Test
    public void shouldNotBeNull() throws Exception {
        SearchFragment fragment = new SearchFragment();
        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment);
    }

    @Test
    public void testAddTripView() {
        SearchFragment fragment = mock(SearchFragment.class);
        TextView tv = mock(TextView.class);
        LayoutInflater inflater = mock(LayoutInflater.class);
        View view = mock(View.class);

        LinearLayout layout = mock(LinearLayout.class);
        Trip trip = new Trip(123, 123, true, false,
                20.3f, 20.4f, 30.4f, 23.4f, 3,
                12.5f, "nnnn", new HashMap<>());
        doCallRealMethod().when(fragment).addTripView(any(LinearLayout.class), any(Trip.class));
        when(fragment.getLayoutInflater()).thenReturn(inflater);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class)))
                .thenReturn(view);
        when(view.findViewById(any(Integer.class))).thenReturn(tv);


        fragment.addTripView(layout, trip);
    }
}
