package com.example.patryk.astroweather1.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patryk.astroweather1.AstroWeather;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class SunFragment extends Fragment  {

    static TextView longitude,latitude,time;
    static TextView sunriseTime,sunsetTime,dawnTime,sunriseAzimuth,sunsetAzimuth,duskTime;
    int SomeInt;
    String someString;
    static private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static SunFragment newInstance(int someInt, String someString) {
        SunFragment fragment = new SunFragment();
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
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sun_information, container, false);
        time = view.findViewById(R.id.Time);

        longitude = view.findViewById(R.id.longitudeValue);
        latitude = view.findViewById(R.id.latitudeValue);
        sunriseTime = view.findViewById(R.id.sunriseTime);
        sunriseAzimuth = view.findViewById(R.id.sunriseAzimuth);
        sunsetTime = view.findViewById(R.id.sunsetTime);
        sunsetAzimuth = view.findViewById(R.id.sunsetAzimuth);
        dawnTime = view.findViewById(R.id.dawnTime);
        duskTime = view.findViewById(R.id.duskTime);
        refresh(view);

        return view;
    }


    public void setInfo(){
        latitude.setText(Double.toString(Database.getInstance().getLatitude()));
        longitude.setText(Double.toString(Database.getInstance().getLongitude()));
    }
    public void setValues(){
        sunriseTime.setText(String.format("%02d:%02d:%02d", AstroWeather.getSunInfo().getSunrise().getHour(), AstroWeather.getSunInfo().getSunrise().getMinute(), AstroWeather.getSunInfo().getSunrise().getSecond()));
        sunriseAzimuth.setText(decimalFormat.format(AstroWeather.getSunInfo().getAzimuthRise()));
        sunsetTime.setText(String.format("%02d:%02d:%02d",AstroWeather.getSunInfo().getSunset().getHour(), AstroWeather.getSunInfo().getSunset().getMinute(), AstroWeather.getSunInfo().getSunset().getSecond()));
        sunsetAzimuth.setText(decimalFormat.format(AstroWeather.getSunInfo().getAzimuthSet()));
        dawnTime.setText(String.format("%02d:%02d:%02d",AstroWeather.getSunInfo().getTwilightMorning().getHour(), AstroWeather.getSunInfo().getTwilightMorning().getMinute(), AstroWeather.getSunInfo().getTwilightMorning().getSecond()));
        duskTime.setText(String.format("%02d:%02d:%02d",AstroWeather.getSunInfo().getTwilightEvening().getHour(), AstroWeather.getSunInfo().getTwilightEvening().getMinute(), AstroWeather.getSunInfo().getTwilightEvening().getSecond()));
    }

    public void refresh(View view)
    {
            Thread timerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            if (getActivity() != null)
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Calendar calendar = Calendar.getInstance();
                                        time.setText(String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
                                    }
                                });
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            timerThread.start();

            Thread dataThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AstroWeather().setUp();
                                        setValues();
                                    }
                                });
                            }
                            Thread.sleep(60000 * Database.getInstance().getRefreshRate());
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            dataThread.start();
        setInfo();
    }
}
