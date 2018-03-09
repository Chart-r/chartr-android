package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.zip.Inflater;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

/**
 * Created by cygnus on 3/7/18.
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(AppHelper.class)
public class ListTripsFragmentTest {

    @Test
    public void testAddTripView() {
        PowerMockito.mockStatic(AppHelper.class);

        ListTripsFragment fragment = mock(ListTripsFragment.class);

        LayoutInflater inflater = mock(LayoutInflater.class);
        LinearLayout layout = mock(LinearLayout.class);
        View view = mock(View.class);

        TextView textView = mock(TextView.class);


        float start = (float)10.1;
        float end = (float)10.2;
        float price = (float)50;

        Trip trip = new Trip("Now", "Later", true, false, start, end, start, end, 5, price, "1", null);

        when(fragment.getLayoutInflater()).thenReturn(inflater);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class))).thenReturn(view);
        when(view.findViewById(any(int.class))).thenReturn(textView);
        when(AppHelper.getLoggedInUser()).thenReturn(new User("me@there.com", "Joe Smo", 50f, null));
        Mockito.doCallRealMethod().when(fragment).addTripView(any(LinearLayout.class), any(Trip.class));

        fragment.addTripView(layout, trip);

        verify(view,times(5)).findViewById(any(int.class));
        verify(layout, times(1)).addView(any(View.class));
    }

    @Test
    public void testOnCreateView() {
        PowerMockito.mockStatic(AppHelper.class);

        ListTripsFragment fragment = mock(ListTripsFragment.class);

        LayoutInflater inflater = mock(LayoutInflater.class);
        View root = mock(View.class);
        LinearLayout layout = mock(LinearLayout.class);

        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class))).thenReturn(root);
        when(root.findViewById(any(int.class))).thenReturn(layout);

        Mockito.doCallRealMethod().when(fragment).onCreateView(any(LayoutInflater.class), any(ViewGroup.class), any(Bundle.class));

        fragment.onCreateView(inflater, null, null);

        verify(fragment, atLeast(0)).addTripView(Mockito.eq(layout), any(Trip.class));
    }
}
