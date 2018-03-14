package com.example.mac.chartr.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.app.Activity.RESULT_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Michael Rush on 3/8/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class ConfirmRegisterActivityTest {
    private CommonDependencyProvider provider;

    @Before
    public void setup() {
        provider = mock(CommonDependencyProvider.class);
    }

    @Test
    public void testShowDialogMessage() {
        ConfirmRegisterActivity activity = Robolectric.setupActivity(ConfirmRegisterActivity.class);
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
    public void setConfirmCodeMessageTest(){
        ConfirmRegisterActivity activity = Robolectric.setupActivity(ConfirmRegisterActivity.class);
        EditText username = (EditText) activity.findViewById(R.id.editTextConfirmUserId);
        activity.setConfirmCodeMessage(username);


        TextView label = (TextView) activity.findViewById(R.id.textViewConfirmCodeMessage);
        String str = label.getText().toString();
        Assert.assertEquals("Username cannot be empty", str);
    }

    @Test
    public void extractFromExtrasTest(){
        ConfirmRegisterActivity activity = Robolectric.setupActivity(ConfirmRegisterActivity.class);

        Bundle extras = new Bundle();
        extras.putString("name", "Michael");
        extras.putString("destination","Chicago");
        extras.putString("deliveryMed","dest");
        activity.extractFromExtras(extras);
        EditText username = (EditText) activity.findViewById(R.id.editTextConfirmUserId);
        Assert.assertEquals("Michael", username.getText().toString());
        TextView screenSubtext = (TextView) activity.findViewById(R.id.textViewConfirmSubtext_1);
        Assert.assertEquals("A confirmation code was sent to Chicago via dest", screenSubtext.getText().toString());

    }

}
