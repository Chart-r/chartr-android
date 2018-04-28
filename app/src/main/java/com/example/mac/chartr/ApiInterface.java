package com.example.mac.chartr;

import com.example.mac.chartr.objects.ConfirmationEmail;
import com.example.mac.chartr.objects.Review;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<User> getUserFromEmail(@Path("email") String email);

    /**
     * Gets a user corresponding to the given uid.
     * @param uid The user's uid
     * @return A call to get the user corresponding to the uid.
     */
    @GET("user/uid/{uid}")
    Call<User> getUserFromUid(@Path("uid") String uid);

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
     * @param uid The ID of the user to be deleted
     * @return A call to delete the specified user
     */
    @DELETE("user/{uid}")
    Call<Void> deleteUser(@Path("uid") String uid);

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
     * @param uid The user's ID
     * @return A call to get a list of trips fulfilling the criteria
     */
    @GET("user/{uid}/trip")
    Call<List<Trip>> getAllUserTrips(@Path("uid") String uid);

    /**
     * Gets all trips for a user with the specified status
     * @param uid The user's ID
     * @param status user's status, can be either driving, riding, or pending
     * @return A call to get a list of trips fulfilling the criteria
     */
    @GET("user/{uid}/trip/{status}")
    Call<List<Trip>> getUserTripsForStatus(@Path("uid") String uid,
                                           @Path("status") String status);

    /**
     * Gets a single trip matching the specified trip ID.
     * @param tid The trip's ID.
     * @return A call to get the specified trip.
     */
    @GET("trip/{tid}")
    Call<Trip> getTrip(@Path("tid") String tid);

    /**
     * Updates a specified user's status in a specified trip to the status provided.
     * @param uid ID of the user to be updated
     * @param tid ID of the trip to be updated
     * @param status The new status of the user
     * @return A call to get a list of trips fulfilling the criteria
     */
    @PUT("user/{uid}/trip/{tid}/{status}")
    Call<String> updateTrip(@Path("uid") String uid,
                            @Path("tid") String tid,
                            @Path("status") String status);

    /**
     * Posts a trip with the specified user as the driver.
     * @param uid Driver's uid
     * @param trip The trip details
     * @return A call to post the trip.
     */
    @POST("user/{uid}/trip")
    Call<String> postUserDrivingTrip(@Path("uid") String uid,
                                     @Body Trip trip);

    /**
     * Delete the specified trip.
     * @param tid Trip's ID
     * @return A call to delete the trip
     */
    @DELETE("trip/{tid}")
    Call<Void> deleteTrip(@Path("tid") String tid);

    // Review related API calls
    /**
     * Posts a review from the specified user.
     * @param uid The ID of the user posting the review
     * @param review The review, must contain a reviewee, tid, comment, and rating
     * @return A call to post the review
     */
    @POST("user/{uid}/review")
    Call<String> postReviewFromUser(@Path("uid") String uid, @Body Review review);

    /**
     * Gets a single review with the specified rid.
     * @param rid The review's ID
     * @return A call to get a review
     */
    @GET("review/{rid}")
    Call<Review> getReview(@Path("rid") String rid);

    /**
     * Gets all the reviews for the specified user.
     * @param uid The user's ID
     * @return A call to get a list of all the reviews for a user.
     */
    @GET("user/{uid}/review")
    Call<List<Review>> getAllReviewsForUser(@Path("uid") String uid);

    /**
     * Posts the information for confirmation emails to the API.  The API will send confirmation
     * emails.
     * @param email An object containing necessary information for the confirmation emails.
     * @return A call to post the confirmation emails.
     */
    @POST("email/confirmation")
    Call<String> postTripConfirmationEmail(@Body ConfirmationEmail email);
}
