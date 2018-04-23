package com.example.mac.chartr.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostTripActivity extends AppCompatActivity {
    private static final String TAG = PostTripActivity.class.getSimpleName();
    private static final int START_PLACE_PICKER = 1;
    private static final int DEST_PLACE_PICKER = 2;


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

    EditText startLocationEditText;
    Double startLocationLat;
    Double startLocationLng;

    EditText endLocationEditText;
    Double endLocationLat;
    Double endLocationLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_trip);
        initViews();
        initPickers();
    }


    private void initViews() {
        departureEditText = findViewById(R.id.editTextDepartureDate);
        returnEditText = findViewById(R.id.editTextReturnDate);
        departureTimeText = findViewById(R.id.editTextDepartureTime);
        returnTimeText = findViewById(R.id.editTextReturnTime);
        startLocationEditText = findViewById(R.id.editTextStartLocation);
        startLocationEditText.setFocusable(false);
        startLocationEditText.setClickable(true);
        startLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickButtonClick(START_PLACE_PICKER);
            }
        });
        endLocationEditText = findViewById(R.id.editTextEndLocation);
        endLocationEditText.setFocusable(false);
        endLocationEditText.setClickable(true);
        endLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickButtonClick(DEST_PLACE_PICKER);
            }
        });
    }

    /**
     * Initializes the date and time pickers.  Based off:
     * https://stackoverflow.com/questions/14933330/
     * datepicker-how-to-popup-datepicker-when-click-on-edittext
     *
     * https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
     */
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
            setTimeField(selectedHour, selectedMinute, departureTimeText);
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
            setTimeField(selectedHour, selectedMinute, returnTimeText);
        };
        returnTimeText.setOnClickListener(v -> {
            new TimePickerDialog(PostTripActivity.this, returnTime, 0, 0,
                    false).show();
        });
    }

    public void onPickButtonClick(int key) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, key);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        if ((requestCode == START_PLACE_PICKER || requestCode == DEST_PLACE_PICKER)
                && resultCode == Activity.RESULT_OK) {
            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            LatLng placeCoordinates = place.getLatLng();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            if (requestCode == START_PLACE_PICKER) {
                startLocationLat = placeCoordinates.latitude;
                startLocationLng = placeCoordinates.longitude;
                if (place.getPlaceTypes().get(0) == 0) {
                    startLocationEditText.setText(address);
                } else {
                    startLocationEditText.setText(name);
                }
            } else {
                endLocationLat = placeCoordinates.latitude;
                endLocationLng = placeCoordinates.longitude;
                if (place.getPlaceTypes().get(0) == 0) {
                    endLocationEditText.setText(address);
                } else {
                    endLocationEditText.setText(name);
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setTimeField(int selectedHour, int selectedMinute, EditText field) {
        String modifier = "am";
        String minutes, hours;
        if (selectedHour > 12) {
            selectedHour -= 12;
            modifier = "pm";
        } else if (selectedHour == 0) {
            selectedHour = 12;
        } else if (selectedHour == 12) {
            modifier = "pm";
        }
        minutes = extractTimeString(selectedMinute);
        hours = extractTimeString(selectedHour);

        field.setText("" + hours + ":" + minutes + " " + modifier);
    }

    private String extractTimeString(int minutesOrHours) {
        return minutesOrHours < 10
                ? "0" + Integer.toString(minutesOrHours) : Integer.toString(minutesOrHours);
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
        Date local = new Date(date.getTime()
                + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    /**
     * Retrieves data from the form and makes a post api call to the trip endpoint.
     *
     * @param view The current view
     */
    public void postTrip(View view) {
        String startLocation = startLocationEditText.getText().toString();
        String endLocation = endLocationEditText.getText().toString();
        boolean canPickUp = getBooleanFromSwitch(R.id.switchCanPickUp);
        int numSeats = getIntegerFromTextView(R.id.textViewSeatValue, 4);
        boolean noSmoking = getBooleanFromSwitch(R.id.switchNoSmoking);
        boolean isQuiet = getBooleanFromSwitch(R.id.switchQuite);
        boolean willReturn = getBooleanFromSwitch(R.id.switchReturn);

        startLocationEditText.setBackground(getDrawable(R.drawable.text_border_default));
        endLocationEditText.setBackground(getDrawable(R.drawable.text_border_default));
        departureEditText.setBackground(getDrawable(R.drawable.text_border_default));
        departureTimeText.setBackground(getDrawable(R.drawable.text_border_default));
        returnEditText.setBackground(getDrawable(R.drawable.text_border_default));
        returnTimeText.setBackground(getDrawable(R.drawable.text_border_default));



        if (startLocation.equals("")) {
            makeLongToast("Please enter a starting location");
            startLocationEditText.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        } else if (endLocation.equals("")) {
            makeLongToast("Please enter an ending location");
            endLocationEditText.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyhh:mm a", Locale.US);
        Date startTime;
        Date returnTime = new Date(0);
        try {
            startTime = dfDate.parse(
                    departureEditText.getText().toString()
                            + departureTimeText.getText().toString());
        } catch (ParseException error) {
            Log.e(TAG, "Error Parsing date/time.");
            makeLongToast("Please enter a departure date and time");
            departureEditText.setBackground(getDrawable(R.drawable.text_border_error));
            departureTimeText.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        if (willReturn) {
            try {
                returnTime = dfDate.parse(
                        returnEditText.getText().toString()
                                + returnTimeText.getText().toString());
            } catch (ParseException error) {
                Log.e(TAG, "Error Parsing date/time.");
                makeLongToast("Please enter a return date and time");
                returnEditText.setBackground(getDrawable(R.drawable.text_border_error));
                returnTimeText.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }

            if (returnTime.before(startTime)) {
                makeLongToast("Return date must be after departure date");
                returnEditText.setBackground(getDrawable(R.drawable.text_border_error));
                returnTimeText.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }
        }

        Date today = gmttoLocalDate(new Date());
        if (startTime.before(today)) {
            makeLongToast("Start date must be in the future, not past");
            departureEditText.setBackground(getDrawable(R.drawable.text_border_error));
            departureTimeText.setBackground(getDrawable(R.drawable.text_border_error));

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Log.e(TAG, formatter.format(today));
            Log.e(TAG, formatter.format(startTime));
            Log.e(TAG, Calendar.getInstance().getTimeZone().getID());
            return;
        }

        CommonDependencyProvider commonDependencyProvider =
                provider == null ? new CommonDependencyProvider() : provider;

        Trip trip = new Trip(startTime.getTime(), startTime.getTime(), isQuiet, (!noSmoking),
                endLocationLat, endLocationLng, startLocationLat, startLocationLng,
                numSeats, 5.0);

        ApiInterface apiInterface = ApiClient.getApiInstance();
        callApi(apiInterface, trip);

        if (willReturn) {
            Trip returnTrip = new Trip(returnTime.getTime(), returnTime.getTime(), isQuiet,
                    (!noSmoking), startLocationLat, startLocationLng, endLocationLat,
                    endLocationLng, numSeats, 5.0);
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
        String uid = provider.getAppHelper().getLoggedInUser().getUid();
        Call<String> call;
        call = apiInterface.postUserDrivingTrip(uid, trip);
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
