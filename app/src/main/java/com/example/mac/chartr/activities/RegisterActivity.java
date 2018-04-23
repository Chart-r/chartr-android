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
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.MaskWatcher;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            email.setBackground(getDrawable(R.drawable.text_border_default));
            password.setBackground(getDrawable(R.drawable.text_border_default));
            givenName.setBackground(getDrawable(R.drawable.text_border_default));
            birthday.setBackground(getDrawable(R.drawable.text_border_default));
            phone.setBackground(getDrawable(R.drawable.text_border_default));

            /*
             Check for a valid EMAIL
             */
            emailInput = email.getText().toString();
            TextView emailMessage = (TextView) findViewById(R.id.textViewRegEmailMessage);
            if (emailInput.isEmpty() || !emailMessage.getText().toString().equals("")) {
                email.setBackground(getDrawable(R.drawable.text_border_error));
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG)
                .show();
                return;
            }
            user.setEmail(emailInput);

            /*
             Check for a valid PASSWORD
             */
            String userpasswordInput = password.getText().toString();
            TextView passwordMessage = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
            userPasswd = userpasswordInput;
            if (userpasswordInput.isEmpty() || !passwordMessage.getText().toString().equals("")) {
                password.setBackground(getDrawable(R.drawable.text_border_error));
                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            /*
             Check for a valid NAME
             */
            String name = givenName.getText().toString();
            TextView nameMessage = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
            if (name.isEmpty() || !nameMessage.getText().toString().equals("")) {
                givenName.setBackground(getDrawable(R.drawable.text_border_error));
                Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            /*
             Check for a valid BIRTHDAY
             */
            String birthdayInput = givenName.getText().toString();
            TextView birthdayMessage = (TextView) findViewById(R.id.textViewRegBirthdayMessage);
            if (birthdayInput.isEmpty() || !birthdayMessage.getText().toString().equals("")) {
                birthday.setBackground(getDrawable(R.drawable.text_border_error));
                Toast.makeText(getApplicationContext(), "Invalid Birthday", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            /*
             Check for a valid PHONE NUMBER
             */
            String phoneInput = givenName.getText().toString();
            TextView phoneMessage = (TextView) findViewById(R.id.textViewRegPhoneMessage);
            if (phoneInput.isEmpty() || !phoneMessage.getText().toString().equals("")) {
                phone.setBackground(getDrawable(R.drawable.text_border_error));
                Toast.makeText(getApplicationContext(), "Invalid Phone Number",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            /*
             Add name to AppHelper Attribute
             */
            String userInput = givenName.getText().toString();
            userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                    .get("Given name"), userInput);
            user.setName(userInput);

            /*
             Add email to AppHelper Attribute
             */
            userInput = email.getText().toString();
            userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                    .get("Email"), userInput);

            /*
             Add birthday to AppHelper Attribute
             */
            userInput = birthday.getText().toString();
            DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat transformedFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthdayDate = originalFormat.parse(userInput);
                userInput = transformedFormat.format(birthdayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                    .get("Birthday"), userInput);
            user.setBirthdate(userInput);
            System.out.println(userInput);

            /*
             Add phone to AppHelper Attribute
             */
            userInput = phone.getText().toString();
            userInput = userInput.substring(0, 2) + userInput.substring(3, 6)
                    + userInput.substring(7, 10) + userInput.substring(11);
            userAttributes.addAttribute(provider.getAppHelper().getSignUpFieldsC2O()
                    .get("Phone number"), userInput);
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
            private MaskWatcher maskWatcher = new MaskWatcher("+#-###-###-####");

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                maskWatcher.beforeTextChanged(s, start, count, after);

                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText("Phone number with country code\n(example: +1-888-333-4444)");
                    phone.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maskWatcher.onTextChanged(s, start, before, count);

                TextView message = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                Pattern pattern =
                        Pattern.compile("^\\+1\\-([1-9])(\\d{2})\\-(\\d{3})\\-(\\d{4})");
                Matcher matcher = pattern.matcher(phone.getText().toString());

                if (matcher.find()) {
                    message.setText("");
                } else {
                    message.setText("Invalid phone number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                maskWatcher.afterTextChanged(s);

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
            private MaskWatcher maskWatcher = new MaskWatcher("##/##/####");

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                    label.setText(birthday.getHint());
                    birthday.setBackground(getDrawable(R.drawable.text_border_selector));
                }

                maskWatcher.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView message = (TextView) findViewById(R.id.textViewRegBirthdayMessage);

                String enteredBirthday = birthday.getText().toString();
                //https://stackoverflow.com/questions/5978510/regex-to-match-date
                Pattern pattern =
                        Pattern.compile("(?:(09|04|06|11)([/])(0[1-9]|[12]\\d|30)"
                                + "([/])((?:19|20)\\d\\d))|(?:(01|03|05|07|08|10|12)"
                                + "([/])(0[1-9]|[12]\\d|3[01])([/])((?:19|20)\\d\\d))"
                                + "|(?:02([/])(?:(?:(0[1-9]|1\\d|2[0-8])([/])"
                                + "((?:19|20)\\d\\d))|(?:(29)([/])"
                                + "((?:(?:19|20)(?:04|08|12|16|20|24|28|32|36|40|44|48|52|56|"
                                + "60|64|68|72|76|80|84|88|92|96))|2000))))");
                Matcher matcher = pattern.matcher(enteredBirthday);


                if (matcher.find()) {
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    int enteredYear = Integer.parseInt(enteredBirthday
                            .substring(enteredBirthday.length() - 4));

                    if (enteredYear < 1880 || enteredYear >= year - 1) {
                        message.setText("Invalid birthday");
                    } else {
                        message.setText("");
                    }
                } else {
                    message.setText("Invalid birthday");
                }

                maskWatcher.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                    label.setText("");
                }

                maskWatcher.afterTextChanged(s);
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
            /**
             * Make the label text up top change once the user starts typing (thus, the current
             * length is zero before the change)
             *
             * @param s CharSequence
             * @param start Start
             * @param count Count
             * @param after After
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    //ADD CHECK FOR REQUIREMENTS TO SET THIS LABEL
                    label.setText("Password");
                    password.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            /**
             * This means that the text in the box has been changed, and thus we should reset the
             * error message that can occur upon sign-up failure
             *
             * @param s Charsequence of the change
             * @param start Start index
             * @param before Before change
             * @param count Count after change
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView message = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);


                //https://stackoverflow.com/questions/19605150/
                // regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
                String specialCharacters = "\\^ $ \\* \\. \\[ \\] \\{ \\} \\( \\) \\? \\-"
                        + "\" ! @ # % & / \\\\ , > < \' : ; \\| _ ~ `";
                Pattern pattern =
                        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[" + specialCharacters
                                + "])[A-Za-z\\d" + specialCharacters + "]{8,}");
                Matcher matcher = pattern.matcher(password.getText().toString());

                if (matcher.find()) {
                    message.setText("");
                } else {
                    message.setText("Password must be at least 8 characters and contain a number,"
                            + " a special character, a lowercase letter, and an uppercase letter.");
                }
            }

            /**
             * If the user has cleared the text field, reset the upper label
             *
             * @param s Change to the text field
             */
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
                    label.setText("Email");
                    email.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView message = (TextView) findViewById(R.id.textViewRegEmailMessage);

                //http://emailregex.com/
                Pattern pattern =
                        Pattern.compile("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
                                + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
                                + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\"
                                + "x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]"
                                + "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\"
                                + "[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]"
                                + "|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\"
                                + "x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\"
                                + "x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher matcher = pattern.matcher(email.getText().toString());

                if (matcher.find()) {
                    message.setText("");
                } else {
                    message.setText("Invalid email format");
                }

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
