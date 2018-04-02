package com.example.mac.chartr.objects;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Trip {
    private static final String TAG = Trip.class.getSimpleName();

    @SerializedName("email")
    @Expose
    private String driverEmail;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("start_time")
    @Expose
    private long startTime;

    @SerializedName("end_time")
    @Expose
    private long endTime;

    @SerializedName("smoking")
    @Expose
    private Boolean smoking;

    @SerializedName("start_lat")
    @Expose
    private double startLat;

    @SerializedName("end_lat")
    @Expose
    private double endLat;

    @SerializedName("end_lng")
    @Expose
    private double endLong;

    @SerializedName("start_lng")
    @Expose
    private double startLong;

    @SerializedName("seats")
    @Expose
    private int seats;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("users")
    @Expose
    private Map<String, String> users;


    private boolean quiet;

    public Trip() {
        users = new HashMap<>();
    }

    public Trip(long startTime, long endTime, Boolean quiet, Boolean smoking, float endLat,
                float endLong, float startLat, float startLong, int seats, float price,
                String id, Map<String, String> users) {

        this.setProperties(startTime, endTime, quiet, smoking, endLat, endLong, startLat, startLong,
                seats, price);

        this.id = id;
        this.users = users;
        this.driverEmail = null;
    }

    public Trip(long startTime, long endTime, Boolean quiet, Boolean smoking, double endLat,
                double endLong, double startLat, double startLong, int seats, double price,
                String email) {

        this.setProperties(startTime, endTime, quiet, smoking, endLat, endLong, startLat, startLong,
                seats, price);

        this.driverEmail = email;
        this.id = null;
        this.users = null;
    }

    private void setProperties(long startTime, long endTime, Boolean quiet, Boolean smoking,
                              double endLat, double endLong, double startLat, double startLong,
                              int seats, double price) {
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
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverFromUsers() {
        // No user map
        if (users == null) {
            return "";
        }

        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue().equals("Driver")) {
                return pair.getKey().toString();
            }
            Log.d(TAG, pair.toString());
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

        if (startTime != trip.startTime) {
            return false;
        }
        if (endTime != trip.endTime) {
            return false;
        }
        if (Double.compare(trip.startLat, startLat) != 0) {
            return false;
        }
        if (Double.compare(trip.endLat, endLat) != 0) {
            return false;
        }
        if (Double.compare(trip.endLong, endLong) != 0) {
            return false;
        }
        if (Double.compare(trip.startLong, startLong) != 0) {
            return false;
        }
        if (seats != trip.seats) {
            return false;
        }
        if (Double.compare(trip.price, price) != 0) {
            return false;
        }
        if (quiet != trip.quiet) {
            return false;
        }
        if (driverEmail != null
                ? !driverEmail.equals(trip.driverEmail) : trip.driverEmail != null) {
            return false;
        }
        if (id != null ? !id.equals(trip.id) : trip.id != null) {
            return false;
        }
        if (smoking != null ? !smoking.equals(trip.smoking) : trip.smoking != null) {
            return false;
        }
        return users != null ? users.equals(trip.users) : trip.users == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = driverEmail != null ? driverEmail.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (endTime ^ (endTime >>> 32));
        result = 31 * result + (smoking != null ? smoking.hashCode() : 0);
        temp = Double.doubleToLongBits(startLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + seats;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (quiet ? 1 : 0);
        return result;
    }
}
