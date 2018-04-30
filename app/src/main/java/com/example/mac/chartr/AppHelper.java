/*
 * Copyright 2013-2017 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mac.chartr;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;
import com.example.mac.chartr.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class to do the connection with Cognito to accomplish the login and
 * sign up functionalities.
 */
public class AppHelper {
    private static final String TAG = "AppHelper";
    /**
     * Set Your User Pools region.
     * e.g. if your user pools are in US East (N Virginia) then set
     * cognitoRegion = Regions.US_EAST_1.
     */
    private final Regions cognitoRegion = Regions.US_EAST_2;
    private Map<String, String> signUpFieldsC2O;
    private CognitoUserPool userPool;
    private String user;
    private CognitoDevice newDevice;
    private CognitoUserDetails userDetails;

    // User details to display - they are the current values, including any local modification
    private boolean phoneVerified;
    private boolean emailVerified;

    private boolean phoneAvailable;
    private boolean emailAvailable;

    private Set<String> currUserAttributes;

    private User loggedInUser;

    /**
     * Construct the AppHelper using the app content
     *
     * @param context Current context from the app
     */
    public AppHelper(Context context) {
        init(context);
    }

    /**
     * Init will initialize all the needed variables in order to use AppHelper for
     * connection to Cognito.
     *
     * @param context Context of the app.
     */
    private void init(Context context) {
        setData();

        if (userPool != null) {
            return;
        }

        // Create a user pool with default ClientConfiguration
        /*
      App secret associated with your app id - if the App id does
      not have an associated App secret,
      set the App secret to null.
      e.g. clientSecret = null;
     */
        String clientSecret = "1iskopdp36s0eponkvduvfmqh0cj6aol31e74pan4bkjbn3722d1";
        /*
      Add you app id
     */
        String clientId = "20d0tqg8ulln6v52hau17ttsc2";
        /*
      Add your pool id here
     */
        String userPoolId = "us-east-2_OHZrHKLGQ";
        userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);

        phoneVerified = false;
        phoneAvailable = false;
        emailVerified = false;
        emailAvailable = false;

        currUserAttributes = new HashSet<>();

        newDevice = null;
    }

    public void setCurrUserAttributes(Set<String> currUserAttributes) {
        this.currUserAttributes = currUserAttributes;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isPhoneAvailable() {
        return phoneAvailable;
    }

    public boolean isEmailAvailable() {
        return emailAvailable;
    }

    public CognitoUserPool getPool() {
        return userPool;
    }

    public Map<String, String> getSignUpFieldsC2O() {
        return signUpFieldsC2O;
    }

    public void setUserDetails(CognitoUserDetails details) {
        userDetails = details;
        refreshWithSync();
    }

    public String getCurrUser() {
        return user;
    }

    public void setUser(String newUser) {
        user = newUser;
    }

    /**
     * Formats an exception into a proper logging format with the proper tag.
     *
     * @param exception Exception to format into a string
     * @return Result of formatting the exception
     */
    public String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.e(TAG, " -- Error: " + exception.toString());
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if (temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if (temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return formattedString;
    }

    public void newDevice(CognitoDevice device) {
        newDevice = device;
    }

    public CognitoDevice getNewDevice() {
        return newDevice;
    }

    /**
     * Sets all internal data based on the known fields
     */
    private void setData() {
        // Set attribute display sequence
        List<String> attributeDisplaySeq = new ArrayList<>();
        attributeDisplaySeq.add("name");
        attributeDisplaySeq.add("middle_name");
        attributeDisplaySeq.add("family_name");
        attributeDisplaySeq.add("nickname");
        attributeDisplaySeq.add("phone_number");
        attributeDisplaySeq.add("email");
        attributeDisplaySeq.add("birthdate");

        signUpFieldsC2O = new HashMap<>();
        signUpFieldsC2O.put("Given name", "name");
        signUpFieldsC2O.put("Family name", "family_name");
        signUpFieldsC2O.put("Nick name", "nickname");
        signUpFieldsC2O.put("Phone number", "phone_number");
        signUpFieldsC2O.put("Phone number verified", "phone_number_verified");
        signUpFieldsC2O.put("Email verified", "email_verified");
        signUpFieldsC2O.put("Email", "email");
        signUpFieldsC2O.put("Middle name", "middle_name");
        signUpFieldsC2O.put("Birthday", "birthdate");

        Map<String, String> signUpFieldsO2C = new HashMap<>();
        signUpFieldsO2C.put("given_name", "Given name");
        signUpFieldsO2C.put("family_name", "Family name");
        signUpFieldsO2C.put("nickname", "Nick name");
        signUpFieldsO2C.put("phone_number", "Phone number");
        signUpFieldsO2C.put("phone_number_verified", "Phone number verified");
        signUpFieldsO2C.put("email_verified", "Email verified");
        signUpFieldsO2C.put("email", "Email");
        signUpFieldsO2C.put("middle_name", "Middle name");

    }

    private void refreshWithSync() {
        // This will refresh the current items to display list with the attributes fetched
        // from service
        List<String> tempKeys = new ArrayList<>();
        List<String> tempValues = new ArrayList<>();

        emailVerified = false;
        phoneVerified = false;

        emailAvailable = false;
        phoneAvailable = false;

        // currDisplayedItems = new ArrayList<ItemToDisplay>();
        currUserAttributes.clear();

        for (Map.Entry<String, String> attr : userDetails.getAttributes()
                .getAttributes().entrySet()) {

            tempKeys.add(attr.getKey());
            tempValues.add(attr.getValue());

            if (attr.getKey().contains("email_verified")) {
                emailVerified = attr.getValue().contains("true");
            } else if (attr.getKey().contains("phone_number_verified")) {
                phoneVerified = attr.getValue().contains("true");
            }

            if (attr.getKey().equals("email")) {
                emailAvailable = true;
            } else if (attr.getKey().equals("phone_number")) {
                phoneAvailable = true;
            }
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}

