package com.example.mac.chartr.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.User;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that allows users to log into the app from the first page that is presented in the app.
 * Interfaces with Cognito to verify the login.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private CommonDependencyProvider provider;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;

    // Screen fields
    private EditText inUsername;
    private EditText inPassword;
    // Callbacks
    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            closeWaitDialog();
            showDialogMessage("Password successfully changed!", "");
            inPassword.setText("");
            inPassword.requestFocus();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            closeWaitDialog();
            //getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            showDialogMessage("Forgot password failed", provider.getAppHelper().
                    formatException(e));
        }
    };
    private ForgotPasswordContinuation forgotPasswordContinuation;
    // User Details
    private String username;
    private String password;
    //
    private final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.d(TAG, " -- Auth Success");
            provider.getAppHelper().newDevice(device);
            provider.getAppHelper().setLoggedInUser(new User(username, "Person", (float) 4.5));
            closeWaitDialog();
            getAndLaunchUser();
            launchUser();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation,
                                             String username) {
            closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation mfaContinuation) {
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inPassword.setBackground(getDrawable(R.drawable.text_border_error));

            label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage("Sign-in failed", provider.getAppHelper().formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                // This is the first sign-in attempt for an admin created user
                closeWaitDialog();
            }
        }
    };

    /**
     * Method inherited from the Activity class that defines behavior when the activity is created
     *
     * @param savedInstanceState Bundle of the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setCommonDependencyProvider(new CommonDependencyProvider());

        // Initialize application
        provider.getAppHelper(getApplicationContext());
        initApp();
        findCurrent();
    }

    /**
     * Allows the common dependency provider to be set to a mock for testing purposes
     *
     * @param provider An initialized or mocked CommonDependencyProvider
     */
    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    /**
     * Captures results from the activity and forwards the intent data and result code
     * to the appropriate method
     *
     * @param requestCode Code used to decide which method to call
     * @param resultCode Result code from the activity invocation
     * @param data Intent data from the activity invocation
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                // Register user
                registerUser(resultCode, data);
                break;
            case 2:
                // Confirm register user
                confirmRegisterUser(resultCode, data);
                break;
            case 3:
                // Forgot password
                forgotPassword(resultCode, data);
                break;
            case 4:
                // User
                userBack(resultCode, data);
                break;
        }
    }

    /**
     * Consume results of activity of the user going back
     *
     * Needs further clarification on exact functions.
     *
     * @param resultCode Result from the registration activity
     * @param data The Intent data
     */
    private void userBack(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            clearInput();
            String name = data.getStringExtra("TODO");
            if (name != null) {
                if (!name.isEmpty()) {
                    name.equals("exit");
                    onBackPressed();
                }
            }
        }
    }

    /**
     * Allows users to reset their password be capturing the intent data from the
     * password reset.
     *
     * @param resultCode From the password reset
     * @param data Intent data from the password reset
     */
    protected void forgotPassword(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String newPass = data.getStringExtra("newPass");
            String code = data.getStringExtra("code");
            if (newPass != null && code != null) {
                if (!newPass.isEmpty() && !code.isEmpty()) {
                    showWaitDialog("Setting new password...");
                    forgotPasswordContinuation.setPassword(newPass);
                    forgotPasswordContinuation.setVerificationCode(code);
                    forgotPasswordContinuation.continueTask();
                }
            }
        }
    }

    /**
     * This method can be used to fill out the username of a user who has registered
     * @param resultCode Result from the registration activity
     * @param data Intent data from the registration activity
     */
    protected void confirmRegisterUser(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            if (!name.isEmpty()) {
                inUsername.setText(name);
                inPassword.setText("");
                inPassword.requestFocus();
            }
        }
    }

    /**
     * Registers the user in the based on if all of the results are OK
     *
     * @param resultCode Result code indicating the status of the forms
     * @param data Data Intent to get the information from
     */
    protected void registerUser(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            if (!name.isEmpty()) {
                inUsername.setText(name);
                inPassword.setText("");
                inPassword.requestFocus();
            }
            String userPasswd = data.getStringExtra("password");
            if (!userPasswd.isEmpty()) {
                inPassword.setText(userPasswd);
            }
            if (!name.isEmpty() && !userPasswd.isEmpty()) {
                // We have the user details, so sign in!
                username = name;
                password = userPasswd;
                provider.getAppHelper().getPool().getUser(username)
                        .getSessionInBackground(authenticationHandler);
            }
        }
    }

    /**
     * Starts up the sign-up activity to allow the user to sign up for the service
     *
     * @param view Current view
     */
    public void signUp(View view) {
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerActivity, 1);
    }


    /**
     * Login if a user is already present. Simple matter of getting the username and password
     * and verifying it through the AppHelper
     *
     * @param view Current view
     */
    public void logIn(View view) {
        username = inUsername.getText().toString();
        if (username == null || username.length() < 1) {
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText(inUsername.getHint() + " cannot be empty");
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        provider.getAppHelper().setUser(username);

        password = inPassword.getText().toString();
        if (password == null || password.length() < 1) {
            TextView label = findViewById(R.id.textViewUserPasswordMessage);
            label.setText(inPassword.getHint() + " cannot be empty");
            inPassword.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        showWaitDialog("Signing in...");
        provider.getAppHelper().getPool().getUser(username)
                .getSessionInBackground(authenticationHandler);
    }

    // Forgot password processing

    /**
     * Stub currently, used in the future to allow users to reset their passwords
     *
     * @param view Current view
     */
    public void forgotPassword(View view) {
    }

    /**
     * Calls api to get a user details from dynamoDB with given username then launches the user.
     */
    private void getAndLaunchUser() {
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<User> call;
        call = apiInterface.getUserFromEmail(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (code == 200) {
                    Log.d(TAG, "User gotten successfully.");
                    provider.getAppHelper().setLoggedInUser(response.body());
                } else {
                    Log.d(TAG, "Retrofit failed to get user, response code: "
                            + response.code());
                }
                launchUser();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to get user.");
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                launchUser();
                call.cancel();
            }
        });
    }

    private void launchUser() {
        Intent userActivity = new Intent(this, MainActivity.class);
        userActivity.putExtra("name", username);
        userActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(userActivity, 4);
    }

    private void findCurrent() {
        CognitoUser user = provider.getAppHelper().getPool().getCurrentUser();
        username = user.getUserId();
        if (username != null) {
            provider.getAppHelper().setUser(username);
            inUsername.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            this.username = username;
            provider.getAppHelper().setUser(username);
        }
        if (this.password == null) {
            inUsername.setText(username);
            password = inPassword.getText().toString();
            if (password == null) {
                TextView label = findViewById(R.id.textViewUserPasswordMessage);
                label.setText(inPassword.getHint() + " enter password");
                inPassword.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }

            if (password.length() < 1) {
                TextView label = findViewById(R.id.textViewUserPasswordMessage);
                label.setText(inPassword.getHint() + " enter password");
                inPassword.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username,
                password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    // initialize app
    private void initApp() {
        inUsername = findViewById(R.id.editTextUserId);
        inUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserIdLabel);
                    label.setText(R.string.Username);
                    inUsername.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = findViewById(R.id.textViewUserIdMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserIdLabel);
                    label.setText("");
                }
            }
        });

        inPassword = findViewById(R.id.editTextUserPassword);
        inPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserPasswordLabel);
                    label.setText(R.string.Password);
                    inPassword.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = findViewById(R.id.textViewUserPasswordMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserPasswordLabel);
                    label.setText("");
                }
            }
        });
    }

    private void clearInput() {
        if (inUsername == null) {
            inUsername = findViewById(R.id.editTextUserId);
        }

        if (inPassword == null) {
            inPassword = findViewById(R.id.editTextUserPassword);
        }

        inUsername.setText("");
        inUsername.requestFocus();
        inUsername.setBackground(getDrawable(R.drawable.text_border_selector));
        inPassword.setText("");
        inPassword.setBackground(getDrawable(R.drawable.text_border_selector));
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = provider.getProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    /**
     * Shows a message dialog to the user with the following parameters
     *
     * @param title Title of the message
     * @param body Body of the message
     */
    protected void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = provider.getAlertDialogBuilder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK",
                (dialog, which) -> {
                    try {
                        userDialog.dismiss();
                    } catch (Exception e) {
                        //
                    }
                });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }
}
