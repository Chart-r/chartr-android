package com.example.mac.chartr;


import android.location.Address;
import android.location.Geocoder;

import com.example.mac.chartr.fragments.SearchFragment;

import org.junit.Test;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationNameProviderTest {
    private Geocoder geocoder = mock(Geocoder.class);

    @Test
    public void testGetLocationNameNoAddress() {
        LocationNameProvider.getLocationName(12, 13, geocoder, "TEST");
    }

    @Test
    public void testGetLocationName() throws IOException {
        Address address = mock(Address.class);
        when(address.getAddressLine(0)).thenReturn("123 my way");
        when(address.getMaxAddressLineIndex()).thenReturn(0);

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        when(geocoder.getFromLocation(12, 13, 1))
                .thenReturn(addressList);


        String result = LocationNameProvider.getLocationName(12, 13, geocoder,
                "TEST");

        assertEquals("123 my way", result);
    }

    @Test
    public void testIOException() throws IOException {
        when(geocoder.getFromLocation(12, 13, 1))
                .thenThrow(new IOException("HA"));


        String result = LocationNameProvider.getLocationName(12, 13, geocoder,
                "TEST");

        assertEquals("12.0, 13.0", result);
    }

    @Test
    public void testIllegalArgument() throws IOException {
        when(geocoder.getFromLocation(12, 13, 1))
                .thenThrow(new IllegalArgumentException("HA"));


        String result = LocationNameProvider.getLocationName(12, 13, geocoder,
                "TEST");

        assertEquals("12.0, 13.0", result);
    }
}
