package com.example.mac.chartr.objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mac.chartr.utils.VolleyCallback;

import org.json.JSONArray;

/**
 * Class for making trip related API calls, result of API calls is stored in the output variable
 */

public class TripInvoker {
    private static final String TAG = TripInvoker.class.getSimpleName();


    /**
     * Gets trips for a given driver username.
     * @param context The context of the calling activity
     * @param username The username of the trip driver
     */
    public static void get(Context context, String username, VolleyCallback callback) {
        callURL(context, "https://99sepehum8.execute-api.us-east-2.amazonaws.com/prod/trip/*?" + username, null, callback);
    }

    public static void post(Context context, Trip trip) {

    }

    public static void update(Context context, Trip trip) {

    }

    public static void delete(Context context, Trip trip) {

    }

    private static void callURL(Context context, String url) {

    }

    /**
     * Makes api call to url with provided parameters and calls callback.onSuccess with the result.
     * @param context The context of the calling activity.
     * @param url The api url.
     * @param params The parameters to pass to the api.
     * @param callback The callback that should receive the result.
     */
    private static void callURL(Context context, String url, JSONArray params, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                        Log.d(TAG, "Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
