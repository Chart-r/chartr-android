package com.example.mac.chartr.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.mac.chartr.CommonDependencyProvider;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Michael Rush on 3/13/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest {
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }

    @Test
    public void testShowDialogMessage() {
        RegisterActivity activity = Robolectric.setupActivity(RegisterActivity.class);
        activity.setCommonDependencyProvider(provider);

        AlertDialog.Builder builder = mock(AlertDialog.Builder.class);
        AlertDialog dialog = mock(AlertDialog.class);

        when(provider.getAlertDialogBuilder(any(Context.class))).thenReturn(builder);
        when(builder.create()).thenReturn(dialog);
        when(builder.setTitle(any(String.class))).thenReturn(builder);
        when(builder.setMessage(any(String.class))).thenReturn(builder);
        when(builder.setNeutralButton(any(String.class), any(DialogInterface.OnClickListener.class))).thenReturn(builder);


        activity.showDialogMessage("This is a test", "Body body body", false);

        verify(dialog, times(1)).show();
    }

    @Test
    public void onActivityResultTest() {
        RegisterActivity activity = Robolectric.setupActivity(RegisterActivity.class);
        Intent intent = mock(Intent.class);
        when(intent.getStringExtra("name")).thenReturn("iLove2");

        activity.onActivityResult(10, Activity.RESULT_OK, intent);
        Assert.assertTrue(activity.isFinishing());

    }
}

