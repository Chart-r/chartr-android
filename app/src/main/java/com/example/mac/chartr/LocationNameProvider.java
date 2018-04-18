package com.example.mac.chartr;


import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class LocationNameProvider {

    public static String getLocationName(double latitude, double longitude,
                                         Geocoder geocoder, String TAG) {
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e(TAG, ioException.toString());
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e(TAG, "Lat/Long Error: "
                    + "Latitude = " + latitude
                    + ", Longitude = "
                    + longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            Log.e(TAG, "No address found");
        } else {
            Address address = addresses.get(0);
            StringBuilder stringBuilder = new StringBuilder(address.getAddressLine(0));

            // Fetch the address lines using getAddressLine
            for (int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                stringBuilder.append(", ").append(address.getAddressLine(i));
            }
            Log.i(TAG, "Address found");
            return stringBuilder.toString();
        }

        // In the case of failure, return location coordinate string
        return latitude + ", " + longitude;
    }
}
