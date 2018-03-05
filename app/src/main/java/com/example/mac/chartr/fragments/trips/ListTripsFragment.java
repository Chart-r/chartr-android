package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

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
            // Test data to show that functionality is working
            addTripView(tripsLinearLayout, new Trip("10:30pm", "11:30pm", false, true, 30, 30, 40, 40, 4, (float)10.20, "id", new User[] {AppHelper.getLoggedInUser()}));
            addTripView(tripsLinearLayout, new Trip("12:30pm", "4:30pm", false, true, 40, 40, 30, 30, 4, (float)20.0, "id", new User[] {AppHelper.getLoggedInUser()}));
            addTripView(tripsLinearLayout, new Trip("2:30pm", "3:30pm", false, true, 50, 50, 20, 20, 4, (float)23.0, "id", new User[] {AppHelper.getLoggedInUser()}));
            addTripView(tripsLinearLayout, new Trip("1:30pm", "5:30pm", false, true, 60, 60, 10, 10, 4, (float)15.60, "id", new User[] {AppHelper.getLoggedInUser()}));
        }

        return root;
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     *
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