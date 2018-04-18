package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestsFragment extends Fragment {
    public static final String TAG = RequestsFragment.class.getSimpleName();
    private List<Trip> tripData = new ArrayList<Trip>();


    public RequestsFragment() {
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
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

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

}
