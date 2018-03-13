package com.example.mac.chartr.objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mac.chartr.utils.ObjectCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for making user related API calls.
 */

public class UserInvoker {
    private static final String TAG = UserInvoker.class.getSimpleName();

    /**
     * Gets trips for a given driver username.
     * @param context The context of the calling activity
     * @param username The username of the user to be retrieved
     * @param callback A callback function that can be used to retrieve data from the response
     */
    public static void get(Context context, String username, ObjectCallback callback) {
        String params = "email=" + username;

        callURL(context,
                "https://99sepehum8.execute-api.us-east-2.amazonaws.com/prod/user/*?" + params,
                Request.Method.GET,
                null,
                callback);

    }

    /**
     * Gets trips for a given driver username.
     * @param context The context of the calling activity
     * @param user The user to be added to the database
     * @param callback A callback function that can be used to retrieve data from the response
     */
    public static void create(Context context, User user, ObjectCallback callback) {
        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("email", user.getEmail());
            userDetails.put("name", user.getName());
            callURL(context,
                    "https://99sepehum8.execute-api.us-east-2.amazonaws.com/prod/user/*",
                    Request.Method.POST,
                    userDetails,
                    callback);
        } catch (JSONException error) {
            Log.e(TAG, "Error creating JSONObject: " + error.getMessage());
        }

    }

    /**
     * Updates a user in the database
     * @param context The context of the calling activity
     * @param user The user to be updated with the updated information
     */
    public static void update(Context context, User user) {

    }

    /**
     * Deletes a user from the database
     * @param context The context of the calling activity
     * @param user The user to be deleted from the database
     */
    //TODO test this
    public static void delete(Context context, User user) {
        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("email", user.getEmail());
            callURL(context,
                    "https://99sepehum8.execute-api.us-east-2.amazonaws.com/prod/user/*",
                    Request.Method.DELETE,
                    userDetails);
        } catch (JSONException error) {
            Log.e(TAG, "Error creating JSONObject: " + error.getMessage());
        }
    }

    /**
     * Makes api call to url with provided parameters without a callback.
     * Volley response is a JSONObject.
     * @param context The context of the calling activity.
     * @param url The api url.
     */
    private static void callURL(Context context, String url, int requestMethod, JSONObject body) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                requestMethod,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(objectRequest);
    }

    /**
     * Makes api call to url with provided parameters and calls callback.onSuccess with the result.
     * Volley response is a JSONArray.
     * @param context The context of the calling activity.
     * @param url The api url.
     * @param body The request body to be passed to the api, used for creating and updating.
     * @param callback The callback that should receive the result.
     */
    private static void callURL(Context context, String url, int requestMethod, JSONObject body, final ObjectCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d(TAG, body.toString());
        // Request a string response from the provided URL.
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                requestMethod,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                        Log.d(TAG, "Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Network error: " + error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Access-Control-Allow-Origin", "*");
                return headers;
            }
        };
        try {
            Log.d(TAG, "Request string: " + objectRequest.getHeaders().toString());
        } catch (AuthFailureError authFailureError) {
            Log.d(TAG, authFailureError.getMessage());
        }
        // Add the request to the RequestQueue.
        queue.add(objectRequest);
    }
}