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

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.adapters.RequestAdapter;
import com.example.mac.chartr.objects.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsFragment extends Fragment {
    public static final String TAG = RequestsFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;
    private List<Trip> allTrips;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Pair<Trip, String>> requestedUsers = new ArrayList<>();
    private Comparator<Trip> comparator = (a, b) -> (int) (b.getStartTime() - a.getStartTime());

    public RequestsFragment() {
        // Required empty public constructor
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
        uid = getLoggedInUid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "start onCreate()");
        super.onCreate(savedInstanceState);
        uid = getLoggedInUid();
        Log.d(TAG, "end onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "start onCreateView()");
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_requests, container, false);
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

    private void getRequestedUsers(){
        Log.d(TAG, "start getRequestedUsers()");
        ApiInterface apiInterface = ApiClient.getApiInstance();

        Log.d(TAG, "call apiInterface.getAllUserTrips()");
        Call<List<Trip>> call = apiInterface.getAllUserTrips(uid);
        Log.d(TAG, "finished apiInterface.getAllUserTrips()");

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                // Sort trips from most earliest to latest
                allTrips = response.body();
                Collections.sort(allTrips, comparator);
                Log.d(TAG, "allTrips: " + allTrips.toString());
                filterRequestedUsers(allTrips);
                Log.d(TAG, "requestedUsers: " + requestedUsers.toString());
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

        Log.d(TAG, "call filterRequestedUsers()");
        Log.d(TAG, "finished filterRequestedUsers()");

        Log.d(TAG, "end getRequestedUsers()");
    }

    private void filterRequestedUsers(List<Trip> allTrips){
        Log.d(TAG, "start filterRequestedUsers()");
        Log.d(TAG, "allTrips: " + allTrips);
        requestedUsers.clear();
        for (Trip trip : allTrips){
            Log.d(TAG, "trip: " + trip);
            Map<String, String> allUsers = trip.getUsers();
            for (Map.Entry<String, String> user : allUsers.entrySet()){
                Log.d(TAG, "user: " + user);
                Log.d(TAG, "user.getValue(): " + user.getValue());
                if(user.getValue().equals("pending")) {
                    Log.d(TAG, "user is pending");
                    Pair<Trip, String> requestedUser = new Pair<>(trip, user.getKey());
                    requestedUsers.add(requestedUser);
                }
            }
        }
        Log.d(TAG, "end filterRequestedUsers()");
    }

    /**
     * Gets the logged in user from the provider
     * @return logged in user
     */
    private String getLoggedInUid() {
        return provider.getAppHelper().getLoggedInUser().getUid();
    }
}
