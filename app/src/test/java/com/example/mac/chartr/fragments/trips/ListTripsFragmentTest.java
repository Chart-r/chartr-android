package com.example.mac.chartr.fragments.trips;

import android.os.Bundle;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.BuildConfig;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.objects.User;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ListTripsFragmentTest {
    private CommonDependencyProvider provider = mock(CommonDependencyProvider.class);
    private AppHelper helper = mock(AppHelper.class);

    @Test
    public void testCreateViewConfirmedTrips() {
        User user = new User("email", "name",
                "02/02/2002", "+18888888888", 23.3f, 12);
        user.setUid("swag");

        when(provider.getAppHelper()).thenReturn(helper);
        when(helper.getLoggedInUser()).thenReturn(user);

        ListTripsFragment fragment = new ListTripsFragment();
        fragment.setCommonDependencyProvider(provider);

        Bundle bundle = new Bundle();
        bundle.putString("TripTypeKey", "Confirmed");

        fragment.setArguments(bundle);

        SupportFragmentTestUtil.startFragment(fragment);

        Assert.assertNotNull(fragment);
    }

    @Test
    public void testCreateViewPendingTrips() {
        User user = new User("email", "name",
                "02/02/2002", "+18888888888", 23.3f, 12);
        user.setUid("swag");

        when(provider.getAppHelper()).thenReturn(helper);
        when(helper.getLoggedInUser()).thenReturn(user);

        ListTripsFragment fragment = new ListTripsFragment();
        fragment.setCommonDependencyProvider(provider);

        Bundle bundle = new Bundle();
        bundle.putString("TripTypeKey", "Pending");

        fragment.setArguments(bundle);

        SupportFragmentTestUtil.startFragment(fragment);

        Assert.assertNotNull(fragment);
    }

    @Test
    public void testCreateViewPostedTrips() {
        User user = new User("email", "name",
                "02/02/2002", "+18888888888", 23.3f, 12);
        user.setUid("swag");

        when(provider.getAppHelper()).thenReturn(helper);
        when(helper.getLoggedInUser()).thenReturn(user);

        ListTripsFragment fragment = new ListTripsFragment();
        fragment.setCommonDependencyProvider(provider);

        Bundle bundle = new Bundle();
        bundle.putString("TripTypeKey", "Posted");

        fragment.setArguments(bundle);

        SupportFragmentTestUtil.startFragment(fragment);

        Assert.assertNotNull(fragment);
    }

}
