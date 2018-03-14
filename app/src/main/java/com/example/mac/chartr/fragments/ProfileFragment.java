package com.example.mac.chartr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.R;
import com.example.mac.chartr.activities.LoginActivity;
import com.example.mac.chartr.activities.MainActivity;
import com.example.mac.chartr.activities.PostTripActivity;
import com.example.mac.chartr.activities.RegisterActivity;

public class ProfileFragment extends Fragment {
    public static final String TAG = ProfileFragment.class.getSimpleName();

    public ProfileFragment() {
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
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button logoutButton = (Button) root.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Activity parent = getActivity();

                if (parent instanceof MainActivity) {
                    ((MainActivity) parent).signOut();
                }
=======
                logout(v);
>>>>>>> skeleton
            }
        });

        return root;
    }

    public void logout(View view){
        Intent intent = new Intent(this.getActivity().getApplicationContext(), LoginActivity.class);
        ((MainActivity) getActivity()).signOut();
        startActivity(intent); }

}
