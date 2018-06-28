package com.example.patryk.astroweather1.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.astroweather1.AstroWeather;
import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.FileManager;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.xml.transform.Templates;

public class ForecastFragment extends Fragment implements WeatherServiceCallback{

    ImageView photo;
    private YahooService yahooService;
    private Button read;
    private TextView data;
    FileManager fileManager;

    public static ForecastFragment newInstance(int someInt, String someString) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        args.putString("someString", someString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.weather_forecast, container, false);
        photo = view.findViewById(R.id.imageView3);
        read = view.findViewById(R.id.Read);
        data = view.findViewById(R.id.data);
        yahooService = new YahooService(this);
        yahooService.refreshWeather(Database.getInstance().getLocationName());
        fileManager = new FileManager();
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setText(fileManager.readFile());
            }
        });

        return view;
    }

    @Override
    public void serviceSucces(Channel channel) {
        Item item = channel.getItem();
        int resourceID = getResources().getIdentifier("drawable/icon" + item.getCondition().getCode(),null,getContext().getPackageName());
        Drawable weatherIcon = getResources().getDrawable(resourceID);
        photo.setImageDrawable(weatherIcon);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getActivity(),exception.getMessage(),Toast.LENGTH_LONG).show();
    }
}
