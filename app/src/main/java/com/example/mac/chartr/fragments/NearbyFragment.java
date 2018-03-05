package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mac.chartr.R;

public class NearbyFragment extends Fragment {
    public static final String TAG = NearbyFragment.class.getSimpleName();


    public NearbyFragment() {
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Nearby");
        ((AppCompatActivity) getActivity()).findViewById(R.id.buttonAddTrip).setVisibility(View.GONE);

        View root = inflater.inflate(R.layout.fragment_nearby, container, false);
        Log.d(TAG, root.toString());
        LinearLayout tripsLinearLayout = root.findViewById(R.id.tripsLinearLayout);
        Log.d(TAG, tripsLinearLayout.toString());
        addTripView(tripsLinearLayout);

        return root;
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     * Duplicate of this method is in ListTripsFragment, possible refactoring in future.
     */
    private void addTripView(LinearLayout parentLayout) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container, parentLayout,false);
        parentLayout.addView(tripContainer);
    }
}
