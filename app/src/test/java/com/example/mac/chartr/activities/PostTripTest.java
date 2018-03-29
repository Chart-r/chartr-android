package com.example.mac.chartr.activities;

import android.widget.TextView;

import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by Michael Rush on 3/3/2018.
 */
/*@RunWith(AndroidJUnit4.class)
@LargeTest*/

@RunWith(RobolectricTestRunner.class)
public class PostTripTest {
    private static final String TAG = "PostTripTest";
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
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
}



