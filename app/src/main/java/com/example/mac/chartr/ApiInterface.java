package com.example.mac.chartr;

import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface for implemented API calls.
 */

public interface ApiInterface {
    // User API calls

    /**
     * Gets a user corresponding to the given email.
     * @param email The user's email
     * @return A call to get the user corresponding to the email
     */
    @GET("user/{email}")
    Call<User> getUser(@Path("email") String email);

    /**
     * Posts a user.
     * @param user The user to be posted, should have email,
     *             name, birthdate, and phone fields filled
     * @return A call to post the given user
     */
    @POST("user")
    Call<Void> postUser(@Body User user);

    /**
     * Deletes the user corresponding to the given email.
     * @param email The email of the user to be deleted
     * @return A call to delete the specified user
     */
    @DELETE("user/{email}")
    Call<Void> deleteUser(@Path("email") String email);

    // Trip API calls
    /**
     * Gets all the trips.
     * @return A call to get a list of sll trips
     */
    @GET("trip")
    Call<List<Trip>> getAllTrips();

    /**
     * Gets all the trips that have not started yet.
     * @return A call to get a list of current trips.
     */
    @GET("trip/current")
    Call<List<Trip>> getAllCurrentTrips();

    /**
     * Gets all the trips for a given user.
     * @param email Email of the user.
     * @return A call to get a list of trips fulfilling the criteria
     */
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

    /**
     * Gets a single trip matching the specified trip ID.
     * @param tripID The trip's ID.
     * @return A call to get the specified trip.
     */
    @GET("trip/{tripID}")
    Call<Trip> getTrip(@Path("tripID") String tripID);

    /**
     * Updates a specified user's status in a specified trip to the status provided.
     * @param email The user to be updated
     * @param tripID The trip to be updated
     * @param status The new status of the user
     * @return A call to get a list of trips fulfilling the criteria
     */
    @GET("user/{email}/trip/{tripID}/{status}")
    Call<String> updateTrip(@Path("email") String email,
                            @Path("tripID") String tripID,
                            @Path("status") String status);

    /**
     * Posts a trip with the specified user as the driver.
     * @param email The driver's email
     * @param trip The trip details
     * @return A call to post the trip.
     */
    @POST("user/{email}/trip")
    Call<String> postUserDrivingTrip(@Path("email") String email,
                                   @Body Trip trip);

    /**
     * Delete the specified trip.
     * @param tripID The trip ID of the trip to delete
     * @return A call to delete the trip
     */
    @DELETE("trip/{tripID}")
    Call<Void> deleteTrip(@Path("tripID") String tripID);
}
