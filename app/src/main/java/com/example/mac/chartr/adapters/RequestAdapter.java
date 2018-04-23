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
import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAdapter extends RecyclerView.Adapter {
    public static final String TAG = RequestAdapter.class.getSimpleName();
    private List<Pair<Trip, User>> requestedUsers;

    public RequestAdapter(List<Pair<Trip, User>> data) {
        requestedUsers = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_request_container, parent, false);
        return new RequestAdapter.RequestViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pair<Trip, User> tripUserPair = requestedUsers.get(position);
        RequestViewHolder placeholder = (RequestViewHolder) holder;
        placeholder.bindData(tripUserPair, position);

        placeholder.acceptButton.setOnClickListener(v -> {
            Log.d(TAG, "accept button pressed");
            setRiderStatus(v.getContext(), position, tripUserPair, "riding");
        });
        placeholder.rejectButton.setOnClickListener(v -> {
            Log.d(TAG, "reject button pressed");
            setRiderStatus(v.getContext(),  position, tripUserPair, "rejected");
        });
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "" + requestedUsers.size());
        return requestedUsers.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView startTime;
        TextView start;
        TextView destination;
        Button acceptButton;
        Button rejectButton;

        RequestViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            startTime = itemView.findViewById(R.id.textViewTime);
            start = itemView.findViewById(R.id.textViewStart);
            destination = itemView.findViewById(R.id.textViewDestination);
            acceptButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }

        void bindData(Pair<Trip, User> tripPair, int position) {
            Trip trip = tripPair.first;
            String nameRetrieved = tripPair.second.getName();
            name.setText("" + nameRetrieved);
            startTime.setText(String
                    .format("%s at %s", trip.getStartDateString(), trip.getStartTimeString()));
            start.setText(trip.getStartLocationShortName(itemView.getContext()));
            destination.setText(trip.getEndLocationShortName(itemView.getContext()));


        }

    }

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
}
