package com.example.mac.chartr.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter {
    private List<Trip> tripsData;

    public TripAdapter(List<Trip> data) {
       tripsData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trip_container, parent, false);
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

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView rating;
        TextView seats;
        TextView seatsText;
        TextView start;
        TextView destination;

        TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            rating = itemView.findViewById(R.id.textViewRating);
            seats = itemView.findViewById(R.id.textViewSeats);
            seatsText = itemView.findViewById(R.id.textViewSeatsText);
            start = itemView.findViewById(R.id.textViewStart);
            destination = itemView.findViewById(R.id.textViewDestination);
        }

        void bindData(Trip trip) {
            name.setText("TEST NAME");
            seats.setText((trip.getUsers().size() - 1) + "/" + trip.getSeats());
            start.setText("" + trip.getStartLat());
            destination.setText("" + trip.getEndLat());
        }

    }


}
