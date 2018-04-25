package com.example.mac.chartr;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Class used to retrieve dependencies in app activities and fragments.
 *
 * Used as a form of dependency injection such that classes can be more easily mocked.
 */
public class CommonDependencyProvider {

    private static AppHelper appHelper = null;

    public AppHelper getAppHelper() {
        return appHelper;
    }

    /**
     * Gets a AppHelper based on the context, creating a new one if one does not already exist
     *
     * @param context Context of the app
     * @return An AppHelper that has been initialized
     */
    public AppHelper getAppHelper(Context context) {
        if (appHelper == null) {
            appHelper = new AppHelper(context);
        }
        return appHelper;
    }

    /**
     * Builds an alert dialog builder based on the current context
     *
     * @param currentContext Current context of the application
     * @return An AlertDialog builder
     */
    @NonNull
    public AlertDialog.Builder getAlertDialogBuilder(Context currentContext) {
        return new AlertDialog.Builder(currentContext);
    }

    /**
     * Builds an progress dialog based on the current context
     *
     * @param currentContext Current context of the application
     * @return An ProgressDialog
     */
    @NonNull
    public ProgressDialog getProgressDialog(Context currentContext) {
        return new ProgressDialog(currentContext);
    }


}
