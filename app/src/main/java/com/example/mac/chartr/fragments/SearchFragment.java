package com.example.mac.chartr.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.TripAdapter;
import com.example.mac.chartr.objects.Trip;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
    EditText startLocationEditText;
    Double startLocationLat;
    Double startLocationLng;
    EditText endLocationEditText;
    Double endLocationLat;
    Double endLocationLng;
    EditText departureDateEditText;
    DatePickerDialog.OnDateSetListener departureDate;
    Calendar departureCalendar = Calendar.getInstance();
    EditText preferredDriverEditText;
    String preferredDriver;
    EditText priceMinEditText;
    float priceMin;
    EditText priceMaxEditText;
    float priceMax;
    Button submitSearchButton;
    TextView textViewNoTrips;
    // Search radius in meters
    float searchRadius;
    private CommonDependencyProvider provider;
    private String uid;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Trip> tripsData = new ArrayList<>();
    private RelativeLayout searchLayout;
    private Button filterButton;
    private FusedLocationProviderClient mFusedLocationClient;


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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        Log.d("TAG", "Displaying all nearby trips");
        displayAllTrips();
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
        initDatePicker(root);
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

        textViewNoTrips = root.findViewById(R.id.textViewSearchNoTrips);
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
                textViewNoTrips.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Initialize the departure date picker based off:
     * https://stackoverflow.com/questions/14933330/
     * datepicker-how-to-popup-datepicker-when-click-on-edittext
     * <p>
     * https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
     *
     * @param root the root view.
     */
    private void initDatePicker(View root) {
        departureDateEditText = root.findViewById(R.id.searchFragmentEditTextDate);
        departureDateEditText.setFocusable(false);
        departureDateEditText.setClickable(true);
        departureDateEditText.setLongClickable(false);
        departureDate = (view, year, monthOfYear, dayOfMonth) -> {
            departureCalendar.set(Calendar.YEAR, year);
            departureCalendar.set(Calendar.MONTH, monthOfYear);
            departureCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDepartureDateLabel();
        };
        departureDateEditText.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(), departureDate, departureCalendar
                    .get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH),
                    departureCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateDepartureDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        departureDateEditText.setText(sdf.format(departureCalendar.getTime()));
    }

    public void searchTrips() {
        searchLayout.setVisibility(View.GONE);
        filterButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        textViewNoTrips.setVisibility(View.GONE);

        String startLocation = startLocationEditText.getText().toString();
        String endLocation = endLocationEditText.getText().toString();
        preferredDriver = preferredDriverEditText.getText().toString();
        priceMin = priceMinEditText.getText().toString().isEmpty()
                ? 0.0f : Float.valueOf(priceMinEditText.getText().toString());
        priceMax = priceMinEditText.getText().toString().isEmpty()
                ? Float.MAX_VALUE : Float.valueOf(priceMinEditText.getText().toString());
        searchRadius = 5000f;

        String departureDateString = departureDateEditText.getText().toString();
        Date departureDate;
        if (!departureDateString.isEmpty()) {
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                departureDate = dfDate.parse(departureDateString);
            } catch (ParseException error) {
                Log.e(TAG, "Error Parsing date/time.");
                Toast.makeText(getContext(), "Departure Date invalid.", Toast.LENGTH_SHORT);
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
                Log.d(TAG, "Getting trips in search.");
                Log.d(TAG, response.code() + "");

                List<Trip> tripList = response.body();
                List<Trip> result = new ArrayList<Trip>();
                for (int i = 0; i < tripList.size(); i++) {
                    Trip currTrip = tripList.get(i);

                    boolean costOfTripWithinRange = priceMin < currTrip.getPrice()
                            && currTrip.getPrice() < priceMax;
                    boolean hasDriverPreference = !preferredDriver.isEmpty();
                    boolean isNotInTrip = !currTrip.containsUser(uid);
                    boolean isNotFull = !currTrip.isFull();
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

                    if (isNotInTrip && isNotFull && costOfTripWithinRange
                            && endDistance < searchRadius && startDistance < searchRadius) {
                        if (!hasDriverPreference
                                || currTrip.getDriverFromUsers().contains(preferredDriver)) {
                            if (!hasDatePreference || dateAfter) {
                                result.add(currTrip);
                            }
                        }
                    }
                }
                if (result.isEmpty()) {
                    textViewNoTrips.setVisibility(View.VISIBLE);
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
    }

    private void displayAllTrips() {

        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getAllCurrentTrips();
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call,
                                   Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                List<Trip> tripList = response.body();
                List<Trip> result = new ArrayList<Trip>();

                for (int i = 0; i < tripList.size(); i++) {
                    Trip currTrip = tripList.get(i);
                    boolean isNotInTrip = !currTrip.containsUser(uid);
                    boolean isNotFull = !currTrip.isFull();

                    if (isNotInTrip && isNotFull) {
                        result.add(currTrip);
                    }
                }
                if (result.isEmpty()) {
                    textViewNoTrips.setVisibility(View.VISIBLE);
                }
                adapter.addItems(result);
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });
    }
    private void displayNearbyTrips() {
        try {
            Log.d(TAG, "Trying to get the last location");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) getContext(), location -> {
                        Log.d(TAG, "Successfully retrieved current location");
                        if (location == null) {
                            searchTrips();
                            return;
                        }
                        double currLat = location.getLatitude();
                        double currLong = location.getLongitude();

                        ApiInterface apiInterface = ApiClient.getApiInstance();
                        Call<List<Trip>> call = apiInterface.getAllCurrentTrips();
                        call.enqueue(new Callback<List<Trip>>() {
                            @Override
                            public void onResponse(Call<List<Trip>> call,
                                                   Response<List<Trip>> response) {
                                Log.d(TAG, response.code() + "");

                                List<Trip> tripList = response.body();
                                List<Trip> result = new ArrayList<Trip>();

                                for (int i = 0; i < tripList.size(); i++) {
                                    Trip currTrip = tripList.get(i);

                                    boolean withinDistance =
                                            computeDistanceBetween(currLat, currLong,
                                                    currTrip.getStartLat(),
                                                    currTrip.getStartLong()) < 5000f;
                                    boolean isNotInTrip = !currTrip.containsUser(uid);
                                    boolean isNotFull = !currTrip.isFull();

                                    if (withinDistance && isNotInTrip && isNotFull) {
                                        result.add(currTrip);
                                    }
                                }
                                if (result.isEmpty()) {
                                    textViewNoTrips.setVisibility(View.VISIBLE);
                                }
                                adapter.addItems(result);
                            }

                            @Override
                            public void onFailure(Call<List<Trip>> call, Throwable t) {
                                Log.d(TAG, t.getMessage());
                                t.printStackTrace();
                                call.cancel();
                            }
                        });


                    });

        } catch (SecurityException e) {
            Log.d("TAG", "Permission denied; all trips displayed");
            searchTrips();
        }


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

    @Override
    public void onResume() {
        super.onResume();
        if (searchLayout.getVisibility() == View.GONE) {
            displayNearbyTrips();
        }
    }

    /**
     * Computes the distance between two coordinate points.
     *
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
