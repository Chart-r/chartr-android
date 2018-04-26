package com.example.mac.chartr.fragments;

import  android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    public static final String TAG = ProfileFragment.class.getSimpleName();
    private CommonDependencyProvider provider;
    private String uid;

    private User user;
    private TextView textViewName;
    private TextView textViewRating;
    private TextView textViewReviewCount;
    private TextView textViewEmail;
    private TextView textViewPhone;

    /**
     * Constructor of the class
     */
    public ProfileFragment() {
        setCommonDependencyProvider(new CommonDependencyProvider());
    }

    /**
     * Initialises the common dependency provider of the class
     * @param provider The provider to which the class's provider is set
     */

    public void setCommonDependencyProvider(CommonDependencyProvider provider) {
        this.provider = provider;
    }

    /**
     * Method inherited from the Fragment class that is called upon creation of the fragment
     *
     * @param savedInstanceState Bundle of the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "start onCreate()");
        super.onCreate(savedInstanceState);
        uid = getLoggedInUid();
        Log.d(TAG, "end onCreate()");
    }

    /**
     * Method inherited from the Fragment class that returns a view that has been inflated
     * with the container argument
     * @param inflater Used to inflate the returned object
     * @param container The viewGroup used in the inflation of the returned object
     * @param savedInstanceState Bundle of the saved instance state
     * @return the inflated view containing the personal information of the user
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewName = root.findViewById(R.id.textViewName);
        textViewRating = root.findViewById(R.id.textViewRating);
        textViewReviewCount = root.findViewById(R.id.textViewReviewCount);
        textViewEmail = root.findViewById(R.id.textViewEmail);
        textViewPhone = root.findViewById(R.id.textViewPhone);

        ApiInterface apiInterface = ApiClient.getApiInstance();
        Call<User> call = apiInterface.getUserFromUid(uid);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, response.code() + "");
                user = response.body();
                textViewName.setText(user.getName());
                textViewRating.setText("Rating: " + Float.toString(user.getRating()));
                textViewReviewCount.setText(Integer.toString(user.getReviewCount()) + " reviews");
                textViewEmail.setText("email: " + user.getEmail());
                textViewPhone.setText("Phone number: " + user.getPhone());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to get data");
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });

        return root;
    }

    /**
     * Gets the logged in user from the provider
     * @return logged in user
     */
    private String getLoggedInUid() {
        return provider.getAppHelper().getLoggedInUser().getUid();
    }
}
