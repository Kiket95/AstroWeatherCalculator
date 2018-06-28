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

public class MoonFragment extends Fragment  {

    static TextView longitude,latitude,time;
    static TextView moonriseTime,moonsetTime,fullMoon,newMoon,moonPhase,synodMonth;
    static private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static MoonFragment newInstance(int someInt, String someString) {
        MoonFragment fragment = new MoonFragment();
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
        View view = inflater.inflate(R.layout.moon_information, container, false);
        time = view.findViewById(R.id.Time);
        longitude = view.findViewById(R.id.longitudeValue);
        latitude = view.findViewById(R.id.latitudeValue);
        moonsetTime = view.findViewById(R.id.moonsetTime);
        moonriseTime = view.findViewById(R.id.moonriseTime);
        moonPhase = view.findViewById(R.id.moonPhasePercentage);
        newMoon = view.findViewById(R.id.closestNewMoon);
        fullMoon = view.findViewById(R.id.closestFullMoon);
        synodMonth = view.findViewById(R.id.synodMonth);
        refresh(view);
        setInfo();
        return view;
    }

    public void setInfo(){
        latitude.setText(Double.toString(Database.getInstance().getLatitude()));
        longitude.setText(Double.toString(Database.getInstance().getLongitude()));
    }
    public void setValues(){
        moonriseTime.setText(String.format("%02d:%02d:%02d", AstroWeather.getMoonInfo().getMoonrise().getHour(), AstroWeather.getMoonInfo().getMoonrise().getMinute(), AstroWeather.getMoonInfo().getMoonrise().getSecond()));
        moonsetTime.setText(String.format("%02d:%02d:%02d",AstroWeather.getMoonInfo().getMoonset().getHour(), AstroWeather.getMoonInfo().getMoonset().getMinute(), AstroWeather.getMoonInfo().getMoonset().getSecond()));

        newMoon.setText(String.format("%02d/%02d/%02d",AstroWeather.getMoonInfo().getNextNewMoon().getDay(),AstroWeather.getMoonInfo().getNextNewMoon().getMonth(),AstroWeather.getMoonInfo().getNextNewMoon().getYear()));
        fullMoon.setText(String.format("%02d/%02d/%02d",AstroWeather.getMoonInfo().getNextFullMoon().getDay(),AstroWeather.getMoonInfo().getNextFullMoon().getMonth(),AstroWeather.getMoonInfo().getNextFullMoon().getYear()));
        moonPhase.setText(decimalFormat.format(AstroWeather.getMoonInfo().getIllumination()*1000) + "%");
        synodMonth.setText(String.valueOf(Math.round(AstroWeather.getMoonInfo().getAge())));
    }

    public void refresh(View view)
    {
            Thread timerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        latitude.setText(Double.toString(Database.getInstance().getLatitude()));
                                        longitude.setText(Double.toString(Database.getInstance().getLongitude()));
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
                                Thread.sleep(60000 * Database.getInstance().getRefreshRate());
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            dataThread.start();

        setInfo();
    }
}
