package com.example.mac.chartr.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.mac.chartr.R;

import java.sql.Time;
import java.util.Date;

import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.TripInvoker;
import com.example.mac.chartr.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONObject;


public class PostTripActivity extends AppCompatActivity {
    private static final String TAG = PostTripActivity.class.getSimpleName();

    private EditText inStartLocation;
    private EditText inEndLocation;
    private EditText inDepartureDate;
    private EditText inReturnDate;
    private EditText inDepartureTime;
    private EditText inReturnTime;
    private Switch inCanPickUp;
    private TextView inNumSeats;
    private Switch inAllowSmoking;
    private Switch inIsQuiet;

    private String startLocation;
    private String endLocation;
    private Date departureDate;
    private Date returnDate;
    private Time departureTime;
    private Time returnTime;
    private boolean canPickUp;
    private int numSeats;
    private boolean allowSmoking;
    private boolean isQuiet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_trip);
    }

    public void incrementSeats(View view){
        inNumSeats = (TextView) findViewById(R.id.seatValue);
        numSeats = Integer.parseInt(inNumSeats.getText().toString());
        if(numSeats < 4) {
            inNumSeats.setText(String.valueOf(numSeats + 1));
        }
    }

    public void decrementSeats(View view){
        inNumSeats = (TextView) findViewById(R.id.seatValue);
        numSeats = Integer.parseInt(inNumSeats.getText().toString());
        if(numSeats > 1) {
            inNumSeats.setText(String.valueOf(numSeats - 1));
        }
    }

    public void postTrip(View view) {
        // TODO: implement
    }
}
