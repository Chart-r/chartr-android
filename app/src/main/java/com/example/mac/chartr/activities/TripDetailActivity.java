package com.example.mac.chartr.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity will give detailed information about a trip that is clicked on. This activity
 * can be launched by any context where trip cards are displayed.
 */
public class TripDetailActivity extends AppCompatActivity {
    public static final String TAG = TripDetailActivity.class.getSimpleName();

    private Trip trip;
    private String type;

    EditText departureEditText;
    EditText departureTimeText;
    EditText startLocationEditText;
    EditText endLocationEditText;
    TextView numSeatsEditText;
    Switch smokingSwitch;
    Button submitButton;
    String uid;

    /**
     * Inherited class from the Activity class that handles what happens
     * when the activity starts up
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        trip = (Trip) getIntent().getSerializableExtra("trip");
        //type = getIntent().getStringExtra("type");
        try {
            uid =  new CommonDependencyProvider().getAppHelper().getLoggedInUser().getUid();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            uid = "-1";
        }
        Log.d(TAG, uid);
        if (trip.containsUser(uid)) {
            type = "mytrips";
        } else {
            type = "requests";
        }
        initViews();

    }

    private void initViews() {
        departureEditText = findViewById(R.id.editTextDepartureDate);
        departureEditText.setInputType(InputType.TYPE_NULL);
        departureEditText.setFocusable(false);
        departureEditText.setText(trip.getStartDateString());

        departureTimeText = findViewById(R.id.editTextDepartureTime);
        departureTimeText.setInputType(InputType.TYPE_NULL);
        departureTimeText.setText(trip.getStartTimeString());

        startLocationEditText = findViewById(R.id.editTextStartLocation);
        startLocationEditText.setInputType(InputType.TYPE_NULL);
        startLocationEditText.setText(trip.getStartLocationLongName(this));

        endLocationEditText = findViewById(R.id.editTextEndLocation);
        endLocationEditText.setInputType(InputType.TYPE_NULL);
        endLocationEditText.setText(trip.getEndLocationLongName(this));

        numSeatsEditText = findViewById(R.id.textViewSeatValue);
        numSeatsEditText.setText(String.format("%d / %d", trip.getRidingCount(), trip.getSeats()));

        smokingSwitch = findViewById(R.id.switchNoSmoking);
        smokingSwitch.setChecked(trip.getSmoking());

        submitButton = findViewById(R.id.button);
        if (type.equals("mytrips")) {
            submitButton.setText(R.string.back);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            submitButton.setText(R.string.request);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestTrip(v.getContext(), trip);
                }
            });
        }
    }

    private void requestTrip(Context context, Trip trip) {
        ApiInterface apiInterface = ApiClient.getApiInstance();
        //String uid =  new CommonDependencyProvider().getAppHelper().getCurrUser();
        String tid = trip.getId();
        String status = "pending";
        Call<String> call = apiInterface.updateTrip(uid, tid, status);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                CharSequence text = "Requested";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                CharSequence text = "Request failed";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                call.cancel();
            }
        });
    }
}
