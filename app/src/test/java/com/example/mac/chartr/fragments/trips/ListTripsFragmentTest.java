package com.example.mac.chartr.fragments.trips;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 3/7/18.
 */
public class ListTripsFragmentTest {
    @Mock
    private Geocoder geocoder;

    private CommonDependencyProvider provider;
    private AppHelper helper;

    @Test
    public void testAddTripView() {
        provider = mock(CommonDependencyProvider.class);
        helper = mock(AppHelper.class);

        ListTripsFragment fragment = mock(ListTripsFragment.class);
        Mockito.doCallRealMethod().when(fragment).setCommonDependencyProvider(any(CommonDependencyProvider.class));
        fragment.setCommonDependencyProvider(provider);

        when(provider.getAppHelper()).thenReturn(helper);

        LayoutInflater inflater = mock(LayoutInflater.class);
        LinearLayout layout = mock(LinearLayout.class);
        View view = mock(View.class);

        TextView textView = mock(TextView.class);


        float start = (float) 10.1;
        float end = (float) 10.2;
        float price = (float) 50;

        long startTime = 1000;
        long endTime = 2000;

        Map<String, String> users = new HashMap<>();
        users.put("test@email.com", "Driver");
        Trip trip = new Trip(startTime, endTime, true, false, start, end, start, end, 5, price, "1", users);

        when(fragment.getLayoutInflater()).thenReturn(inflater);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class))).thenReturn(view);
        when(view.findViewById(any(int.class))).thenReturn(textView);

        // TODO create test for getLocationName helper.  May need to refactor to pass geocoder as input
        when(fragment.getLocationName(any(double.class), any(double.class))).thenReturn("Test for getLocationName");

        when(helper.getLoggedInUser()).thenReturn(new User("me@there.com", "Joe Smo", 50f, null));
        Mockito.doCallRealMethod().when(fragment).addTripView(any(LinearLayout.class), any(Trip.class));

        fragment.addTripView(layout, trip);

        verify(view, times(5)).findViewById(any(int.class));
        verify(layout, times(1)).addView(any(View.class));
    }

    @Test
    public void testOnCreateView() {
        ListTripsFragment fragment = mock(ListTripsFragment.class);

        LayoutInflater inflater = mock(LayoutInflater.class);
        View root = mock(View.class);
        LinearLayout layout = mock(LinearLayout.class);

        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class))).thenReturn(root);
        when(root.findViewById(any(int.class))).thenReturn(layout);


        fragment.onCreateView(inflater, null, null);

        verify(fragment, atLeast(0)).addTripView(Mockito.eq(layout), any(Trip.class));
    }
}
