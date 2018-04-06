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

    @SerializedName("birthdate")
    @Expose
    private String birthdate;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("trips")
    @Expose
    private Map<String, String> trips;

    private float rating;

    public User() {
        this.trips = new HashMap<>();
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, String> getTrips() {
        return trips;
    }

    public void setTrips(Map<String, String> trips) {
        this.trips = trips;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void addTrip(String tripID, String role) {
        trips.put(tripID, role);
    }
}
