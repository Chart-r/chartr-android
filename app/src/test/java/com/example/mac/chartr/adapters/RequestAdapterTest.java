package com.example.mac.chartr.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class RequestAdapterTest {
    private Context context;

    private List<Pair<Trip, User>> pairList;

    private Trip trip1 = new Trip(1234L, 1234L, true, false,
            12.4, 12.5, 15.4, 14.5, 5, 500);
    private Trip trip2 = new Trip(1234L, 1234L, true, false,
            12.4, 12.5, 15.4, 14.5, 5, 200);

    private User user1 = new User("email@email.com", "Name Name",
            "02/02/2002", "+18888888888", 4.5f, 20);
    private User user2 = new User("gmail@gmail.com", "Stan Smith",
            "02/02/2001", "+18888388888", 4.9f, 200);

    @Before
    public void setup() {
        pairList = new ArrayList<>();

        Pair<Trip, User> pair1 = new Pair<>(trip1, user1);
        Pair<Trip, User> pair2 = new Pair<>(trip2, user2);

        pairList.add(pair1);
        pairList.add(pair2);

        context = RuntimeEnvironment.application;
    }

    @Test
    public void testProperCreate() {
        RequestAdapter adapter = new RequestAdapter(pairList);

        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(rvParent, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        assertEquals(2, adapter.getItemCount());
    }
}
