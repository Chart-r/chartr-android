package com.example.mac.chartr.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText email;
    private EditText password;
    private EditText givenName;
    private EditText birthday;
    private EditText phone;

    private Button signUp;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    private String emailInput;
    private String userPasswd;
    private User user;

    private CommonDependencyProvider provider;

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Add user to DynamoDB
            ApiInterface apiInterface = ApiClient.getApiInstance();
            callPostUserApi(apiInterface, user);

            closeWaitDialog();

            // Check signUpConfirmationState to see if the user is already confirmed
            if (signUpConfirmationState) {
                // User is already confirmed
                showDialogMessage("Sign up successful!", emailInput
                        + " has been Confirmed", true);
            } else {
                // User is not confirmed
                showDialogMessage("Confirm Account",
                        "An email with a confirmation link has been sent to " + emailInput,
                        true);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
            label.setText("Sign up failed");
            email.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Sign up failed", provider.getAppHelper()
                    .formatException(exception), false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCommonDependencyProvider(new CommonDependencyProvider());
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get back to main screen
            String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }
        }

        init();
    }

    /**
     * Calls api to post a user.
     *
     * @param apiInterface Contains api calls
     * @param user the user to be posted, should contain email, name, birth date, and phone number
     */
    private void callPostUserApi(ApiInterface apiInterface, User user) {
        Call<Void> call;
        call = apiInterface.postUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();
                if (code == 200) {
                    Log.d(TAG, "User posted successfully.");
                } else {
                    Log.d(TAG, "Retrofit failed to post user, response code: "
                            + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to post user, no response.");
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });
    }

    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    private void init() {
        initEmail();
        initPassword();
        initGivenName();
        initBirthday();
        initPhone();
        initSignup();
    }

    private void initSignup() {
        user = new User();

        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(v -> {
            // Read user data and register
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();

            emailInput = email.getText().toString();
            if (emailInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewRegEmailMessage);
                view.setText(email.getHint() + " cannot be empty");
                email.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }
            user.setEmail(emailInput);

            String userpasswordInput = password.getText().toString();
            userPasswd = userpasswordInput;
            if (userpasswordInput.isEmpty()) {
                TextView view = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                view.setText(password.getHint() + " cannot be empty");
                password.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }

            String userInput = givenName.getText().toString();
            if (userInput.length() > 0) {
                userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                        .get(givenName.getHint()), userInput);
            }
            user.setName(userInput);

            userInput = email.getText().toString();
            if (userInput.length() > 0) {
                userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                        .get(email.getHint()), userInput);
            }

            userInput = birthday.getText().toString();
            if (userInput.length() > 0) {
                userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                        .get(birthday.getHint()), userInput);
            }
            user.setBirthdate(userInput);

            userInput = phone.getText().toString();
            if (userInput.length() > 0) {
                userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                        .get(phone.getHint()), userInput);
            }
            user.setPhone(userInput);

            showWaitDialog("Signing up...");

            provider.getAppHelper().getPool()
                    .signUpInBackground(emailInput, userpasswordInput, userAttributes,
                            null, signUpHandler);

        });
    }

    private void initPhone() {
        phone = (EditText) findViewById(R.id.editTextRegPhone);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText(phone.getHint() + " with country code and no seperators");
                    phone.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initBirthday() {
        birthday = (EditText) findViewById(R.id.editTextRegBirthday);
        birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText(email.getHint());
                    birthday.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegBirthdayMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initGivenName() {
        givenName = (EditText) findViewById(R.id.editTextRegGivenName);
        givenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText(givenName.getHint());
                    givenName.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initPassword() {
        password = (EditText) findViewById(R.id.editTextRegUserPassword);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText(password.getHint());
                    password.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText("");
                }
            }
        });
    }

    private void initEmail() {
        email = (EditText) findViewById(R.id.editTextRegEmail);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText(email.getHint());
                    email.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }
        });
    }

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        Intent intent = new Intent(this, ConfirmRegisterActivity.class);
        intent.putExtra("source", "signup");
        intent.putExtra("name", emailInput);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String name = null;
                if (data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
                exit(name, userPasswd);
            }
        }
    }

    protected void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = provider.getAlertDialogBuilder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK",
                (dialog, which) -> {
                    try {
                        userDialog.dismiss();
                        if (exit) {
                            exit(emailInput);
                        }
                    } catch (Exception e) {
                        if (exit) {
                            exit(emailInput);
                        }
                    }
                });
        userDialog = builder.create();
        userDialog.show();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }

    private void exit(String uname) {
        exit(uname, null);
    }

    private void exit(String uname, String password) {
        Intent intent = new Intent();
        if (uname == null) {
            uname = "";
        }
        if (password == null) {
            password = "";
        }
        intent.putExtra("name", uname);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
