package com.example.mac.chartr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.chartr.R;

/**
 * Created by User on 04.03.2018.
 */

public class NearbyActivity extends Activity implements OnItemClickListener {

    ListView listView = findViewById(R.id.container1);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_trips);

        String[] card = getResources().getStringArray(R.array.post_trip_card);
        listView.setFilterText(card[0]);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, card));
        listView.setOnItemClickListener(this);

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = 400;
        listView.setLayoutParams(params);
        listView.setDivider(null);

    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view,
                            int position, long id) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
    }

}
