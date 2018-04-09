package com.example.mac.chartr.objects;


import junit.framework.Assert;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by cygnus on 4/1/18.
 */


public class TripTest {

    @Test
    public void testTripCreationMethods() {
        Trip trip1 = new Trip(120, 123, true, false,
                223, 224, 102, 103,
                4, 12.34f, null, null);

        trip1.setId("abc");
        trip1.setUsers(new HashMap<>());

        Trip trip2 = new Trip(120, 123, true, false,
                223, 224, 102, 103,
                4, 12.34f);

        trip2.setId("abc");
        trip2.setUsers(new HashMap<>());

        Assert.assertTrue(trip1.equals(trip2));
    }

    @Test
    public void testGettersAndSetters() {
        Trip trip = new Trip(120, 123, true, false,
                223, 224, 102, 103,
                4, 12.34f, null, null);

        Assert.assertEquals(trip.getStartTime(), 120);
        Assert.assertEquals(trip.getEndTime(), 123);
        Assert.assertTrue(trip.getQuiet());
        Assert.assertFalse(trip.getSmoking());
        Assert.assertEquals(trip.getEndLat(), 223.0);
        Assert.assertEquals(trip.getEndLong(), 224.0);
        Assert.assertEquals(trip.getStartLat(), 102.0);
        Assert.assertEquals(trip.getStartLong(), 103.0);
        Assert.assertEquals(trip.getSeats(), 4);
        Assert.assertTrue(Math.abs(trip.getPrice() - 12.34) < 0.001);
        Assert.assertEquals(trip.getId(), null);
        Assert.assertEquals(trip.getUsers(), null);


        trip.setStartTime(1);
        trip.setEndTime(1);
        trip.setQuiet(false);
        trip.setSmoking(true);
        trip.setEndLat(1);
        trip.setEndLong(1);
        trip.setStartLat(1);
        trip.setStartLong(1);
        trip.setSeats(1);
        trip.setPrice(1f);
        trip.setId("100");
        trip.setUsers(null);


        Assert.assertEquals(trip.getStartTime(), 1);
        Assert.assertEquals(trip.getEndTime(), 1);
        Assert.assertFalse(trip.getQuiet());
        Assert.assertTrue(trip.getSmoking());
        Assert.assertEquals(trip.getEndLat(), 1.0);
        Assert.assertEquals(trip.getEndLong(), 1.0);
        Assert.assertEquals(trip.getStartLat(), 1.0);
        Assert.assertEquals(trip.getStartLong(), 1.0);
        Assert.assertEquals(trip.getSeats(), 1);
        Assert.assertEquals(trip.getPrice(), 1.0);
        Assert.assertEquals(trip.getId(), "100");
        Assert.assertEquals(trip.getUsers(), null);
    }

    @Test
    public void testHash() {
        Trip trip = new Trip(120, 123, true, false,
                223, 224, 102, 103,
                4, 12.34f, "abc", new HashMap<>());

        int hash = trip.hashCode();

        Assert.assertTrue(hash != 0);
    }
}
