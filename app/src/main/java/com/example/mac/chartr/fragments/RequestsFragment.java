package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.RequestAdapter;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment will display all the riders who have requested to join different trips, and allow
 * the driver to go through and reject or accept each rider.
 */
public class RequestsFragment extends Fragment {
    private static final String TAG = RequestsFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private final List<Pair<Trip, User>> requestedUsers = new ArrayList<>();
    private final Comparator<Pair<Trip, User>> comparator =
            (a, b) -> (int) (b.first.getStartTime() - a.first.getStartTime());

    /**
     * Constructor of the class
     */
    public RequestsFragment() {
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    /**
     * Initialises the common dependency provider of the class
     * @param provider The provider to which the class' provider is set
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
        Log.d(TAG, "start onCreate()");
        super.onCreate(savedInstanceState);
        uid = getLoggedInUid();
        Log.d(TAG, "end onCreate()");
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
        Log.d(TAG, "start onCreateView()");
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_requests, container, false);
        noRequestsTextView = root.findViewById(R.id.textViewNoRequests);
        recyclerView = root.findViewById(R.id.recyclerViewRequests);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RequestAdapter(requestedUsers);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "call getRequestedUsers()");
        getRequestedUsers();
        Log.d(TAG, "finished getRequestedUsers()");

        Log.d(TAG, "end onCreateView()");
        return root;
    }

    private void getRequestedUsers() {
        Log.d(TAG, "start getRequestedUsers()");

        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<List<Trip>> call = apiInterface.getUserTripsForStatus(uid, "driving");

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                List<Trip> allTrips = response.body();
                List<Pair<Trip, String>> requestedUsersUids = filterRequestedUsers(allTrips);

                getUsersFromUids(requestedUsersUids);

                adapter = new RequestAdapter(requestedUsers);
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

        Log.d(TAG, "end getRequestedUsers()");
    }

    private List<Pair<Trip, String>> filterRequestedUsers(List<Trip> allTrips) {
        Log.d(TAG, "start filterRequestedUsers()");

        List<Pair<Trip, String>> filteredRequestedUsers = new ArrayList<>();
        for (Trip trip : allTrips) {
            Map<String, String> allUsers = trip.getUsers();

            for (Map.Entry<String, String> user : allUsers.entrySet()) {
                if (user.getValue().equals("pending")) {
                    Pair<Trip, String> requestedUser = new Pair<>(trip, user.getKey());
                    filteredRequestedUsers.add(requestedUser);
                }
            }

        }

        if (filteredRequestedUsers.isEmpty()) {
            noRequestsTextView.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "end filterRequestedUsers()");
        return filteredRequestedUsers;
    }

    private void getUsersFromUids(List<Pair<Trip, String>> requestedUsersUids) {
        Log.d(TAG, "start getNamesFromUids()");

        requestedUsers.clear();
        for (Pair<Trip, String> requestedUserPair : requestedUsersUids) {
            ApiInterface apiInterface = ApiClient.getApiInstance();
            Call<User> call = apiInterface.getUserFromUid(requestedUserPair.second);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d(TAG, response.code() + "");

                    User user = response.body();
                    Pair<Trip, User> tripUserPair =
                        new Pair<>(requestedUserPair.first, user);

                    requestedUsers.add(tripUserPair);

                    Collections.sort(requestedUsers, comparator);

                    adapter = new RequestAdapter(requestedUsers);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "Retrofit failed to get data");
                    Log.d(TAG, t.getMessage());
                    t.printStackTrace();
                    call.cancel();
                }
            });
        }

        Log.d(TAG, "end getNamesFromUids()");
    }

    /**
     * Gets the logged in user from the provider
     * @return logged in user
     */
    private String getLoggedInUid() {
        return provider.getAppHelper().getLoggedInUser().getUid();
    }
}
