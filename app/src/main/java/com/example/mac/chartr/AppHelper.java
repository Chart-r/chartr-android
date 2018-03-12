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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.AttributeType;
import com.example.mac.chartr.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppHelper {
    private final String TAG = "AppHelper";
    // App settings
    private List<String> attributeDisplaySeq;
    private Map<String, String> signUpFieldsC2O;
    private Map<String, String> signUpFieldsO2C;
    private CognitoUserPool userPool;
    private String user;
    private CognitoDevice newDevice;
    private CognitoUserAttributes attributesChanged;
    private List<AttributeType> attributesToDelete;
    private  int itemCount;
    private int trustedDevicesCount;
    private List<CognitoDevice> deviceDetails;
    private CognitoDevice thisDevice;
    private boolean thisDeviceTrustState;
    private Map<String, String> firstTimeLogInUserAttributes;
    private List<String> firstTimeLogInRequiredAttributes;
    private int firstTimeLogInItemsCount;
    private Map<String, String> firstTimeLogInUpDatedAttributes;
    private String firstTimeLoginNewPassword;
    private List<String> mfaAllOptionsCode;

    /**
     * Add your pool id here
     */
    private final String userPoolId = "us-east-2_OHZrHKLGQ";

    /**
     * Add you app id
     */
    private final String clientId = "20d0tqg8ulln6v52hau17ttsc2";

    /**
     * App secret associated with your app id - if the App id does not have an associated App secret,
     * set the App secret to null.
     * e.g. clientSecret = null;
     */
    private final String clientSecret = "1iskopdp36s0eponkvduvfmqh0cj6aol31e74pan4bkjbn3722d1";

    /**
     * Set Your User Pools region.
     * e.g. if your user pools are in US East (N Virginia) then set cognitoRegion = Regions.US_EAST_1.
     */
    private final Regions cognitoRegion = Regions.US_EAST_2;

    // User details from the service
    private CognitoUserSession currSession;
    private CognitoUserDetails userDetails;

    // User details to display - they are the current values, including any local modification
    private boolean phoneVerified;
    private boolean emailVerified;

    private boolean phoneAvailable;
    private boolean emailAvailable;

    private Set<String> currUserAttributes;

    private User loggedInUser;

    public AppHelper(Context context) {
        init(context);
    }

    public void init(Context context) {
        setData();

        if (userPool != null) {
            return;
        }

        // Create a user pool with default ClientConfiguration
        userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);

        phoneVerified = false;
        phoneAvailable = false;
        emailVerified = false;
        emailAvailable = false;

        currUserAttributes = new HashSet<>();
        firstTimeLogInUpDatedAttributes= new HashMap<>();

        newDevice = null;
        thisDevice = null;
        thisDeviceTrustState = false;
    }

    public CognitoUserPool getPool() {
        return userPool;
    }

    public Map<String, String> getSignUpFieldsC2O() {
        return signUpFieldsC2O;
    }

    public Map<String, String> getSignUpFieldsO2C() {
        return signUpFieldsO2C;
    }

    public List<String> getAttributeDisplaySeq() {
        return attributeDisplaySeq;
    }

    public void setCurrSession(CognitoUserSession session) {
        currSession = session;
    }

    public CognitoUserSession getCurrSession() {
        return currSession;
    }

    public void setUserDetails(CognitoUserDetails details) {
        userDetails = details;
        refreshWithSync();
    }

    public CognitoUserDetails getUserDetails() {
        return userDetails;
    }

    public String getCurrUser() {
        return user;
    }

    public void setUser(String newUser) {
        user = newUser;
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

    public void setPhoneVerified(boolean phoneVerif) {
        phoneVerified = phoneVerif;
    }

    public void setEmailVerified(boolean emailVerif) {
        emailVerified = emailVerif;
    }

    public void setPhoneAvailable(boolean phoneAvail) {
        phoneAvailable = phoneAvail;
    }

    public void setEmailAvailable(boolean emailAvail) {
        emailAvailable = emailAvail;
    }

    public void clearCurrUserAttributes() {
        currUserAttributes.clear();
    }

    public void addCurrUserattribute(String attribute) {
        currUserAttributes.add(attribute);
    }

    public List<String> getNewAvailableOptions() {
        List<String> newOption = new ArrayList<String>();
        for(String attribute : attributeDisplaySeq) {
            if(!(currUserAttributes.contains(attribute))) {
                newOption.add(attribute);
            }
        }
        return  newOption;
    }

    public String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.e(TAG, " -- Error: "+exception.toString());
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if(temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if(temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return  formattedString;
    }

    public   int getItemCount() {
        return itemCount;
    }

    public int getDevicesCount() {
        return trustedDevicesCount;
    }

    public int getFirstTimeLogInItemsCount() {
        return  firstTimeLogInItemsCount;
    }

    public Map<String, String> getUserAttributesForFirstTimeLogin() {
        return firstTimeLogInUpDatedAttributes;
    }

    public void setPasswordForFirstTimeLogin(String password) {
        firstTimeLoginNewPassword = password;
    }

    public String getPasswordForFirstTimeLogin() {
        return firstTimeLoginNewPassword;
    }

    public void newDevice(CognitoDevice device) {
        newDevice = device;
    }

    public CognitoDevice getDeviceDetail(int position) {
        if (position <= trustedDevicesCount) {
            return deviceDetails.get(position);
        } else {
            return null;
        }
    }

    public List<String> getAllMfaOptions() {
        return mfaAllOptionsCode;
    }

    public String getMfaOptionCode(int position) {
        return mfaAllOptionsCode.get(position);
    }

    public CognitoDevice getNewDevice() {
        return newDevice;
    }

    public CognitoDevice getThisDevice() {
        return thisDevice;
    }

    public void setThisDevice(CognitoDevice device) {
        thisDevice = device;
    }

    public boolean getThisDeviceTrustState() {
        return thisDeviceTrustState;
    }

    private void setData() {
        // Set attribute display sequence
        attributeDisplaySeq = new ArrayList<String>();
        attributeDisplaySeq.add("name");
        attributeDisplaySeq.add("middle_name");
        attributeDisplaySeq.add("family_name");
        attributeDisplaySeq.add("nickname");
        attributeDisplaySeq.add("phone_number");
        attributeDisplaySeq.add("email");
        attributeDisplaySeq.add("birthdate");

        signUpFieldsC2O = new HashMap<String, String>();
        signUpFieldsC2O.put("Given name", "name");
        signUpFieldsC2O.put("Family name", "family_name");
        signUpFieldsC2O.put("Nick name", "nickname");
        signUpFieldsC2O.put("Phone number", "phone_number");
        signUpFieldsC2O.put("Phone number verified", "phone_number_verified");
        signUpFieldsC2O.put("Email verified", "email_verified");
        signUpFieldsC2O.put("Email","email");
        signUpFieldsC2O.put("Middle name","middle_name");
        signUpFieldsC2O.put("Birthday", "birthdate");

        signUpFieldsO2C = new HashMap<String, String>();
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
        // This will refresh the current items to display list with the attributes fetched from service
        List<String> tempKeys = new ArrayList<>();
        List<String> tempValues = new ArrayList<>();

        emailVerified = false;
        phoneVerified = false;

        emailAvailable = false;
        phoneAvailable = false;

       // currDisplayedItems = new ArrayList<ItemToDisplay>();
        currUserAttributes.clear();
        itemCount = 0;

        for(Map.Entry<String, String> attr: userDetails.getAttributes().getAttributes().entrySet()) {

            tempKeys.add(attr.getKey());
            tempValues.add(attr.getValue());

            if(attr.getKey().contains("email_verified")) {
                emailVerified = attr.getValue().contains("true");
            }
            else if(attr.getKey().contains("phone_number_verified")) {
                phoneVerified = attr.getValue().contains("true");
            }

            if(attr.getKey().equals("email")) {
                emailAvailable = true;
            }
            else if(attr.getKey().equals("phone_number")) {
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

