package com.example.mac.chartr;

import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface for implemented API calls.
 */

public interface ApiInterface {
    // User API calls
    @GET("user/{email}")
    Call<User> getUser(@Path("email") String email);

    @POST("user")
    Call<Void> postUser(@Body User user);

    // Trip API calls
    @GET("user/{email}/trip")
    Call<List<Trip>> getAllUserTrips(@Path("email") String email);

    /**
     * Gets all trips for a user with the specified status
     * @param email user's email
     * @param status user's status, can be either driving, riding, or pending
     * @return A call to get a list of trips fulfilling the criteria
     */
    @GET("user/{email}/trip/{status}")
    Call<List<Trip>> getUserTripsForStatus(@Path("email") String email,
                                           @Path("status") String status);

    @POST("user/{email}/trip")
    Call<String> postUserDrivingTrip(@Path("email") String email,
                                   @Body Trip trip);

    /**
     * Updates a specified user's status in a specified trip to the status provided.
     * @param email The user to be updated
     * @param tripID The trip to be updated
     * @param status The new status of the user
     * @return A call to get a list of trips fulfilling the criteria
     */
    @GET("user/{email}/trip/{tripID}/{status}")
    Call<Trip> updateTrip(@Path("email") String email,
                          @Path("tripID") String tripID,
                          @Path("status") String status);
}
