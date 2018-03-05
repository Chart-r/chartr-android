package com.example.mac.chartr.objects;


public class Trip {
    private String startTime;
    private String endTime;
    private Boolean quiet;
    private Boolean smoking;
    private float endLat;
    private float endLong;
    private float startLat;
    private float startLong;
    private int seats;
    private float price;
    private String id;
    private User[] users;

    public Trip() {
    }

    // TODO implement api response based Trip constructor
    public Trip(String apiResponse) {

    }

    public Trip(String startTime, String endTime, Boolean quiet, Boolean smoking, float endLat, float endLong, float startLat, float startLong, int seats, float price, String id, User[] users) {
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
        this.id = id;
        this.users = users;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public float getEndLat() {
        return endLat;
    }

    public void setEndLat(float endLat) {
        this.endLat = endLat;
    }

    public float getEndLong() {
        return endLong;
    }

    public void setEndLong(float endLong) {
        this.endLong = endLong;
    }

    public float getStartLat() {
        return startLat;
    }

    public void setStartLat(float startLat) {
        this.startLat = startLat;
    }

    public float getStartLong() {
        return startLong;
    }

    public void setStartLong(float startLong) {
        this.startLong = startLong;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
