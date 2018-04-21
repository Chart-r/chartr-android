package com.example.mac.chartr.fragments;


import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.LocationNameProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.TripAdapter;
import com.example.mac.chartr.objects.Trip;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SearchFragment.class.getSimpleName();
    private static final int START_PLACE_PICKER = 1;
    private static final int DEST_PLACE_PICKER = 2;

    private CommonDependencyProvider provider;
    private String uid;

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Trip> tripsData = new ArrayList<>();

    private RelativeLayout searchLayout;
    private Button filterButton;

    EditText startLocationEditText;
    Double startLocationLat;
    Double startLocationLng;

    EditText endLocationEditText;
    Double endLocationLat;
    Double endLocationLng;

    EditText departureDateEditText;

    EditText preferredDriverEditText;
    String preferredDriver;

    EditText priceMinEditText;
    float priceMin;

    EditText priceMaxEditText;
    float priceMax;

    Button submitSearchButton;


    public SearchFragment() {
        // Required empty public constructor
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = provider.getAppHelper().getLoggedInUser().getUid();

        // TODO: Call api and filter for nearby trips
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(root);
        recyclerView = root.findViewById(R.id.recyclerViewSearch);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TripAdapter(tripsData);
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void initViews(View root) {
        departureDateEditText = root.findViewById(R.id.searchFragmentEditTextDate);
        // TODO: Use a date picker
        preferredDriverEditText = root.findViewById(R.id.searchFragmentEditTextPreferredDriver);
        // TODO: Filter by driver
        priceMinEditText = root.findViewById(R.id.searchFragmentEditPriceRangeFrom);
        priceMaxEditText = root.findViewById(R.id.searchFragmentEditPriceRangeTo);
        startLocationEditText = root.findViewById(R.id.searchFragmentEditTextStartLocation);
        startLocationEditText.setFocusable(false);
        startLocationEditText.setClickable(true);
        startLocationEditText.setOnClickListener(this);
        endLocationEditText = root.findViewById(R.id.searchFragmentEditTextEndLocation);
        endLocationEditText.setFocusable(false);
        endLocationEditText.setClickable(true);
        endLocationEditText.setOnClickListener(this);

        filterButton = root.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(this);
        searchLayout = root.findViewById(R.id.relativeLayoutSearchParameters);
        submitSearchButton = root.findViewById(R.id.buttonSubmitSearch);
        submitSearchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmitSearch:
                searchTrips();
                break;
            case R.id.searchFragmentEditTextEndLocation:
                onPickButtonClick(DEST_PLACE_PICKER);
                break;
            case R.id.searchFragmentEditTextStartLocation:
                onPickButtonClick(START_PLACE_PICKER);
                break;
            case R.id.filterButton:
                searchLayout.setVisibility(View.VISIBLE);
                filterButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                break;
        }
    }

    public void searchTrips() {
        searchLayout.setVisibility(View.GONE);
        filterButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        String startLocation = startLocationEditText.getText().toString();
        String endLocation = endLocationEditText.getText().toString();
        preferredDriver = preferredDriverEditText.getText().toString();
        priceMin = priceMinEditText.getText().toString().isEmpty()
                ? 0.0f : Float.valueOf(priceMinEditText.getText().toString());
        priceMax = priceMinEditText.getText().toString().isEmpty()
                ? Float.MAX_VALUE : Float.valueOf(priceMinEditText.getText().toString());

        String departureDateString = departureDateEditText.getText().toString();
        Date departureDate;
        if (!departureDateString.isEmpty()) {
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                departureDate = dfDate.parse(departureDateString);
            } catch (ParseException error) {
                Log.e(TAG, "Error Parsing date/time.");
                Toast.makeText(getContext(),"Departure Date invalid.", Toast.LENGTH_SHORT);
                return;
            }
        } else {
            departureDate = new Date(0);
        }

        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getAllCurrentTrips();
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                List<Trip> tripList = response.body();
                List<Trip> result = new ArrayList<Trip>();
                for (int i = 0; i < tripList.size(); i++) {
                    Trip currTrip = tripList.get(i);

                    boolean costOfTripWithinRange = priceMin < currTrip.getPrice()
                            && currTrip.getPrice() < priceMax;
                    boolean hasDriverPreference = !preferredDriver.isEmpty();
                    boolean isNotDriver = !uid.equals(currTrip.getDriverFromUsers());
                    boolean hasDatePreference = !(departureDate.getTime() < 1);
                    long currTripDate = currTrip.getStartTime();
                    boolean dateAfter = departureDate.getTime() >= currTripDate;

                    float startDistance = 0.0f;
                    float endDistance = 0.0f;
                    if (!startLocation.isEmpty()) {
                        startDistance = computeDistanceBetween(startLocationLat, startLocationLng,
                                currTrip.getStartLat(), currTrip.getStartLong());
                    }
                    if (!endLocation.isEmpty()) {
                        endDistance = computeDistanceBetween(endLocationLat, endLocationLng,
                                currTrip.getEndLat(), currTrip.getEndLong());
                    }

                    if (isNotDriver && costOfTripWithinRange
                            && endDistance < 5000f && startDistance < 5000f) {
                        if (!hasDriverPreference
                                || currTrip.getDriverFromUsers().contains(preferredDriver)) {
                            if (!hasDatePreference || dateAfter) {
                                result.add(currTrip);
                            }
                        }
                    }
                }
                adapter.addItems(result);
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to get data");
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });

        /* Geocoder code in case we switch back.
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
        */
    }

    public void onPickButtonClick(int key) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getActivity());
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, key);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        if ((requestCode == START_PLACE_PICKER || requestCode == DEST_PLACE_PICKER)
                && resultCode == Activity.RESULT_OK) {
            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(getActivity(), data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            LatLng placeCoordinates = place.getLatLng();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            if (requestCode == START_PLACE_PICKER) {
                startLocationLat = placeCoordinates.latitude;
                startLocationLng = placeCoordinates.longitude;
                if (place.getPlaceTypes().get(0) == 0) {
                    startLocationEditText.setText(address);
                } else {
                    startLocationEditText.setText(name);
                }
            } else {
                endLocationLat = placeCoordinates.latitude;
                endLocationLng = placeCoordinates.longitude;
                if (place.getPlaceTypes().get(0) == 0) {
                    endLocationEditText.setText(address);
                } else {
                    endLocationEditText.setText(name);
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

}
