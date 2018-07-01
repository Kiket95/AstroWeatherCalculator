package com.example.patryk.astroweather1.Databases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Fragments.Settings;
import com.example.patryk.astroweather1.MainActivity;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.util.ArrayList;
import java.util.List;

public class SQLListData extends AppCompatActivity implements WeatherServiceCallback {

    private static final String TAG = "ListDataActivity";

    SqlDatabase myDatabase;
    private ListView mListView;
    private Button deleteButton;
    YahooService yahooService;
    private static boolean deleteFlag = false;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdata);
        mListView = findViewById(R.id.ListView1);
        myDatabase = new SqlDatabase(this);
        populateListView();
        yahooService = new YahooService(this);
        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFlag = true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();

                if(deleteFlag){
                    myDatabase.deleteName(name);
                    deleteFlag = false;
                    Intent intent = new Intent(SQLListData.this, Settings.class);
                    startActivity(intent);
                }
                    else{
                    Database.getInstance().setLocationName(name);
                    yahooService.refreshWeather(Database.getInstance().getLocationName());
                    Intent intent = new Intent(SQLListData.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

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
        Database.getInstance().setLatitude(channel.getItem().getLatitude());
        Database.getInstance().setLongitude(channel.getItem().getLongitude());
        Settings.latitude.setText(Double.toString(channel.getItem().getLatitude()));
        Settings.longitude.setText(Double.toString(channel.getItem().getLongitude()));
    }

    @Override
    public void serviceFailure(Exception exception) {

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



//    @Override
//    public void serviceSucces(Channel channel) {
//        Item item = channel.getItem();
//        Settings.longitude.setText(Double.toString(item.getLongitude()));
//        Settings.latitude.setText(Double.toString(item.getLatitude()));
//        Database.getInstance().setLatitude(item.getLatitude());
//        Database.getInstance().setLongitude(item.getLongitude());
//    }
//
//    @Override
//    public void serviceFailure(Exception exception) {
//
//    }
}
