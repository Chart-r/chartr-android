package com.example.mac.chartr.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.fragments.trips.ListTripsFragment;
import com.example.mac.chartr.objects.Trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                searchTrips(view.findViewById(R.id.search_relative_layout));
            }
        });

        return view;
    }

    public void searchTrips(final View view) {
        CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
        view.findViewById(R.id.search_relative_layout).setVisibility(View.GONE);

        String startLocation =
                getStringFromEditText(view, R.id.searchFragmentEditTextStartLocation);
        String endLocation = getStringFromEditText(view, R.id.searchFragmentEditTextEndLocation);
        final String preferredDriverEmail =
                getStringFromEditText(view, R.id.searchFragmentEditPreferredDriver);
        final int priceRangeFrom =
                getIntegerFromEditText(view, R.id.searchFragmentEditPriceRangeFrom, 0);
        final int priceRangeTo = getIntegerFromEditText(
                view, R.id.searchFragmentEditPriceRangeTo, Integer.MAX_VALUE);

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
        Call<List<Trip>> call = apiInterface.getAllTrips();
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                List<Trip> tripList = response.body();
                List<Trip> result = new ArrayList<Trip>();
                for (int i = 0; i < tripList.size(); i++) {

                    Trip currTrip = tripList.get(i);
                    boolean startLocWithinRange = startLat - 1 < currTrip.getStartLat()
                            && currTrip.getStartLat() < startLat + 1
                            && startLng - 1 < currTrip.getStartLat()
                            && currTrip.getStartLat() < startLng + 1;

                    boolean endLocWithinRange = endLat - 1 < currTrip.getStartLat()
                            && currTrip.getStartLat() < endLat + 1
                            && endLng - 1 < currTrip.getStartLat()
                            && currTrip.getStartLat() < endLng + 1;

                    boolean costOfTripWithinRange = priceRangeFrom < currTrip.getPrice()
                            && currTrip.getPrice() < priceRangeTo;

                    boolean hasDriverPreference = !preferredDriverEmail.isEmpty();

                    if (startLocWithinRange && endLocWithinRange && costOfTripWithinRange) {
                        if (!hasDriverPreference
                                || (hasDriverPreference
                                    && currTrip.getDriverFromUsers() == preferredDriverEmail)) {
                            result.add(currTrip);
                        }
                    }
                }
                LinearLayout tripsLayout = view.findViewById(R.id.searchTripsLinearLayout);

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

        ListTripsFragment helper = new ListTripsFragment();

        for (Trip trip: trips) {
            helper.addTripView(layout, trip);
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
    protected int getIntegerFromEditText(View view, int id, int defaultVal) {
        EditText editText = view.findViewById(id);
        int result = editText.getText().toString().isEmpty()
                ? defaultVal : Integer.valueOf(editText.getText().toString());
        return result;
    }
}
