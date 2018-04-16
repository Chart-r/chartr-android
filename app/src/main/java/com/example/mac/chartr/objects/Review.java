package com.example.mac.chartr.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    private static final String TAG = Trip.class.getSimpleName();

    @SerializedName("rid")
    @Expose
    private String rid;

    @SerializedName("reviewer")
    @Expose
    private String reviewer;

    @SerializedName("reviewee")
    @Expose
    private String reviewee;

    @SerializedName("trip")
    @Expose
    private String trip;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("rating")
    @Expose
    private double rating;

    public Review() { }

    public Review(String rid, String reviewer, String reviewee,
                  String trip, String comment, double rating) {
        this.rid = rid;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.trip = trip;
        this.comment = comment;
        this.rating = rating;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewee() {
        return reviewee;
    }

    public void setReviewee(String reviewee) {
        this.reviewee = reviewee;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Review review = (Review) o;

        if (Double.compare(review.rating, rating) != 0) {
            return false;
        }
        if (rid != null ? !rid.equals(review.rid) : review.rid != null) {
            return false;
        }
        if (reviewer != null ? !reviewer.equals(review.reviewer) : review.reviewer != null) {
            return false;
        }
        if (reviewee != null ? !reviewee.equals(review.reviewee) : review.reviewee != null) {
            return false;
        }
        if (trip != null ? !trip.equals(review.trip) : review.trip != null) {
            return false;
        }
        return comment != null ? comment.equals(review.comment) : review.comment == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = rid != null ? rid.hashCode() : 0;
        result = 31 * result + (reviewer != null ? reviewer.hashCode() : 0);
        result = 31 * result + (reviewee != null ? reviewee.hashCode() : 0);
        result = 31 * result + (trip != null ? trip.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
