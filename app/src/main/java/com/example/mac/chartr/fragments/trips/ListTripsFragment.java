package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mac.chartr.R;

import java.util.List;

public class ListTripsFragment extends Fragment {
    private static final String TAG = ListTripsFragment.class.getSimpleName();
    public static final String TRIP_TYPE_KEY = "TripTypeKey";

    public ListTripsFragment() {
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
        View root = inflater.inflate(R.layout.fragment_list_trips, container, false);
        if (getArguments() != null) {

            LinearLayout tripsLinearLayout = root.findViewById(R.id.tripsLinearLayout);
            addTripView(tripsLinearLayout);
            addTripView(tripsLinearLayout);
            addTripView(tripsLinearLayout);
            addTripView(tripsLinearLayout);
        }

        return root;
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     *
     */
    private void addTripView(LinearLayout parentLayout) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container, parentLayout,false);
        parentLayout.addView(tripContainer);
    }
}