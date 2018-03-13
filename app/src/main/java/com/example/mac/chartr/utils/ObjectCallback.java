package com.example.mac.chartr.utils;

import org.json.JSONObject;

/**
 * Interface for callbacks when using JsonObjectRequest from the volley library.
 */

public interface ObjectCallback {
    void onSuccess(JSONObject result);
}
