package com.example.mac.chartr.fragments;

import com.example.mac.chartr.AppHelper;
import com.example.mac.chartr.BuildConfig;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.objects.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SearchFragmentTest {

    private CommonDependencyProvider provider;
    private AppHelper helper;
    private User loggedInUser;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
        helper = mock(AppHelper.class);

        loggedInUser = new User("email", "name", "02/02/2002",
                "+19999999999", 2.2f, 200);

        when(provider.getAppHelper()).thenReturn(helper);
        when(helper.getLoggedInUser()).thenReturn(loggedInUser);
    }

    /**
     * There are issues with Robolectric that's preventing the nested inflation of
     * fragments that is currently being done. Thus, for now, this will need to be a stub
     */
    @Test
    public void stub() {
        SearchFragment fragment = new SearchFragment();
        fragment.setCommonDependencyProvider(provider);

//        SupportFragmentTestUtil.startFragment(fragment);

        assertNotNull(fragment);
    }
}
