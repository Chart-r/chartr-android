package com.example.mac.chartr.objects;

import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Michael Rush on 4/22/2018.
 */

public class ReviewTest {
    @Test
    public void testEquals(){
        Review r1 = new Review("1","2","3","4","5",6.0);
        Review r2 = new Review("1","2","3","4","5",6.0);
        Review r3 = new Review("1","2","3","4","5",7.0);
        Assert.assertTrue(r1.equals(r2));
        r2.setComment("fail");
        Assert.assertFalse(r1.equals(r2));
        r2.setRating(7.0);
        Assert.assertFalse(r1.equals(r2));
        r2.setReviewee("fail");
        Assert.assertFalse(r1.equals(r2));
        r2.setReviewer("fail");
        Assert.assertFalse(r1.equals(r2));
        r2.setRid("fail");
        Assert.assertFalse(r1.equals(r2));
        r2.setTrip("fail");
        Assert.assertFalse(r1.equals(r2));
    }

    @Test
    public void testHashCode(){
        Review r = new Review();
        r.setRid("hi");
        int hash = r.hashCode();
        Assert.assertEquals(hash,817163167);
    }
}
