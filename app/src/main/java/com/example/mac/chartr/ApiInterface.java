package com.example.mac.chartr;

import com.example.mac.chartr.objects.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Interface for implemented API calls.
 */

public interface ApiInterface {

    @GET("/prod/trip/*?")
    Call<List<Trip>> getAllUserTrips(@Query("email") String email);

    @GET("prod/trip/driving?")
    Call<List<Trip>> getUserDrivingTrips(@Query("email") String email);

    @GET("prod/trip/riding?")
    Call<List<Trip>> getUserRidingTrips(@Query("email") String email);

    @GET("prod/trip/pending?")
    Call<List<Trip>> getUserPendingTrips(@Query("email") String email);

    @POST("prod/trip/*?")
    Call<Trip> postUserDrivingTrip(@Body Trip trip);

    @PUT("prod/trip/*")
    Call<Trip> updateTrip(@Body Trip trip);

    @DELETE("prod/trip/*")
    Call<Trip> deleteTrip(@Body Trip trip);
}
