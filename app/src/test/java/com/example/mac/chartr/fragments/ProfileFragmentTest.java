package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 3/7/18.
 * <p>
 * Used to test the ProfileFragment
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProfileFragment.class)
public class ProfileFragmentTest {
    @Test
    public void testOnCreateView() {
        ProfileFragment victim = PowerMockito.mock(ProfileFragment.class);

        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        Bundle bundle = mock(Bundle.class);
        AppCompatActivity appCompatActivity = mock(AppCompatActivity.class);
        ActionBar actionBar = mock(ActionBar.class);
        View view = mock(View.class);
        Button button = mock(Button.class);

        PowerMockito.when(victim.getActivity()).thenReturn(appCompatActivity);
        when(appCompatActivity.getSupportActionBar()).thenReturn(actionBar);
        when(appCompatActivity.findViewById(any(int.class))).thenReturn(button);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class)))
                .thenReturn(view);
        when(view.findViewById(any(int.class))).thenReturn(button);
        when(victim.onCreateView(inflater, viewGroup, bundle)).thenCallRealMethod();

        View root = victim.onCreateView(inflater, viewGroup, bundle);

        assertNotNull(root);
    }
}
