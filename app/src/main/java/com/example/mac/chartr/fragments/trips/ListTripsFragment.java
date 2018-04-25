package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.TripAdapter;
import com.example.mac.chartr.objects.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTripsFragment extends Fragment {
    public static final String TRIP_TYPE_KEY = "TripTypeKey";
    private static final String TAG = ListTripsFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;

    private TextView textViewNoTrips;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Trip> tripsData = new ArrayList<>();

    /**
     * Empty constructor of the class
     */
    public ListTripsFragment() {
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    /**
     * Sets the common dependency provider
     * @param provider THe provider to which the class's provider is set
     */
    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    /**
     * Method inherited from the Fragment class that is called upon creation of the fragment
     *
     * @param savedInstanceState Bundle of the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getLoggedInUid();
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
        View root = inflater.inflate(R.layout.fragment_list_trips, container, false);
        textViewNoTrips = root.findViewById(R.id.textViewNoTrips);
        recyclerView = root.findViewById(R.id.recyclerViewTrips);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TripAdapter(tripsData);
        recyclerView.setAdapter(adapter);
        if (getArguments() != null) {
            switch (getArguments().getString(ListTripsFragment.TRIP_TYPE_KEY)) {
                case "Confirmed":
                    textViewNoTrips.setText(R.string.no_confirmed_trips);
                    getFilteredTrips("riding");
                    break;
                case "Pending":
                    textViewNoTrips.setText(R.string.no_pending_trips);
                    getFilteredTrips("pending");
                    break;
                case "Posted":
                    textViewNoTrips.setText(R.string.no_posted_trips);
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

                tripsData = response.body();
                Log.d(TAG, tripsData.toString());
                if (tripsData.isEmpty()) {
                    textViewNoTrips.setVisibility(View.VISIBLE);
                }
                adapter.addItems(tripsData);
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
     * Gets the logged in user from the provider
     * @return logged in user
     */
    private String getLoggedInUid() {
        return provider.getAppHelper().getLoggedInUser().getUid();
    }
}
