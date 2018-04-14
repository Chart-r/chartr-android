package com.example.mac.chartr.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.widget.TimePicker;
import android.widget.Toast;


public class PostTripActivity extends AppCompatActivity {
    private static final String TAG = PostTripActivity.class.getSimpleName();

    private TextView inNumSeats;
    private int numSeats;

    private CommonDependencyProvider provider = null;

    Calendar departureCalendar = Calendar.getInstance();
    Calendar returnCalendar = Calendar.getInstance();

    EditText departureEditText;
    DatePickerDialog.OnDateSetListener departureDate;

    EditText returnEditText;
    DatePickerDialog.OnDateSetListener returnDate;

    EditText departureTimeText;
    TimePickerDialog.OnTimeSetListener departTime;

    EditText returnTimeText;
    TimePickerDialog.OnTimeSetListener returnTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_trip);
        initPickers();


    }

    private void initPickers() {
    /*
    SETUP DEPARTURE DATE PICKER
     */
        departureEditText = (EditText) findViewById(R.id.editTextDepartureDate);
        departureDate  = (view, year, monthOfYear, dayOfMonth) -> {
            departureCalendar.set(Calendar.YEAR, year);
            departureCalendar.set(Calendar.MONTH, monthOfYear);
            departureCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDepartureDateLabel();
        };
        departureEditText.setOnClickListener(v -> {
            new DatePickerDialog(PostTripActivity.this, departureDate, departureCalendar
                    .get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH),
                    departureCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        /*
        SETUP RETURN DATE PICKER
         */
        returnEditText = (EditText) findViewById(R.id.editTextReturnDate);
        returnDate  = (view, year, monthOfYear, dayOfMonth) -> {
            returnCalendar.set(Calendar.YEAR, year);
            returnCalendar.set(Calendar.MONTH, monthOfYear);
            returnCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateReturnDateLabel();
        };
        returnEditText.setOnClickListener(v -> {
            new DatePickerDialog(PostTripActivity.this, returnDate, returnCalendar
                    .get(Calendar.YEAR), returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        /*
        SETUP DEPARTURE TIME PICKER
         */
        departureTimeText = (EditText) findViewById(R.id.editTextDepartureTime);
        departTime  = (TimePicker timePicker, int selectedHour, int selectedMinute) -> {
            String modifier = "am";
            String minutes, hours;
            if (selectedHour > 12) {
                selectedHour -= 12;
                modifier = "pm";
            }
            minutes = extractTimeString(selectedMinute);
            hours = extractTimeString(selectedHour);

            departureTimeText.setText("" + hours + ":" + minutes + " " + modifier);
        };
        departureTimeText.setOnClickListener(v -> {
            new TimePickerDialog(PostTripActivity.this, departTime, 0, 0,
                    false).show();
        });

        /*
        SETUP RETURN TIME PICKER
         */
        returnTimeText = (EditText) findViewById(R.id.editTextReturnTime);
        returnTime  = (TimePicker timePicker, int selectedHour, int selectedMinute) -> {
            String modifier = "am";
            String minutes, hours;
            if (selectedHour > 12) {
                selectedHour -= 12;
                modifier = "pm";
            }
            minutes = extractTimeString(selectedMinute);
            hours = extractTimeString(selectedHour);

            returnTimeText.setText("" + hours + ":" + minutes + " " + modifier);
        };
        returnTimeText.setOnClickListener(v -> {
            new TimePickerDialog(PostTripActivity.this, returnTime, 0, 0,
                    false).show();
        });
    }

    private String extractTimeString(int minutesOrHours) {
        return minutesOrHours < 10 ?
                "0" + Integer.toString(minutesOrHours) : Integer.toString(minutesOrHours);
    }

    private void updateDepartureDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        departureEditText.setText(sdf.format(departureCalendar.getTime()));
    }

    private void updateReturnDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        returnEditText.setText(sdf.format(returnCalendar.getTime()));
    }

    public void incrementSeats(View view) {
        inNumSeats = findViewById(R.id.textViewSeatValue);
        numSeats = Integer.parseInt(inNumSeats.getText().toString());
        if (numSeats < 4) {
            inNumSeats.setText(String.valueOf(numSeats + 1));
        }
    }

    public void decrementSeats(View view) {
        inNumSeats = findViewById(R.id.textViewSeatValue);
        numSeats = Integer.parseInt(inNumSeats.getText().toString());
        if (numSeats > 1) {
            inNumSeats.setText(String.valueOf(numSeats - 1));
        }
    }

    public void setProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    //https://stackoverflow.com/questions/37390080/convert-local-time-to-utc-and-vice-versa
    public static Date gmttoLocalDate(Date date) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    /**
     * Retrieves data from the form and makes a post api call to the trip endpoint.
     *
     * @param view The current view
     */
    public void postTrip(View view) {
        EditText inDepartureDate = findViewById(R.id.editTextDepartureDate);
        EditText inReturnDate = findViewById(R.id.editTextReturnDate);
        EditText inDepartureTime = findViewById(R.id.editTextDepartureTime);
        EditText inReturnTime = findViewById(R.id.editTextReturnTime);

        String startLocation = getStringFromEditText(R.id.editTextStartLocation);
        String endLocation = getStringFromEditText(R.id.editTextEndLocation);
        boolean canPickUp = getBooleanFromSwitch(R.id.switchCanPickUp);
        int numSeats = getIntegerFromTextView(R.id.textViewSeatValue, 4);
        boolean noSmoking = getBooleanFromSwitch(R.id.switchNoSmoking);
        boolean isQuiet = getBooleanFromSwitch(R.id.switchQuite);
        boolean willReturn = getBooleanFromSwitch(R.id.switchReturn);

        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyhh:mm a", Locale.US);
        Date startTime;
        Date returnTime = new Date(0);
        try {
            startTime = dfDate.parse(
                    inDepartureDate.getText().toString()
                            + inDepartureTime.getText().toString());
        } catch (ParseException error) {
            Log.e(TAG, "Error Parsing date/time.");
            return;
        }

        if (willReturn) {
            try {
                returnTime = dfDate.parse(
                        inReturnDate.getText().toString()
                                + inReturnTime.getText().toString());
            } catch (ParseException error) {
                Log.e(TAG, "Error Parsing date/time.");
                return;
            }

            if (returnTime.before(startTime)) {
                makeLongToast("Return date must be after departure date");
                return;
            }
        }

        Date today = gmttoLocalDate(new Date());
        if (startTime.before(today)) {
            makeLongToast("Start date must be in the future, not past");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Log.e(TAG, formatter.format(today));
            Log.e(TAG, formatter.format(startTime));
            Log.e(TAG, Calendar.getInstance().getTimeZone().getID());
            return;
        }

        double startLat;
        double startLng;
        double endLat;
        double endLng;
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses;

            addresses = geocoder.getFromLocationName(startLocation, 1);
            if (addresses.size() > 0) {
                startLat = addresses.get(0).getLatitude();
                startLng = addresses.get(0).getLongitude();
            } else {
                makeLongToast("Could not find starting location..." +
                        "Please try a different address");
                return;
            }

            addresses = geocoder.getFromLocationName(endLocation, 1);
            if (addresses.size() > 0) {
                endLat = addresses.get(0).getLatitude();
                endLng = addresses.get(0).getLongitude();
            } else {
                makeLongToast("Could not find ending address..." +
                        "Please try a different address");
                return;
            }
        } catch (IOException error) {
            Log.e(TAG, "Error getting location coordinates for: " + startLocation);
            return;
        } catch (Exception e) {
            Log.e(TAG, "Error in Geocode lookup for: " + startLocation);
            return;
        }
        CommonDependencyProvider commonDependencyProvider =
                provider == null ? new CommonDependencyProvider() : provider;
        String email = commonDependencyProvider.getAppHelper().getLoggedInUser().getEmail();

        Trip trip = new Trip(startTime.getTime(), startTime.getTime(), isQuiet, (!noSmoking),
                endLat, endLng, startLat, startLng, numSeats, 5.0);

        ApiInterface apiInterface = ApiClient.getApiInstance();
        callApi(apiInterface, trip);

        if (willReturn) {
            Trip returnTrip = new Trip(returnTime.getTime(), returnTime.getTime(), isQuiet,
                    (!noSmoking), startLat, startLng, endLat, endLng, numSeats, 5.0);
            callApi(apiInterface, returnTrip);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void makeLongToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Calls api to post a trip.
     *
     * @param apiInterface Contains api calls
     * @param trip         The trip to be posted
     */
    private void callApi(ApiInterface apiInterface, Trip trip) {
        CommonDependencyProvider provider = new CommonDependencyProvider();
        String email = provider.getAppHelper().getLoggedInUser().getEmail();
        Call<String> call;
        call = apiInterface.postUserDrivingTrip(email, trip);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int code = response.code();
                if (code == 200) {
                    Log.d(TAG, "Trip posted successfully.");
                    Log.d(TAG, "Response was: " + response.body());
                } else {
                    Log.d(TAG, "Retrofit failed to post trip, response code: "
                            + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to post trip.");
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });
    }

    /**
     * Gets an integer from an EditText
     *
     * @param id         The id of the EditText
     * @return The String content of the EditText
     */
    protected String getStringFromEditText(int id) {
        EditText editText = findViewById(id);
        return editText.getText().toString();
    }

    /**
     * Gets an integer from an EditText
     *
     * @param id         The id of the EditText
     * @param defaultVal The default value to use in case of an empty EditText
     * @return The int contents of the EditText or the default value
     */
    protected int getIntegerFromTextView(int id, int defaultVal) {
        TextView textView = findViewById(id);
        int result = textView.getText().toString().isEmpty()
                ? defaultVal : Integer.valueOf(textView.getText().toString());
        return result;
    }

    /**
     * Gets a boolean from an Switch
     * @param id         The id of the EditText
     * @return The boolean contents of the Switch
     */
    protected Boolean getBooleanFromSwitch(int id) {
        Switch s = findViewById(id);
        return s.isChecked();
    }

}
