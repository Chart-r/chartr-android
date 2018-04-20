package com.example.mac.chartr.fragments;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.BuildConfig;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 3/13/18.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RequestsFragmentTest {

    private CommonDependencyProvider provider = mock(CommonDependencyProvider.class);
    private AppHelper appHelper = mock(AppHelper.class);

    @Test
    public void shouldNotBeNull() throws Exception {
        RequestsFragment fragment = new RequestsFragment();

        User user = new User("email", "name", "birthdate",
                "phone", 4.5f, 50);
        user.setUid("swag");

        when(provider.getAppHelper()).thenReturn(appHelper);
        when(appHelper.getLoggedInUser()).thenReturn(user);

        fragment.setCommonDependencyProvider(provider);

        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment);
    }
}
