package com.example.mac.chartr.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Trip {
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
