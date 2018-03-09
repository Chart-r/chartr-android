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

import com.example.mac.chartr.R;


public class PostTripActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

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

        /**
         * Sample http request with volley
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://99sepehum8.execute-api.us-east-2.amazonaws.com/prod/user/*?email=joe.smitty@gmail.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "Response is: "+ response.substring(0,response.length()));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

         **/
    }
    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() != 0){
            super.onBackPressed();
        }
    }
}
