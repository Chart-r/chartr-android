package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.chartr.R;

/**
 * Trip fragment is the main fragment that supports the different type of trips, namely:
 * Confirmed, Pending, and Posted. The different tabs are controlled from this fragment.
 */
public class TripsFragment extends Fragment {
    public static final String TAG = TripsFragment.class.getSimpleName();

    public TripsFragment() {
        // Required empty public constructor
    }

    /**
     * Method inherited from the Fragment class that is called upon creation of the fragment
     *
     * @param savedInstanceState Bundle of the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method inherited from the Fragment class that returns a view that has been inflated
     * with the container argument
     * @param inflater Used to inflate the returned object
     * @param container The viewGroup used in the inflation of the returned object
     * @param savedInstanceState Bundle of the saved instance state
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trips, container, false);
        FragmentTabHost tabHost = root.findViewById(android.R.id.tabhost);
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
