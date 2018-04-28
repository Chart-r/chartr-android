package com.example.mac.chartr.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for a Confirmation Email on Chartr representing all information that we need to send to
 * the API. Can be serialized back and forth in order to send to the API.
 */
public class ConfirmationEmail {
    @SerializedName("driverName")
    @Expose
    private String driverName;

    @SerializedName("riderName")
    @Expose
    private String riderName;

    // Format for phone is +1-222-333-4444
    @SerializedName("driverPhone")
    @Expose
    private String driverPhone;

    // Format for phone is +1-222-333-4444
    @SerializedName("riderPhone")
    @Expose
    private String riderPhone;

    @SerializedName("driverEmail")
    @Expose
    private String driverEmail;

    @SerializedName("riderEmail")
    @Expose
    private String riderEmail;

    @SerializedName("tripTime")
    @Expose
    private long tripTime;

    public ConfirmationEmail() { }

    public ConfirmationEmail(String driverName, String riderName, String driverPhone,
                             String riderPhone, String driverEmail, String riderEmail,
                             long tripTime) {
        this.driverName = driverName;
        this.riderName = riderName;
        this.driverPhone = driverPhone;
        this.riderPhone = riderPhone;
        this.driverEmail = driverEmail;
        this.riderEmail = riderEmail;
        this.tripTime = tripTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getRiderEmail() {
        return riderEmail;
    }

    public void setRiderEmail(String riderEmail) {
        this.riderEmail = riderEmail;
    }

    public long getTripTime() {
        return tripTime;
    }

    public void setTripTime(long tripTime) {
        this.tripTime = tripTime;
    }
}
