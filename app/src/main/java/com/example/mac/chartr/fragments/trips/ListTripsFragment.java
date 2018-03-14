package com.example.mac.chartr.fragments.trips;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

            if(getArguments().getString(ListTripsFragment.TRIP_TYPE_KEY).equals("Posted")){
                inflatePostedTripsInLayout(tripsLinearLayout);
            }

        }

        return root;
    }

    private void inflatePostedTripsInLayout(final LinearLayout tripsLinearLayout) {
        // Gets all trips for logged in user
        CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
        String userEmail = commonDependencyProvider.getAppHelper().getLoggedInUser().getEmail();
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getUserDrivingTrips(userEmail);
        Log.d(TAG, userEmail);
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
        String name = trip.getDriverFromUsers();
        String rating = "rating";
        String seats = (trip.getUsers().size() - 1) + "/" + trip.getSeats();
        String start = getLocationName(trip.getStartLat(), trip.getStartLong());
        String destination = getLocationName(trip.getEndLat(), trip.getEndLong());

        ((TextView) tripContainer.findViewById(R.id.textViewName)).setText(name);
        ((TextView) tripContainer.findViewById(R.id.textViewRating)).setText(rating);
        ((TextView) tripContainer.findViewById(R.id.textViewSeats)).setText(seats);
        ((TextView) tripContainer.findViewById(R.id.textViewStart)).setText(start);
        ((TextView) tripContainer.findViewById(R.id.textViewDestination)).setText(destination);

        parentLayout.addView(tripContainer);
    }

    protected String getLocationName(double latitude, double longitude) {
        /* testAddTripView was failing on account of this code, specifically:
         * Method getFromLocation in android.location.Geocoder not mocked.
         * I'm too tired to look into it now.
        */
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e(TAG, ioException.toString());
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e(TAG,  "Lat/Long Error: " +
                    "Latitude = " + latitude +
                    ", Longitude = " +
                    longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            Log.e(TAG, "No address found");
        } else {
            Address address = addresses.get(0);
            String addressString = address.getAddressLine(0);

            // Fetch the address lines using getAddressLine
            for(int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                addressString += ", " + address.getAddressLine(i);
            }
            Log.i(TAG, "Address found");
            return addressString;
        }


        // In the case of failure, return location coordinate string
        return latitude + ", " + longitude;
    }


}