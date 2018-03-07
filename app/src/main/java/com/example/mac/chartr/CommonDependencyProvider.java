package com.example.mac.chartr;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Class used to retrieve dependencies in app activities and fragments.
 *
 * Used as a form of dependency injection such that classes can be more easily mocked.
 */
public class CommonDependencyProvider {
    @NonNull
    public AlertDialog.Builder getAlertDialogBuilder(Context currentContext) {
        return new AlertDialog.Builder(currentContext);
    }

    @NonNull
    public ProgressDialog getProgressDialog(Context currentContext) {
        return new ProgressDialog(currentContext);
    }


}
