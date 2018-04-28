package com.example.mac.chartr.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;

public class ConfirmRegisterActivity extends AppCompatActivity {
    private static final String TAG = ConfirmRegisterActivity.class.getSimpleName();

    private CommonDependencyProvider provider;

    private EditText username;
    private EditText confCode;

    private Button confirm;
    private TextView reqCode;
    private String userName;
    private AlertDialog userDialog;
    GenericHandler confHandler = new GenericHandler() {
        /**
         * Handler implementation that specifies behavior on the success condition
         *
         * Specifically used to confirm that a user has been accepted for a ride.
         */
        @Override
        public void onSuccess() {
            showDialogMessage("Success!", userName + " has been confirmed!", true);
        }

        /**
         * Handler implementation that specifies behavior on the failure condition
         *
         * Specifically used to specify that the user could not be accepted for the ride
         *
         * @param exception Exception that may have caused the failure to occur
         */
        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText("Confirmation failed!");
            username.setBackground(getDrawable(R.drawable.text_border_error));

            label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
            label.setText("Confirmation failed!");
            confCode.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage("Confirmation failed",
                    provider.getAppHelper().formatException(exception), false);
        }
    };
    VerificationHandler resendConfCodeHandler = new VerificationHandler() {

        /**
         * Handler implementation that specifies behavior on the success condition
         *
         * Specifically confirms the account creation
         */
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            TextView mainTitle = (TextView) findViewById(R.id.textViewConfirmTitle);
            mainTitle.setText("Confirm your account");
            confCode = (EditText) findViewById(R.id.editTextConfirmCode);
            confCode.requestFocus();
            showDialogMessage("Confirmation code sent.", "Code sent to "
                    + cognitoUserCodeDeliveryDetails.getDestination() + " via "
                    + cognitoUserCodeDeliveryDetails.getDeliveryMedium() + ".", false);
        }

        /**
         * Handler implementation that specifies behavior on the failure condition
         *
         * Specifically responses to failure events from the sign up process
         *
         * @param exception Exception that may have caused the failure to occur
         */
        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
            label.setText("Confirmation code resend failed");
            username.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Confirmation code request has failed",
                    provider.getAppHelper().formatException(exception), false);
        }
    };

    /**
     * Inherited method from the Activity class that is generated when the
     * activity is first created.
     *
     * @param savedInstanceState An instance of the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setCommonDependencyProvider(new CommonDependencyProvider());
        init();
    }

    /**
     * Initializes the needed fields, attaching listeners for each event
     */
    protected void init() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("name")) {
                extractFromExtras(extras);
            } else {
                TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                screenSubtext.setText("Request for a confirmation code or confirm"
                        + " with the code you already have.");
            }
        }

        username = (EditText) findViewById(R.id.editTextConfirmUserId);
        username.addTextChangedListener(new TextWatcher() {
            /**
             * Handles events on the username field before the change is made to the field.
             * Indicates that the user has started typing
             *
             * @param s Character sequence being inputted
             * @param start Start of the sequence
             * @param count Count of how many characters
             * @param after How many characters will be in the box afterward
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdLabel);
                    label.setText(username.getHint());
                    username.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            /**
             * Handles events on the username field as the change is made to the field.
             * Indicates that the user has changed the text
             *
             * @param s Sequence of the change
             * @param start Start index
             * @param before Before index
             * @param count Count of characters
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdMessage);
                label.setText(" ");
            }

            /**
             * Called after the text has been entered
             * @param s Editable object that has the users input into the text box
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmUserIdLabel);
                    label.setText("");
                }
            }
        });

        confCode = (EditText) findViewById(R.id.editTextConfirmCode);
        confCode.addTextChangedListener(new TextWatcher() {
            /**
             * Handles events on the confirmation code field before the change is made to the field.
             * Indicates that the user has started typing
             *
             * @param s Character sequence being inputted
             * @param start Start of the sequence
             * @param count Count of how many characters
             * @param after How many characters will be in the box afterward
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
                    label.setText(confCode.getHint());
                    confCode.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            /**
             * Handles events on the username field as the change is made to the field.
             * Indicates that the user has changed the text
             *
             * @param s Sequence of the change
             * @param start Start index
             * @param before Before index
             * @param count Count of characters
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
                label.setText(" ");
            }

            /**
             * Called after the text has been entered
             * @param s Editable object that has the users input into the text box
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
                    label.setText("");
                }
            }
        });

        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(v -> sendConfCode());

        reqCode = (TextView) findViewById(R.id.resend_confirm_req);
        reqCode.setOnClickListener(v -> reqConfCode());
    }

    /**
     * Extracts known parameters from the bundle that is passed into the activity
     *
     * @param extras Bundle of parameters
     */
    protected void extractFromExtras(Bundle extras) {
        userName = extras.getString("name");
        username = (EditText) findViewById(R.id.editTextConfirmUserId);
        username.setText(userName);

        confCode = (EditText) findViewById(R.id.editTextConfirmCode);
        confCode.requestFocus();

        if (extras.containsKey("destination")) {
            String dest = extras.getString("destination");
            String delMed = extras.getString("deliveryMed");

            TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
            if (dest != null && delMed != null && dest.length() > 0 && delMed.length() > 0) {
                screenSubtext.setText("A confirmation code was sent to " + dest + " via " + delMed);
            } else {
                screenSubtext.setText("A confirmation code was sent");
            }
        }


    }

    /**
     * Sets the common dependency provider for tests that will mock it
     *
     * @param provider An initialized CommonDependencyProvider
     */
    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    private void sendConfCode() {
        userName = username.getText().toString();
        String confirmCode = confCode.getText().toString();

        if (userName == null || userName.length() < 1) {
            setConfirmCodeMessage(username);
            return;
        }

        if (confirmCode == null || confirmCode.length() < 1) {
            setConfirmCodeMessage(confCode);
            return;
        }

        provider.getAppHelper().getPool().getUser(userName).confirmSignUpInBackground(confirmCode,
                true, confHandler);
    }

    /**
     * Set the text for the field textViewConfirmCodeMessage in the ConfirmRegister Layout
     *
     * @param et is and EditText
     */
    protected void setConfirmCodeMessage(EditText et) {
        TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
        label.setText(et.getHint() + " cannot be empty");
        et.setBackground(getDrawable(R.drawable.text_border_error));
        return;
    }

    /**
     * Requests a confirmation code
     */
    private void reqConfCode() {
        userName = username.getText().toString();
        if (userName == null || userName.length() < 1) {
            setConfirmCodeMessage(username);
            return;
        }
        provider.getAppHelper().getPool().getUser(userName)
                .resendConfirmationCodeInBackground(resendConfCodeHandler);

    }

    /**
     * Shows a dialog message to the user with the following parameters.
     *
     * @param title The title of the message
     * @param body The body of the message
     * @param exitActivity The activity to call on exit.
     */
    protected void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = provider.getAlertDialogBuilder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK",
                (dialog, which) -> {
                    try {
                        userDialog.dismiss();
                        if (exitActivity) {
                            exit();
                        }
                    } catch (Exception e) {
                        exit();
                    }
                });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Intent intent = new Intent();
        if (userName == null) {
            userName = "";
        }
        intent.putExtra("name", userName);
        setResult(RESULT_OK, intent);
        finish();
    }

}
