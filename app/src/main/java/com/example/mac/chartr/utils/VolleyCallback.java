package com.example.mac.chartr.utils;

import org.json.JSONArray;

/**
 * Interface for callbacks when using JsonArrayRequest from the volley library.
 */

public interface VolleyCallback {
    void onSuccess(JSONArray result);

}
