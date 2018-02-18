package com.example.mac.chartr.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.mac.chartr.R;
import com.example.mac.chartr.fragments.NearbyFragment;
import com.example.mac.chartr.fragments.ProfileFragment;
import com.example.mac.chartr.fragments.RequestsFragment;
import com.example.mac.chartr.fragments.trips.TripsFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.content) != null) {
            if (savedInstanceState != null) {
                return;
            }
            TripsFragment initialFragment = new TripsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, initialFragment).commit();
        }


        toolbar = (Toolbar) findViewById(R.id.topToolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        Log.d(TAG, "Setting up Bottom Navigation");
        final BottomNavigationViewEx navBar = findViewById(R.id.bottomNavigationBar);
        navBar.enableAnimation(false);
        navBar.enableShiftingMode(false);
        navBar.enableItemShiftingMode(false);
        navBar.setTextVisibility(true);
        navBar.setSelectedItemId(R.id.ic_trips);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        switch (itemId) {
                            case R.id.ic_nearby:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content, new NearbyFragment())
                                        .addToBackStack("Nearby").commit();
                                break;
                            case R.id.ic_trips:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content, new TripsFragment())
                                        .addToBackStack("Trips").commit();
                                break;
                            case R.id.ic_requests:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content, new RequestsFragment())
                                        .addToBackStack("Requests").commit();
                                break;
                            case R.id.ic_profile:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content, new ProfileFragment())
                                        .addToBackStack("Profile").commit();
                                break;
                        }
                        return true;
                    }
                });
    }
}
