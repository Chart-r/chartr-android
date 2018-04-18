package com.example.mac.chartr.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getSimpleName();


    public SearchFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        view.findViewById(R.id.search_relative_layout).setVisibility(View.GONE);
        Button search = (Button) view.findViewById(R.id.searchFragmentButtonSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTrips(view.findViewById(R.id.search_parent_layout));
            }
        });

        return view;
    }

    public void searchTrips(final View view) {
        view.findViewById(R.id.search_relative_layout).setVisibility(View.GONE);

        String startLocation =
                getStringFromEditText(view, R.id.searchFragmentEditTextStartLocation);
        String endLocation = getStringFromEditText(view, R.id.searchFragmentEditTextEndLocation);
        final String preferredDriverEmail =
                getStringFromEditText(view, R.id.searchFragmentEditPreferredDriver);
        final float priceRangeFrom =
                getFloatFromEditText(view, R.id.searchFragmentEditPriceRangeFrom, 0f);
        final float priceRangeTo = getFloatFromEditText(
                view, R.id.searchFragmentEditPriceRangeTo, Float.MAX_VALUE);

        final double startLat;
        final double startLng;
        final double endLat;
        final double endLng;
        try {
            Geocoder geocoder = new Geocoder(this.getContext());
            List<Address> addresses;

            addresses = geocoder.getFromLocationName(startLocation, 1);
            if (addresses.size() > 0) {
                startLat = addresses.get(0).getLatitude();
                startLng = addresses.get(0).getLongitude();
            } else {
                return;
            }

            addresses = geocoder.getFromLocationName(endLocation, 1);
            if (addresses.size() > 0) {
                endLat = addresses.get(0).getLatitude();
                endLng = addresses.get(0).getLongitude();
            } else {
                return;
            }

        } catch (IOException error) {
            Log.e(TAG, "Error getting location coordinates for: " + startLocation);
            return;
        }

        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getAllCurrentTrips();
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
                String userEmail =
                        commonDependencyProvider.getAppHelper().getLoggedInUser().getEmail();

                LinearLayout tripsLayout = view.findViewById(R.id.searchTripsLinearLayout);

                List<Trip> tripList = response.body();
                List<Trip> result = new ArrayList<Trip>();
                for (int i = 0; i < tripList.size(); i++) {
                    Trip currTrip = tripList.get(i);

                    boolean costOfTripWithinRange = priceRangeFrom < currTrip.getPrice()
                            && currTrip.getPrice() < priceRangeTo;
                    boolean hasDriverPreference = !preferredDriverEmail.isEmpty();
                    boolean isNotDriver = !userEmail.equals(currTrip.getDriverFromUsers());

                    float startDistance = computeDistanceBetween(startLat, startLng,
                            currTrip.getStartLat(), currTrip.getStartLong());
                    float endDistance = computeDistanceBetween(endLat, endLng,
                            currTrip.getEndLat(), currTrip.getEndLong());

                    if (isNotDriver && costOfTripWithinRange
                            && endDistance < 5000f && startDistance < 5000f) {
                        if (!hasDriverPreference
                                || currTrip.getDriverFromUsers().contains(preferredDriverEmail)) {
                            result.add(currTrip);
                        }
                    }
                }

                displayTrips(result, tripsLayout);
            }

                @Override
                public void onFailure(Call<List<Trip>> call, Throwable t) {
                    Log.d(TAG, "Retrofit failed to get data");
                    Log.d(TAG, t.getMessage());
                    t.printStackTrace();
                    call.cancel();
                }
        });
    }

    public void displayTrips(List<Trip> trips, LinearLayout layout) {
        for (Trip trip: trips) {
            addTripView(layout, trip);
        }
    }

    /**
     * Gets the string from an EditText
     * @param view The view to look in
     * @param id The id of the EditText
     * @return The String contents of the EditText
     */
    protected String getStringFromEditText(View view, int id) {
        EditText editText = view.findViewById(id);
        return editText.getText().toString();
    }

    /**
     * Gets an integer from an EditText
     * @param view The view to look in
     * @param id The id of the EditText
     * @param defaultVal The default value to use in case of an empty EditText
     * @return The int contents of the EditText or the default value
     */
    protected float getFloatFromEditText(View view, int id, float defaultVal) {
        EditText editText = view.findViewById(id);
        float result = editText.getText().toString().isEmpty()
                ? defaultVal : Float.valueOf(editText.getText().toString());
        return result;
    }

    /**
     * Computes the distance between two coordinate points.
     * @param lat1 first point latitude
     * @param lng1 first point longitude
     * @param lat2 second point latitude
     * @param lng2 second point longitude
     * @return The straight line distance in meters between two points.
     */
    protected float computeDistanceBetween(double lat1, double lng1, double lat2, double lng2) {
        float[] distance = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, distance);
        return distance[0];
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     *
     * @param parentLayout The layout to which a trip view is to be added
     * @param trip         The trip details to be added
     */
    public void addTripView(LinearLayout parentLayout, Trip trip) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container,
                parentLayout, false);

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
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        return getLocationName(latitude, longitude, geocoder);
    }

    protected String getLocationName(double latitude, double longitude, Geocoder geocoder) {
        /* testAddTripView was failing on account of this code, specifically:
         * Method getFromLocation in android.location.Geocoder not mocked.
         * Geocoder functionality needs to be extracted to a separate function which
         * receives a geocoder argument that can be mocked and passed in for testing.
        */
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
            Log.e(TAG, "Lat/Long Error: "
                    + "Latitude = " + latitude
                    + ", Longitude = "
                    + longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            Log.e(TAG, "No address found");
        } else {
            Address address = addresses.get(0);
            StringBuilder stringBuilder = new StringBuilder(address.getAddressLine(0));

            // Fetch the address lines using getAddressLine
            for (int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                stringBuilder.append(", ").append(address.getAddressLine(i));
            }
            Log.i(TAG, "Address found");
            return stringBuilder.toString();
        }

        // In the case of failure, return location coordinate string
        return latitude + ", " + longitude;
    }
}
