package com.example.mac.chartr.fragments.trips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mac.chartr.R;
import com.example.mac.chartr.activities.PostTripActivity;

public class TripsFragment extends Fragment {
    public static final String TAG = TripsFragment.class.getSimpleName();

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trips, container, false);
        FragmentTabHost tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle arg1 = new Bundle();
        arg1.putString(ListTripsFragment.TRIP_TYPE_KEY, "Confirmed");
        tabHost.addTab(tabHost.newTabSpec("ConfirmedTab").setIndicator("Confirmed"),
                ListTripsFragment.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putString(ListTripsFragment.TRIP_TYPE_KEY, "Pending");
        tabHost.addTab(tabHost.newTabSpec("PendingTab").setIndicator("Pending"),
                ListTripsFragment.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putString(ListTripsFragment.TRIP_TYPE_KEY, "Posted");
        tabHost.addTab(tabHost.newTabSpec("PostedTab").setIndicator("Posted"),
                ListTripsFragment.class, arg3);

        return root;
    }

}
