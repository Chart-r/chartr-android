package com.example.mac.chartr.activities;

import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
public class PostTripActivityTest {
    private static final String TAG = "PostTripActivityTest";
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }


    @Test
    public void testInitPickers() {
        PostTripActivity activity = Robolectric.buildActivity(PostTripActivity.class)
                .create().get();


        EditText departureDate = activity.findViewById(R.id.editTextDepartureDate);
        EditText departureTime = activity.findViewById(R.id.editTextDepartureTime);
        EditText returnDate = activity.findViewById(R.id.editTextReturnDate);
        EditText returnTime = activity.findViewById(R.id.editTextReturnTime);

        Assert.assertEquals(0, departureDate.getFocusable());
        Assert.assertEquals(0, departureTime.getFocusable());
        Assert.assertEquals(0, returnDate.getFocusable());
        Assert.assertEquals(0, returnTime.getFocusable());
    }

    @Test
    public void decrementSeatsTest() {
        PostTripActivity activity = Robolectric.setupActivity(PostTripActivity.class);

        TextView results = (TextView) activity.findViewById(R.id.textViewSeatValue);

        activity.decrementSeats(null);
        String str = results.getText().toString();
        Assert.assertEquals("1", str);

        results.setText("3");
        activity.decrementSeats(null);
        str = results.getText().toString();
        Assert.assertEquals("2", str);
    }

    @Test
    public void incrementSeatsTest() {
        PostTripActivity activity = Robolectric.setupActivity(PostTripActivity.class);

        TextView results = (TextView) activity.findViewById(R.id.textViewSeatValue);

        activity.incrementSeats(null);
        String str = results.getText().toString();
        Assert.assertEquals("2", str);

        results.setText("4");
        activity.incrementSeats(null);
        str = results.getText().toString();
        Assert.assertEquals("4", str);
    }

    @Test
    public void testPostTripDoesNotCrash() {
        PostTripActivity activity = Robolectric.setupActivity(PostTripActivity.class);

        CommonDependencyProvider provider = Mockito.mock(CommonDependencyProvider.class);
        AppHelper appHelper = Mockito.mock(AppHelper.class);
        User user = Mockito.mock(User.class);

        when(provider.getAppHelper()).thenReturn(appHelper);
        when(appHelper.getLoggedInUser()).thenReturn(user);

        activity.setProvider(provider);

        EditText inStartLocation = activity.findViewById(R.id.editTextStartLocation);
        EditText inEndLocation = activity.findViewById(R.id.editTextEndLocation);
        EditText inDepartureDate = activity.findViewById(R.id.editTextDepartureDate);
        EditText inReturnDate = activity.findViewById(R.id.editTextReturnDate);
        EditText inDepartureTime = activity.findViewById(R.id.editTextDepartureTime);
        EditText inReturnTime = activity.findViewById(R.id.editTextReturnTime);
        Switch inCanPickUp = activity.findViewById(R.id.switchCanPickUp);
        TextView inNumSeats = activity.findViewById(R.id.textViewSeatValue);
        Switch inNoSmoking = activity.findViewById(R.id.switchNoSmoking);
        Switch inIsQuiet = activity.findViewById(R.id.switchQuite);
        Switch inWillReturn = activity.findViewById(R.id.switchReturn);

        inStartLocation.setText("Chicago, Illinois");
        inEndLocation.setText("Champaign, Illinois");
        inDepartureDate.setText("10/10/2018");
        inReturnDate.setText("11/11/2018");
        inDepartureTime.setText("12:00");
        inReturnTime.setText("12:00");
        inCanPickUp.setChecked(false);
        inNumSeats.setText("4");
        inNoSmoking.setChecked(true);
        inIsQuiet.setChecked(true);
        inWillReturn.setChecked(true);

        activity.postTrip(null);
    }
}



