package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTripsFragment extends Fragment {
    private static final String TAG = ListTripsFragment.class.getSimpleName();
    public static final String TRIP_TYPE_KEY = "TripTypeKey";

    private CommonDependencyProvider provider;

    public ListTripsFragment() {
        // Required empty public constructor
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
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

            // Populate scrollview
            final LinearLayout tripsLinearLayout = root.findViewById(R.id.tripsLinearLayout);

            // Gets all trips for logged in user
            CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
            String userEmail = commonDependencyProvider.getAppHelper().getCurrUser();
            ApiInterface apiInterface = ApiClient.getApiInstance();
            Call<List<Trip>> call = apiInterface.getAllUserTrips(userEmail);
            call.enqueue(new Callback<List<Trip>>() {
                @Override
                public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                    Log.d(TAG,response.code()+"");

                    List<Trip> resource = response.body();
                    for (int i = 0; i < resource.size(); i++) {
                        addTripView(tripsLinearLayout, resource.get(i));
                    }
                }

                @Override
                public void onFailure(Call<List<Trip>> call, Throwable t) {
                    Log.d(TAG, "Retrofit failed to get data");
                    Log.e(TAG, t.getMessage());
                    t.printStackTrace();
                    call.cancel();
                }
            });
        }

        return root;
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     *
     * @param parentLayout The layout to which a trip view is to be added
     * @param trip The trip details to be added
     */
    protected void addTripView(LinearLayout parentLayout, Trip trip) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container, parentLayout,false);

        // Set TextViews with appropriate data
        String name = provider.getAppHelper().getLoggedInUser().getName();
        String rating = String.valueOf(provider.getAppHelper().getLoggedInUser().getRating());
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