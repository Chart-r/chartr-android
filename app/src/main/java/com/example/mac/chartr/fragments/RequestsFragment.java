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
<<<<<<< HEAD
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

public class RequestsFragment extends Fragment {
    public static final String TAG = RequestsFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Pair<Trip, String>> requestedUsers = new ArrayList<>();
    private Comparator<Trip> comparator = (a, b) -> (int) (b.getStartTime() - a.getStartTime());
=======
import com.example.mac.chartr.objects.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestsFragment extends Fragment {
    public static final String TAG = RequestsFragment.class.getSimpleName();
    private List<Trip> tripData = new ArrayList<Trip>();

>>>>>>> d85cbd1dfe1913d5d534f95a960d11d749ba330c

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
<<<<<<< HEAD
        uid = getLoggedInUid();
        Log.d(TAG, "end onCreate()");
=======

>>>>>>> d85cbd1dfe1913d5d534f95a960d11d749ba330c
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
        Call<List<Trip>> call = apiInterface.getAllUserTrips(uid);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Log.d(TAG, response.code() + "");

                // Sort trips from most earliest to latest
                List<Trip> allTrips = response.body();
                List<Pair<Trip, String>> requestedUsersUids = filterRequestedUsers(allTrips);

                getNamesFromUids(requestedUsersUids);

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

<<<<<<< HEAD
    private List<Pair<Trip, String>> filterRequestedUsers(List<Trip> allTrips){
        Log.d(TAG, "start filterRequestedUsers()");

        List<Pair<Trip, String>> filteredRequestedUsers = new ArrayList<>();
        for (Trip trip : allTrips){
            Map<String, String> allUsers = trip.getUsers();

            for (Map.Entry<String, String> user : allUsers.entrySet()){
                if(user.getValue().equals("pending")) {
                    Pair<Trip, String> requestedUser = new Pair<>(trip, user.getKey());
                    filteredRequestedUsers.add(requestedUser);
                }
            }

        }

        Log.d(TAG, "end filterRequestedUsers()");
        return filteredRequestedUsers;
    }

    private void getNamesFromUids(List<Pair<Trip, String>> requestedUsersUids){
        Log.d(TAG, "start getNamesFromUids()");

        requestedUsers.clear();
        for (Pair<Trip, String> requestedUserPair : requestedUsersUids){
            ApiInterface apiInterface = ApiClient.getApiInstance();
            Call<User> call = apiInterface.getUserFromUid(requestedUserPair.second);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d(TAG, response.code() + "");

                    // Sort trips from most earliest to latest
                    User user = response.body();
                    Pair<Trip, String> requestedUserNamePair =
                        new Pair(requestedUserPair.first, user.getName());

                    requestedUsers.add(requestedUserNamePair);

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
=======
    public List<Trip> mockAPI() {
        List<Trip> dummyData = new ArrayList<Trip>();
        HashMap<String, String> users = new HashMap<>();
        users.put("User 1", "driving");
        users.put("User 2", "riding");
        users.put("User 3", "pending");
        users.put("User 4", "pending");
        dummyData.add(new Trip(0, 0, false, false, 1.,1.,
                2.,2.,5, 5.0, "test", users));
        return dummyData;
    }

>>>>>>> d85cbd1dfe1913d5d534f95a960d11d749ba330c
}
