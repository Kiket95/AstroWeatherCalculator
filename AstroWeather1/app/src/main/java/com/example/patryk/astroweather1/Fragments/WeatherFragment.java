package com.example.patryk.astroweather1.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.astroweather1.Data.Atmosphere;
import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Data.MyLocation;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.util.Calendar;

public class WeatherFragment extends Fragment implements WeatherServiceCallback{

    TextView temperature,city,country,time,latitude,longitude,pressure,windDirection,windSpeed;
    ImageView photo;
    private YahooService yahooService ;
    ;

    public static WeatherFragment newInstance(int someInt, String someString) {
        WeatherFragment fragment = new WeatherFragment();
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
        View view = inflater.inflate(R.layout.weather_information, container, false);
        photo = view.findViewById(R.id.imageView2);
        temperature = view.findViewById(R.id.temperature);
        city = view.findViewById(R.id.cityNamevalue);
        time = view.findViewById(R.id.time);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
        pressure = view.findViewById(R.id.pressure);
        windDirection = view.findViewById(R.id.windDirectionValue);
        windSpeed = view.findViewById(R.id.windSpeedValue);
        country = view.findViewById(R.id.countryName);
        yahooService = new YahooService(this);
        yahooService.refreshWeather(Database.getInstance().getLocationName());
        return view;
    }

    @Override
    public void serviceSucces(Channel channel) {
        Item item = channel.getItem();
        Atmosphere atmosphere = channel.getAtmosphere();
        temperature.setText(item.getCondition().getTemperature() + channel.getUnit().getTemperature());
        city.setText(channel.getLocation().getCity());
        country.setText(channel.getLocation().getCountry());
        longitude.setText(Double.toString(item.getLongitude()));
        latitude.setText(Double.toString(item.getLatitude()));
        Database.getInstance().setLatitude(item.getLatitude());
        Database.getInstance().setLongitude(item.getLongitude());
        pressure.setText(Integer.toString(atmosphere.getPressure()) + channel.getUnit().getPressure());
        windSpeed.setText(Integer.toString(channel.getWind().getSpeed()) + channel.getUnit().getSpeed());
        windDirection.setText(Integer.toString(channel.getWind().getDirection()) + (char) 0x00B0);
        time.setText(item.getCondition().getTime());
        int resourceID = getResources().getIdentifier("drawable/icon" + item.getCondition().getCode(),null,getContext().getPackageName());
        Drawable weatherIcon = getResources().getDrawable(resourceID);
        photo.setImageDrawable(weatherIcon);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getActivity(),exception.getMessage(),Toast.LENGTH_LONG).show();
    }

    void refresh(  )
    {
        Thread timerThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10000);
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    yahooService.refreshWeather(Database.getInstance().getLocationName());
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        timerThread.start();
    }
}
