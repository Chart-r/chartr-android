package com.example.mac.chartr.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.app.Activity.RESULT_OK;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 3/7/18.
 */

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }

    @Test
    public void testShowDialogMessage() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        activity.setCommonDependencyProvider(provider);

        AlertDialog.Builder builder = mock(AlertDialog.Builder.class);
        AlertDialog dialog = mock(AlertDialog.class);

        when(provider.getAlertDialogBuilder(any(Context.class))).thenReturn(builder);
        when(builder.create()).thenReturn(dialog);
        when(builder.setTitle(any(String.class))).thenReturn(builder);
        when(builder.setMessage(any(String.class))).thenReturn(builder);
        when(builder.setNeutralButton(any(String.class),
                any(DialogInterface.OnClickListener.class))).thenReturn(builder);


        activity.showDialogMessage("This is a test", "Body body body");

        verify(dialog, times(1)).show();
    }

    @Test
    public void testOnActivityResult() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);

        EditText username = activity.findViewById(R.id.editTextUserId);
        EditText password = activity.findViewById(R.id.editTextUserPassword);
    }

    @Test
    public void testRegisterUser() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);

        EditText username = activity.findViewById(R.id.editTextUserId);
        EditText password = activity.findViewById(R.id.editTextUserPassword);

        Intent intent = mock(Intent.class);
        when(intent.getStringExtra("name")).thenReturn("Random");
        when(intent.getStringExtra("password")).thenReturn("rnd123");

        activity.registerUser(RESULT_OK, intent);

        Assert.assertEquals("Random", username.getText().toString());
        Assert.assertEquals("rnd123", password.getText().toString());
    }

    @Test
    public void testConfirmRegisterUser() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);

        EditText username = activity.findViewById(R.id.editTextUserId);
        EditText password = activity.findViewById(R.id.editTextUserPassword);

        Intent intent = mock(Intent.class);
        when(intent.getStringExtra("name")).thenReturn("Random");

        activity.confirmRegisterUser(RESULT_OK, intent);

        Assert.assertEquals("Random", username.getText().toString());
        Assert.assertEquals("", password.getText().toString());
    }

    @Test
    public void testForgotPassword() {
        LoginActivity activity = Robolectric.setupActivity(LoginActivity.class);
        activity.setCommonDependencyProvider(provider);

        ProgressDialog dialog = mock(ProgressDialog.class);
        when(provider.getProgressDialog(any(Context.class))).thenReturn(dialog);

        EditText username = activity.findViewById(R.id.editTextUserId);
        EditText password = activity.findViewById(R.id.editTextUserPassword);

        Intent intent = mock(Intent.class);
        when(intent.getStringExtra("newPass")).thenReturn("iLove2");
        when(intent.getStringExtra("code")).thenReturn("12345");

        try {
            activity.forgotPassword(RESULT_OK, intent);
        } catch (NullPointerException e) {
            //Normal, as the continuation isn't defined
        }

        verify(dialog, times(1)).show();
    }
}
