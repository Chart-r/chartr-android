package com.example.mac.chartr.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mac.chartr.R;
import com.example.mac.chartr.objects.Trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter {
    private List<Pair<Trip, String>> RequestedUsers;

    public RequestAdapter(List<Pair<Trip, String>> data) {
        RequestedUsers = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate (R.layout.layout_request_container, parent, false);
        return new RequestAdapter.RequestViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pair<Trip, String> tripPair = RequestedUsers.get(position);
        RequestViewHolder placeholder = (RequestViewHolder) holder;
        placeholder.bindData(tripPair);
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "" + RequestedUsers.size());
        return RequestedUsers.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView startTime;
        TextView start;
        TextView destination;

        RequestViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            startTime = itemView.findViewById(R.id.textViewTime);
            start = itemView.findViewById(R.id.textViewStart);
            destination = itemView.findViewById(R.id.textViewDestination);
        }

        void bindData(Pair<Trip, String> tripPair) {
            Trip trip = tripPair.first;
            String nameRetrieved = tripPair.second;
            name.setText("" + nameRetrieved);
            startTime.setText(convertLongToDateTime(trip.getStartTime()));
            start.setText("Start: " + trip.getStartLat());
            destination.setText("Destination: " + trip.getEndLat());
        }

        String convertLongToDateTime(long longTime){
            Date date = new Date(longTime);
            SimpleDateFormat formatter
                    = new SimpleDateFormat("yyyy/M/dd 'at' h:mm a zzz");
            return formatter.format(date);
        }

    }
}
