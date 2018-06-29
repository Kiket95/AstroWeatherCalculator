package com.example.patryk.astroweather1.Databases;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Fragments.Settings;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.util.ArrayList;

public class SQLListData extends AppCompatActivity implements WeatherServiceCallback{

    private static final String TAG = "ListDataActivity";

    YahooService service;
    SqlDatabase myDatabase;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdata);
        mListView = findViewById(R.id.ListView1);
        myDatabase = new SqlDatabase(this);
        service = new YahooService(this);
        populateListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Database.getInstance().setLocationName(name);
                service.refreshWeather(Database.getInstance().getLocationName());
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = myDatabase.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }

    @Override
    public void serviceSucces(Channel channel) {
        Item item = channel.getItem();
        Settings.longitude.setText(Double.toString(item.getLongitude()));
        Settings.latitude.setText(Double.toString(item.getLatitude()));
        Database.getInstance().setLatitude(item.getLatitude());
        Database.getInstance().setLongitude(item.getLongitude());
    }

    @Override
    public void serviceFailure(Exception exception) {

    }
}
