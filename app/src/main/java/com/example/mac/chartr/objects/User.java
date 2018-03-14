package com.example.mac.chartr.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class User {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    private float rating;

    @SerializedName("trips")
    @Expose
    private Map<String, String> trips;

    public User() {
        this.trips = new HashMap<String, String>();
    }

    public User(String email, String name, float rating) {
        this.email = email;
        this.name = name;
        this.rating = rating;
    }

    public User(String email, String name, float rating, Map<String, String> trips) {
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.trips = trips;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getTrips() {
        return trips;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTrips(Map<String, String> trips) {
        this.trips = trips;
    }

    public void addTrip(String tripID, String role) {
        trips.put(tripID, role);
    }
}
