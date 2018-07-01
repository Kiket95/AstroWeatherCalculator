package com.example.patryk.astroweather1.Fragments;

import android.annotation.SuppressLint;
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

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Forecast;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.FileDataManager;
import com.example.patryk.astroweather1.Databases.FileManager;
import com.example.patryk.astroweather1.NetworkConnection;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import org.w3c.dom.Text;

public class ForecastFragment extends Fragment implements WeatherServiceCallback{

    ImageView photo,photo2,photo3,photo4,photo5,photo6,photo7;
    YahooService yahooService;
 //   private Button read;
    private TextView data,high,high2,high3,high4,high5,high6,high7,
            low,low2,low3,low4,low5,low6,low7,date,date2,date3,date4,date5,date6,date7,
            text,text2,text3,text4,text5,text6,text7;
    FileDataManager fileManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.weather_forecast, container, false);
        setView(view);
        yahooService = new YahooService(ForecastFragment.this);
        yahooService.refreshWeather(Database.getInstance().getLocationName());
        if(!NetworkConnection.isOnline())
        {
            fileManager = new FileDataManager(getContext());
            fileManager.load(this);
        }

     //   fileManager = new FileManager();


     //    refresh();
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void serviceSucces(Channel channel) {

        if(Channel.ErrorFlag)
        {
            yahooService.refreshWeather(Settings.tempLocation);
            Channel.ErrorFlag = false;
        }
        else{
            Forecast[] forecast = channel.getItem().getForecast();
            ImageView[] imageViews = {photo,photo2,photo3,photo4,photo5,photo6,photo7};
            TextView[] texts = {text,text2,text3,text4,text5,text6,text7};
            TextView[] dates = {date,date2,date3,date4,date5,date6,date7};
            TextView[] highs = {high,high2,high3,high4,high5,high6,high7};
            TextView[] lows = {low,low2,low3,low4,low5,low6,low7};
            for(int i = 0;i<7;i++)
            {
                dates[i].setText(forecast[i].getDay() + " " + forecast[i].getDate());
                highs[i].setText("↑ "+ forecast[i].getHigh() + " °" + channel.getUnit().getTemperature());
                lows[i].setText( "↓ " + forecast[i].getLow() + " °" + channel.getUnit().getTemperature());
                texts[i].setText(forecast[i].getText());
                int image = getResources().getIdentifier("drawable/icon" + forecast[i].getCode(),null,getContext().getPackageName());
                Drawable weatherIcon = getResources().getDrawable(image);
                imageViews[i].setImageDrawable(weatherIcon);
            }
            fileManager = new FileDataManager(getContext());
            fileManager.save(channel);
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");
            System.out.println("FORECAST");

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getActivity(),exception.getMessage(),Toast.LENGTH_LONG).show();
    }
    public void setView(View view)
    {
        photo = view.findViewById(R.id.photo);
        photo2 = view.findViewById(R.id.photo2);
        photo3 = view.findViewById(R.id.photo3);
        photo4 = view.findViewById(R.id.photo4);
        photo5 = view.findViewById(R.id.photo5);
        photo6 = view.findViewById(R.id.photo6);
        photo7 = view.findViewById(R.id.photo7);
        high = view.findViewById(R.id.high);
        high2 = view.findViewById(R.id.high2);
        high3 = view.findViewById(R.id.high3);
        high4 = view.findViewById(R.id.high4);
        high5 = view.findViewById(R.id.high5);
        high6 = view.findViewById(R.id.high6);
        high7 = view.findViewById(R.id.high7);
        low = view.findViewById(R.id.low);
        low2 = view.findViewById(R.id.low2);
        low3 = view.findViewById(R.id.low3);
        low4 = view.findViewById(R.id.low4);
        low5 = view.findViewById(R.id.low5);
        low6 = view.findViewById(R.id.low6);
        low7 = view.findViewById(R.id.low7);
        text = view.findViewById(R.id.text);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);
        text5 = view.findViewById(R.id.text5);
        text6 = view.findViewById(R.id.text6);
        text7 = view.findViewById(R.id.text7);
        date = view.findViewById(R.id.date);
        date2 = view.findViewById(R.id.date2);
        date3 = view.findViewById(R.id.date3);
        date4 = view.findViewById(R.id.date4);
        date5 = view.findViewById(R.id.date5);
        date6 = view.findViewById(R.id.date6);
        date7 = view.findViewById(R.id.date7);
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
                                    setYahoo();
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

    void setYahoo()
    {
        yahooService = new YahooService(this);
        yahooService.refreshWeather(Database.getInstance().getLocationName());
    }

}
