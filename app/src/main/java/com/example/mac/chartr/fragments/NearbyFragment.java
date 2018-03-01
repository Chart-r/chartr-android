package com.example.mac.chartr.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.chartr.R;

public class NearbyFragment extends Fragment {
    public static final String TAG = NearbyFragment.class.getSimpleName();


    public NearbyFragment() {
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Nearby");
        ((AppCompatActivity) getActivity()).findViewById(R.id.buttonAddTrip).setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_nearby, container, false);
    }

}
