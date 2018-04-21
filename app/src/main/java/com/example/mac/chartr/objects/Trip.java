package com.example.mac.chartr.objects;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Trip implements Serializable {
    private static final String TAG = Trip.class.getSimpleName();

    @SerializedName("tid")
    @Expose
    private String tid;

    @SerializedName("start_lat")
    @Expose
    private double startLat;

    @SerializedName("start_lng")
    @Expose
    private double startLong;

    @SerializedName("end_lat")
    @Expose
    private double endLat;

    @SerializedName("end_lng")
    @Expose
    private double endLong;

    @SerializedName("start_time")
    @Expose
    private long startTime;

    @SerializedName("end_time")
    @Expose
    private long endTime;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("seats")
    @Expose
    private int seats;

    @SerializedName("smoking")
    @Expose
    private Boolean smoking;

    @SerializedName("users")
    @Expose
    private Map<String, String> users;

    private boolean quiet;

    public Trip() {
        users = new HashMap<>();
    }

    public Trip(long startTime, long endTime, Boolean quiet, Boolean smoking, double endLat,
                double endLong, double startLat, double startLong, int seats, double price,
                String tid, Map<String, String> users) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quiet = quiet;
        this.smoking = smoking;
        this.endLat = endLat;
        this.endLong = endLong;
        this.startLat = startLat;
        this.startLong = startLong;
        this.seats = seats;
        this.price = price;
        this.tid = tid;
        this.users = users;
    }

    public Trip(long startTime, long endTime, Boolean quiet, Boolean smoking, double endLat,
                double endLong, double startLat, double startLong, int seats, double price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quiet = quiet;
        this.smoking = smoking;
        this.endLat = endLat;
        this.endLong = endLong;
        this.startLat = startLat;
        this.startLong = startLong;
        this.seats = seats;
        this.price = price;
    }

    public String getStartTimeString() {
        return getTime(startTime);
    }

    public String getEndTimeString() {
        return getTime(endTime);
    }

    public String getStartDateString() {
        return getDate(startTime);
    }

    public String getEndDateString() {
        return getDate(endTime);
    }

    public String getStartLocationShortName(Context context) {
        return getLocationName(context, startLat, startLong, true);
    }

    public String getStartLocationLongName(Context context) {
        return getLocationName(context, startLat, startLong, false);
    }

    public String getEndLocationShortName(Context context) {
        return getLocationName(context, endLat, endLong, true);
    }

    public String getEndLocationLongName(Context context) {
        return getLocationName(context, endLat, endLong, false);
    }

    /**
     * Checks if a user is already on a given trip
     * @param uid The user's id
     * @return true if the trip contains the user in any state, false otherwise
     */
    public boolean containsUser(String uid) {
        return users.containsKey(uid);
    }

    public String getDriverId() {
        for (String key : users.keySet()) {
            if (users.get(key).equals("driving")) {
                return key;
            }
        }
        return "error";
    }

    public int getRidingCount() {
        int count = 0;
        for (String status : users.values()) {
            if (status.equals("riding")) {
                count++;
            }
        }
        return count;
    }

    private String getTime(long longDate) {
        Date date = new Date(longDate);
        SimpleDateFormat formatter =
                new SimpleDateFormat("h:mm a zzz", Locale.getDefault());
        return formatter.format(date);
    }

    private String getDate(long longDate) {
        Date date = new Date(longDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault());
        return formatter.format(date);
    }

    private String getLocationName(Context context, double latitude,
                                   double longitude, boolean shortName) {
        /* testAddTripView was failing on account of this code, specifically:
         * Method getFromLocation in android.location.Geocoder not mocked.
         * Geocoder functionality needs to be extracted to a separate function which
         * receives a geocoder argument that can be mocked and passed in for testing.
         */
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
            StringBuilder stringBuilder = new StringBuilder();

            if (shortName) {
                String[] addressArray = address.getAddressLine(0).split(", ");
                stringBuilder.append(addressArray[addressArray.length - 3]);
                stringBuilder.append(", ");
                stringBuilder.append(addressArray[addressArray.length - 2].substring(0, 2));
                return stringBuilder.toString();
            } else {
                stringBuilder.append(address.getAddressLine(0));
                // Fetch the address lines using getAddressLine
                for (int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(", ").append(address.getAddressLine(i));
                }
                Log.i(TAG, "Address found");
                return stringBuilder.toString();
            }
        }

        // In the case of failure, return location coordinate string
        return latitude + ", " + longitude;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Boolean getQuiet() {
        return quiet;
    }

    public void setQuiet(Boolean quiet) {
        this.quiet = quiet;
    }

    public Boolean getSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLong() {
        return endLong;
    }

    public void setEndLong(double endLong) {
        this.endLong = endLong;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLong() {
        return startLong;
    }

    public void setStartLong(double startLong) {
        this.startLong = startLong;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return tid;
    }

    public void setId(String tid) {
        this.tid = tid;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    public void addUser(String email, String role) {
        users.put(email, role);
    }

    public String getDriverFromUsers() {
        // No user map
        if (users == null) {
            return "";
        }

        for (Object o : users.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getValue().equals("driving")) {
                return pair.getKey().toString();
            }
        }

        // No driver in list
        return "";
    }

    public String getUserStatus(String userEmail) {
        // No user map
        if (users == null) {
            return "";
        }

        for (Object o : users.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getKey().equals(userEmail)) {
                return pair.getValue().toString();
            }
        }

        // No driver in list
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Trip trip = (Trip) o;

        if (Double.compare(trip.startLat, startLat) != 0) {
            return false;
        }
        if (Double.compare(trip.startLong, startLong) != 0) {
            return false;
        }
        if (Double.compare(trip.endLat, endLat) != 0) {
            return false;
        }
        if (Double.compare(trip.endLong, endLong) != 0) {
            return false;
        }
        if (startTime != trip.startTime) {
            return false;
        }
        if (endTime != trip.endTime) {
            return false;
        }
        if (Double.compare(trip.price, price) != 0) {
            return false;
        }
        if (seats != trip.seats) {
            return false;
        }
        if (quiet != trip.quiet) {
            return false;
        }
        if (!tid.equals(trip.tid)) {
            return false;
        }
        if (!smoking.equals(trip.smoking)) {
            return false;
        }
        return users.equals(trip.users);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = tid.hashCode();
        temp = Double.doubleToLongBits(startLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (endTime ^ (endTime >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + seats;
        result = 31 * result + smoking.hashCode();
        result = 31 * result + users.hashCode();
        result = 31 * result + (quiet ? 1 : 0);
        return result;
    }
}
