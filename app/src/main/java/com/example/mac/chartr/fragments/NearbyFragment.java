package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

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

        // Populate scrollview
        LinearLayout tripsLinearLayout = root.findViewById(R.id.tripsLinearLayout);
        // TODO: implement api call for get trip, store formatted result in trips variable
        // Test data to show that functionality is working
        Trip[] trips = {
                new Trip("Start Time", "End Time", false, true, 0, 0, 0, 0, 4, (float) 0, "id", new User[]{AppHelper.getLoggedInUser()})
        };

        for (Trip trip : trips) {
            addTripView(tripsLinearLayout, trip);
        }

        return root;
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     * Duplicate of this method is in ListTripsFragment, possible refactoring in future.
     */
    private void addTripView(LinearLayout parentLayout, Trip trip) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container, parentLayout,false);

        // Set TextViews with appropriate data
        String name = AppHelper.getLoggedInUser().getName();
        String rating = String.valueOf(AppHelper.getLoggedInUser().getRating());
        String seats = String.valueOf(trip.getSeats());
        String start = String.valueOf(trip.getStartLat()) + "," + String.valueOf(trip.getStartLong());
        String destination = String.valueOf(trip.getEndLat()) + "," + String.valueOf(trip.getEndLong());

        ((TextView) tripContainer.findViewById(R.id.textViewName)).setText(name);
        ((TextView) tripContainer.findViewById(R.id.textViewRating)).setText(rating);
        ((TextView) tripContainer.findViewById(R.id.textViewSeats)).setText(seats);
        ((TextView) tripContainer.findViewById(R.id.textViewStart)).setText(start);
        ((TextView) tripContainer.findViewById(R.id.textViewDestination)).setText(destination);

        parentLayout.addView(tripContainer);
    }
}
