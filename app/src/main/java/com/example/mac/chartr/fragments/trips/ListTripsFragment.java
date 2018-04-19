package com.example.mac.chartr.fragments.trips;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.TripAdapter;
import com.example.mac.chartr.objects.Trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTripsFragment extends Fragment {
    public static final String TRIP_TYPE_KEY = "TripTypeKey";
    private static final String TAG = ListTripsFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Trip> tripsData = new ArrayList<>();
    private Comparator<Trip> comparator = (a, b) -> (int) (b.getStartTime() - a.getStartTime());

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
        uid = getLoggedInUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_trips, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewTrips);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TripAdapter(tripsData);
        recyclerView.setAdapter(adapter);
        if (getArguments() != null) {
            switch (getArguments().getString(ListTripsFragment.TRIP_TYPE_KEY)) {
                case "Confirmed":
                    getFilteredTrips("riding");
                    break;
                case "Pending":
                    getFilteredTrips("pending");
                    break;
                case "Posted":
                    getFilteredTrips("driving");
                    break;
            }
        }

        return root;
    }

    private void getFilteredTrips(String status) {
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getUserTripsForStatus(uid, status);
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                // Sort trips from most earliest to latest
                tripsData = response.body();
                Collections.sort(tripsData, comparator);
                Log.d(TAG, tripsData.toString());
                adapter = new TripAdapter(tripsData);
                recyclerView.setAdapter(adapter);
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

    protected String getLocationName(double latitude, double longitude) {
        /* testAddTripView was failing on account of this code, specifically:
         * Method getFromLocation in android.location.Geocoder not mocked.
         * Geocoder functionality needs to be extracted to a separate function which
         * receives a geocoder argument that can be mocked and passed in for testing.
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

    /**
     * Gets the logged in user from the provider
     * @return logged in user
     */
    private String getLoggedInUid() {
        return provider.getAppHelper().getLoggedInUser().getUid();
    }
}
