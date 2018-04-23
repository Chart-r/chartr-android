package com.example.mac.chartr.objects;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.example.mac.chartr.AppHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 4/2/18.
 *
 */

public class AppHelperTest {

    @Test
    public void testRefresh() {
        AppHelper appHelper = mock(AppHelper.class);
        CognitoUserDetails userDetails = mock(CognitoUserDetails.class);
        CognitoUserAttributes attributes = mock(CognitoUserAttributes.class);

        doCallRealMethod().when(appHelper).setUserDetails(any(CognitoUserDetails.class));
        doCallRealMethod().when(appHelper).setCurrUserAttributes(any(Set.class));
        doCallRealMethod().when(appHelper).isEmailAvailable();
        doCallRealMethod().when(appHelper).isPhoneAvailable();
        doCallRealMethod().when(appHelper).isEmailVerified();
        doCallRealMethod().when(appHelper).isPhoneVerified();

        Map<String, String> map = new HashMap<>();
        map.put("email_verified", "true");
        map.put("phone_number_verified", "true");
        map.put("email", "true");
        map.put("phone_number", "true");

        when(userDetails.getAttributes()).thenReturn(attributes);
        when(attributes.getAttributes()).thenReturn(map);

        appHelper.setCurrUserAttributes(new HashSet<String>());
        appHelper.setUserDetails(userDetails);

        Assert.assertTrue(appHelper.isEmailAvailable());
        Assert.assertTrue(appHelper.isEmailVerified());
        Assert.assertTrue(appHelper.isPhoneAvailable());
        Assert.assertTrue(appHelper.isPhoneVerified());
    }

    @Test
    public void testFormatException() {
        AppHelper appHelper = mock(AppHelper.class);
        when(appHelper.formatException(any(Exception.class))).thenCallRealMethod();

        String ret = appHelper.formatException(new Exception("This SUCKS"));

        assertTrue(ret.contains("This SUCKS"));
    }

}
