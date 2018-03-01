package com.example.mac.chartr.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mac.chartr.R;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Date;

public class PostTripActivity extends AppCompatActivity {

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
            inNumSeats.setText(numSeats + 1);
        }
    }

    public void decrementSeats(View view){
        inNumSeats = (TextView) findViewById(R.id.seatValue);
        numSeats = Integer.parseInt(inNumSeats.getText().toString());
        if(numSeats > 2) {
            inNumSeats.setText(numSeats - 1);
        }
    }

    public void postTrip(View view) {

    }
}
