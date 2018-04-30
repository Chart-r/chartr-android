package com.example.mac.chartr.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.CommonDependencyProvider;
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.ConfirmationEmail;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter for a RecyclerView to view the Requests to join a trip
 */
public class RequestAdapter extends RecyclerView.Adapter {
    private static final String TAG = RequestAdapter.class.getSimpleName();
    private final List<Pair<Trip, User>> requestedUsers;

    public RequestAdapter(List<Pair<Trip, User>> data) {
        requestedUsers = data;
    }

    /**
     * Creates a ViewHolder that will hold the requests to join trips
     *
     * @param parent Parent ViewGroup for the RecyclerView
     * @param viewType The View Type, typically 0
     * @return A ViewHolder, which holds a single trip
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_request_container, parent, false);
        return new RequestAdapter.RequestViewHolder(root);
    }

    /**
     * Method to bind a created view holder to a trip request
     *
     * @param holder ViewHolder on which to insert the trip request card
     * @param position Position of the ViewHolder in the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pair<Trip, User> tripUserPair = requestedUsers.get(position);
        RequestViewHolder placeholder = (RequestViewHolder) holder;
        placeholder.bindData(tripUserPair);

        placeholder.acceptButton.setOnClickListener(v -> {
            Log.d(TAG, "accept button pressed");
            setRiderStatus(v.getContext(), position, tripUserPair, "riding");
        });
        placeholder.rejectButton.setOnClickListener(v -> {
            Log.d(TAG, "reject button pressed");
            setRiderStatus(v.getContext(),  position, tripUserPair, "rejected");
        });
    }

    /**
     * Gets how many items should be displayed in this recycler view
     *
     * @return Count of the number of requested users
     */
    @Override
    public int getItemCount() {
        Log.d("Adapter", "" + requestedUsers.size());
        return requestedUsers.size();
    }

    /**
     * Internal class that represents a single RequestViewHolder object which the
     * recycler view can display
     */
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView startTime;
        final TextView start;
        final TextView destination;
        final Button acceptButton;
        final Button rejectButton;

        RequestViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            startTime = itemView.findViewById(R.id.textViewTime);
            start = itemView.findViewById(R.id.textViewStart);
            destination = itemView.findViewById(R.id.textViewDestination);
            acceptButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }

        void bindData(Pair<Trip, User> tripPair) {
            Trip trip = tripPair.first;
            String nameRetrieved = tripPair.second.getName();
            name.setText("" + nameRetrieved);
            startTime.setText(String
                    .format("%s at %s", trip.getStartDateString(), trip.getStartTimeString()));
            start.setText(String.format("From: %s",
                    trip.getStartLocationShortName(itemView.getContext())));
            destination.setText(String.format("To: %s",
                    trip.getEndLocationShortName(itemView.getContext())));


        }

    }

    /**
     * Gets the status of a particular rider
     *
     * @param context Context
     * @param position Position in the recycler view
     * @param tripUserPair The pair of a trip to a user
     * @param status Status that you wish to set for the user
     */
    private void setRiderStatus(Context context, int position, Pair<Trip,
            User> tripUserPair, String status) {
        ApiInterface apiInterface = ApiClient.getApiInstance();
        Trip trip = tripUserPair.first;
        User user = tripUserPair.second;
        String uid = user.getUid();
        String tid = trip.getId();
        Call<String> call = apiInterface.updateTrip(uid, tid, status);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.code() + "");
                requestedUsers.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, requestedUsers.size());

                if (status.equals("riding")) {
                    CharSequence text = "Accepted " + tripUserPair.second.getName();
                    sendTripConfirmationEmail(tripUserPair);
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                } else {
                    CharSequence text = "Rejected " + tripUserPair.second.getName();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Retrofit failed to get data");
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });
    }

    /**
     * Makes a post api call to send ride approved confirmation emails.
     *
     * @param tripUserPair The pair of a trip to a user
     */
    private void sendTripConfirmationEmail(Pair<Trip,
            User> tripUserPair) {
        ApiInterface apiInterface = ApiClient.getApiInstance();
        CommonDependencyProvider provider = new CommonDependencyProvider();
        Trip trip = tripUserPair.first;
        User driver = provider.getAppHelper().getLoggedInUser();
        User rider = tripUserPair.second;
        ConfirmationEmail confirmationEmail = new ConfirmationEmail(driver.getName(),
                rider.getName(), driver.getFormattedPhone(), rider.getFormattedPhone(),
                driver.getEmail(), rider.getEmail(), trip.getStartTime());
        Call<String> call = apiInterface.postTripConfirmationEmail(confirmationEmail);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.code() + "");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Retrofit call failed");
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        });
    }
}
