package com.example.mac.chartr.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("uid")
    @Expose
    private String uid;

    // Format for email is +12223334444
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

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("review_count")
    @Expose
    private int reviewCount;

    public User() { }

    public User(String email, String name, float rating) {
        this.email = email;
        this.name = name;
        this.rating = rating;
    }

    public User(String email,
                String name,
                String birthdate,
                String phone,
                float rating,
                int reviewCount) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
