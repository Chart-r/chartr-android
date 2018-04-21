package com.example.mac.chartr.activities;

import android.content.Intent;

import com.example.mac.chartr.objects.Trip;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class TripDetailsActivityTest {

    private Trip trip1 = new Trip(1234L, 1234L, true, false,
            12.4, 12.5, 15.4, 14.5, 5, 500);

    private Map<String, String> map;

    @Before
    public void setup() {
        map = new HashMap<>();

        map.put("email@email.com", "riding");

        trip1.setUsers(map);
    }

    @Test
    public void testActivityCreation() {
        Intent intent = new Intent();

        intent.putExtra("trip", trip1);
        intent.putExtra("type", "mytrips");

        TripDetailActivity activity = Robolectric.buildActivity(TripDetailActivity.class, intent)
                .create()
                .get();

        assertNotNull(activity);
    }
}
