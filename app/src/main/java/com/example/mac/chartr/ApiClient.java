package com.example.mac.chartr;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Client for making api calls using Retrofit.
 */
public class ApiClient {

    private static final String ROOT_URL =
            "https://6zvb3ngfyk.execute-api.us-east-2.amazonaws.com/prod/";

    private static Retrofit getClientInstance() {
        return new Retrofit.Builder().baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    /**
     * Returns an instance of the Retrofit api based on the ApiInterface class
     *
     * @return Instance of an API interface
     */
    public static ApiInterface getApiInstance() {
        return getClientInstance().create(ApiInterface.class);
    }

}
