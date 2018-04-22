package com.example.mac.chartr.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.R;
import com.example.mac.chartr.activities.TripDetailActivity;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mac.chartr.activities.MainActivity.TAG;

public class TripAdapter extends RecyclerView.Adapter {
    private List<Trip> tripsData;

    private Comparator<Trip> comparator = new Comparator<Trip>() {
        @Override
        public int compare(Trip a, Trip b) {
            return (int) (b.getStartTime() - a.getStartTime());
        }
    };

    public TripAdapter(List<Trip> data) {
       tripsData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_trip_container, parent, false);
        return new TripViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Trip item = tripsData.get(position);
        TripViewHolder placeholder = (TripViewHolder) holder;
        placeholder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return tripsData.size();
    }

    public void addItems(List<Trip> data) {
        tripsData.clear();
        tripsData.addAll(data);
        Collections.sort(tripsData, comparator);

        notifyDataSetChanged();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        Trip trip;
        TextView name;
        TextView rating;
        TextView seats;
        TextView seatsText;
        TextView start;
        TextView destination;
        TextView departureDate;

        TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            rating = itemView.findViewById(R.id.textViewRating);
            seats = itemView.findViewById(R.id.textViewSeats);
            seatsText = itemView.findViewById(R.id.textViewSeatsText);
            start = itemView.findViewById(R.id.textViewStart);
            destination = itemView.findViewById(R.id.textViewDestination);
            departureDate = itemView.findViewById(R.id.textViewDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), TripDetailActivity.class);
                    intent.putExtra("trip", trip);
                    intent.putExtra("type", "mytrips");
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        void bindData(Trip trip) {
            this.trip = trip;
            seats.setText(String.format("%d / %d", trip.getRidingCount(), trip.getSeats()));
            start.setText(trip.getStartLocationShortName(itemView.getContext()));
            destination.setText(trip.getEndLocationShortName(itemView.getContext()));
            departureDate.setText(trip.getStartDateString());

            ApiInterface apiInterface = ApiClient.getApiInstance();
            Call<User> call = apiInterface.getUserFromUid(trip.getDriverId());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d(TAG, response.code() + "");
                    User user = response.body();
                    name.setText(user.getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "Retrofit failed to get data");
                    Log.d(TAG, t.getMessage());
                    t.printStackTrace();
                    call.cancel();
                }
            });
        }

    }


}
