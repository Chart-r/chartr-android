package com.example.mac.chartr.fragments.trips;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.example.mac.chartr.objects.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTripsFragment extends Fragment {
    public static final String TRIP_TYPE_KEY = "TripTypeKey";
    private static final String TAG = ListTripsFragment.class.getSimpleName();
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

            switch (getArguments().getString(ListTripsFragment.TRIP_TYPE_KEY)) {
                case "Posted":
                    inflatePostedTripsInLayout(tripsLinearLayout);
                    break;
                case "Joined":
                    inflateJoinedTripsInLayout(tripsLinearLayout);
                    break;
                default:
                    addSectionTitle(tripsLinearLayout, "Default action.");
                    break;
            }
        }

        return root;
    }

    private void inflatePostedTripsInLayout(final LinearLayout tripsLinearLayout) {
        // Gets all trips for logged in user
        CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
        String userEmail = commonDependencyProvider.getAppHelper().getLoggedInUser().getEmail();
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getUserTripsForStatus(userEmail, "driving");
        Log.d(TAG, "Logged in user: " + userEmail);
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                // Sort trips from most earliest to latest
                ArrayList<Trip> resource = (ArrayList<Trip>) response.body();
                Collections.sort(resource, new Comparator<Trip>() {
                    @Override
                    public int compare(Trip a, Trip b) {
                        return (int) (b.getStartTime() - a.getStartTime());
                    }
                });

                // Get index of first trip that has not ended
                int activeIndex = getActiveIndex(resource);

                for (int i = activeIndex; i < resource.size(); i++) {
                    addTripView(tripsLinearLayout, resource.get(i));
                }
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

    protected void inflateJoinedTripsInLayout(final LinearLayout tripsLinearLayout) {
        CommonDependencyProvider commonDependencyProvider = new CommonDependencyProvider();
        String userEmail = commonDependencyProvider.getAppHelper().getLoggedInUser().getEmail();
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getUserTripsForStatus(userEmail, "pending");
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                // Sort trips from most earliest to latest
                ArrayList<Trip> resource = (ArrayList<Trip>) response.body();
                Collections.sort(resource, new Comparator<Trip>() {
                    @Override
                    public int compare(Trip a, Trip b) {
                        return (int) (b.getStartTime() - a.getStartTime());
                    }
                });

                // Get index of first trip that has not ended
                int activeIndex = getActiveIndex(resource);

                addSectionTitle(tripsLinearLayout, "Pending");
                Log.d(TAG, "Got " + resource.size() + " pending trips.");
                for (int i = activeIndex; i < resource.size(); i++) {
                    addTripView(tripsLinearLayout, resource.get(i));
                }
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

    /**
     * Finds the index of the first trip that has a start time after the current time
     * @param trips A list of trips sorted in ascending order by start time
     * @return The index of the first trip that starts after current time, or trips.size()
     */
    protected int getActiveIndex(List<Trip> trips) {
        int index = 0;
        GregorianCalendar currentTime = new GregorianCalendar();
        for (; index < trips.size(); index++) {
            if (trips.get(index).getEndTime() > currentTime.getTimeInMillis()) {
                break;
            }
        }
        return index;
    }

    /**
     * Adds a TextView section title to the specified linear layout.
     * @param parentLayout The linear layout containing the trip cards
     * @param title The section title
     */
    protected  void addSectionTitle(LinearLayout parentLayout, String title) {
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(22);
        textView.setPadding(0, 15, 0, 0);
        textView.setGravity(Gravity.CENTER);
        parentLayout.addView(textView);
    }

    /**
     * Adds an individual trip view to the linear layout passed in.
     *
     * @param parentLayout The layout to which a trip view is to be added
     * @param trip         The trip details to be added
     */
    protected void addTripView(LinearLayout parentLayout, Trip trip) {
        //create a view to inflate the layout_item (the xml with the textView created before)
        View tripContainer = getLayoutInflater().inflate(R.layout.layout_trip_container,
                parentLayout, false);

        // Set TextViews with appropriate data
        String name = trip.getDriverFromUsers();
        String rating = "rating";
        String seats = (trip.getUsers().size() - 1) + "/" + trip.getSeats();
        String start = getLocationName(trip.getStartLat(), trip.getStartLong());
        String destination = getLocationName(trip.getEndLat(), trip.getEndLong());

        if (name.equals(getLoggedInUser().getEmail())) {
            name = getLoggedInUser().getName();
        } else {
            Log.d(TAG, "Username " + name
                    + " does not match logged in user: " + getLoggedInUser().getName());
        }

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
    private User getLoggedInUser() {
        return provider.getAppHelper().getLoggedInUser();
    }
}
