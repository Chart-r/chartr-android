package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

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

        View root = inflater.inflate(R.layout.fragment_nearby, container, false);
        return root;
    }

}
