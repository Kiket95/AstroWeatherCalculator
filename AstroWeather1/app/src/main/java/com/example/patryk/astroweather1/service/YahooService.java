package com.example.patryk.astroweather1.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.SqlDatabase;
import com.example.patryk.astroweather1.Fragments.Settings;
import com.example.patryk.astroweather1.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class YahooService {
    public enum Operation{findByName,None,findByLocalizationName,findByLatLong};
    public static Operation operation = Operation.None;
    public WeatherServiceCallback weatherServiceCallback;
    private String location;
    private Exception exception;


    public YahooService(WeatherServiceCallback callback){
        this.weatherServiceCallback = callback;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(String location2)
    {
        if(NetworkConnection.isOnline())
        {
            this.location = location2;
            new AsyncTask<String,Void,String>(){
                @SuppressLint("DefaultLocale")
                @Override
                protected String doInBackground(String... strings){
                    String YQL,Endpoint;

                    if(operation == Operation.findByLatLong)
                    {
                        System.out.println(Database.getInstance().getLatitude() + Database.getInstance().getLongitude());
                        YQL ="select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"("+Database.getInstance().getLatitude()+","+ Database.getInstance().getLongitude() +")\")";
                    }

                    else
                        YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")and u= '" + Database.getInstance().getUnit() + "'",strings[0]);

                    Endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                    try{
                        URL url = new URL(Endpoint);
                        URLConnection connection = url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while((line = bufferedReader.readLine())!= null){
                            result.append(line);
                        }
                        return result.toString();
                    }catch(MalformedURLException e)
                    {
                        exception = e;
                    } catch (IOException e) {
                        exception = e;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(String string)
                {
                    try {
                        if(string == null && exception != null)
                        {
                            weatherServiceCallback.serviceFailure(exception);
                            return;
                        }

                        JSONObject data = new JSONObject(string);
                        JSONObject querryResult = data.optJSONObject("query");
                        int count = querryResult.optInt("count");
                        if(count == 0)
                        {
                            Database.getInstance().setWoeidFlag(false);
                            weatherServiceCallback.serviceFailure(new LocationWeatherException("No information found"));
                            return;
                        }
                        if(count>0 && operation==Operation.findByName)
                        {
                            Database.getInstance().setWoeidFlag(true);
                        }

                        Channel channel = new Channel();
                        channel.populate(querryResult.optJSONObject("results").optJSONObject("channel"));
                        weatherServiceCallback.serviceSucces(channel);
                        operation = Operation.None;
                        Database.getInstance().setWoeidFlag(false);

                    } catch (JSONException e) {
                        weatherServiceCallback.serviceFailure(e);
                    }
                }
            }.execute(location);
        }
    }

    public String getLocation(){return  location;}

    public class LocationWeatherException extends Exception
    {
        public LocationWeatherException(String message) {
            super(message);
        }
    }
}
