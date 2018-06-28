package com.example.patryk.astroweather1.service;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.SqlDatabase;
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
    private  WeatherServiceCallback weatherServiceCallback;
    private  String location;
    private  Exception exception;
    private  boolean SuccesFlag = false;
    private static String unit = "c";

    public  String getUnit() {
        return unit;
    }

    public  void setUnit(String unit) {
        this.unit = unit;
    }

    public  boolean isSuccesFlag() {
        return SuccesFlag;
    }

    public  void setSuccesFlag(boolean succesFlag) {
        SuccesFlag = succesFlag;
    }


    public YahooService(WeatherServiceCallback callback){
        this.weatherServiceCallback = callback;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(String location2)
    {
        if(NetworkConnection.isOnline())
        {
            location = location2;
            new AsyncTask<String,Void,String>(){
                @Override
                protected String doInBackground(String... strings){
                    String YQL,Endpoint;
                    if(Database.getInstance().isWoeidFlag())
                    {
                        YQL = String.format("select * from weather.forecast where woeid=%s) and u='"+ unit +"'",String.valueOf(Database.getInstance().getWoeid()));
                        Endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                    }else
                    {
                        YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")and u= '" + unit + "'",strings[0]);

                        Endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                    }

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
                            setSuccesFlag(false);
                            weatherServiceCallback.serviceFailure(new LocationWeatherException("No information found for: " + location));
                            return;
                        }
                        else setSuccesFlag(true);

                        Channel channel = new Channel();
                        channel.populate(querryResult.optJSONObject("results").optJSONObject("channel"));
                        weatherServiceCallback.serviceSucces(channel);
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
