package com.example.mac.chartr.adapters;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
public class TripAdapterTest {
    private TripAdapter adapter;
    private Context context;

    private List<Trip> tripData;
    private Trip trip1 = new Trip(1234L, 1234L, true, false,
            12.4, 12.5, 15.4, 14.5, 5, 500);
    private Trip trip2 = new Trip(1234L, 1234L, true, false,
            12.4, 12.5, 15.4, 14.5, 5, 200);
    private Map<String, String> userMap = new HashMap<>();
    private User user = new User("email@email.com", "Name Name",
            "02/02/2002", "+18888888888", 4.5f, 20);

    @Before
    public void setup() {
        tripData  = new ArrayList<>();

        userMap.put("email@email.email", "riding");

        trip1.setUsers(userMap);
        trip2.setUsers(userMap);

        tripData.add(trip1);
        tripData.add(trip2);

        context = RuntimeEnvironment.application;
    }

    @Test
    public void testProperCreate() {
        adapter = new TripAdapter(tripData);

        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(rvParent, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testReplaceList() {
        adapter = new TripAdapter(tripData);

        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(rvParent, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        List<Trip> newList = new ArrayList<>();
        newList.add(trip1);

        adapter.addItems(newList);

        assertEquals(1, adapter.getItemCount());
    }
}
