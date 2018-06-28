package com.example.patryk.astroweather1.service;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Databases.Database;

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
    private static WeatherServiceCallback weatherServiceCallback;
    private static String location;
    private static Exception exception;

    public YahooService(WeatherServiceCallback callback){
        this.weatherServiceCallback = callback;
    }

    @SuppressLint("StaticFieldLeak")
    public static void refreshWeather(String location2)
    {
        location = location2;
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings){
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")",strings[0]);
                String Endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
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
                        weatherServiceCallback.serviceFailure(new LocationWeatherException("No information found for: " + location));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.populate(querryResult.optJSONObject("results").optJSONObject("channel"));

                    weatherServiceCallback.serviceSucces(channel);

                } catch (JSONException e) {
                    weatherServiceCallback.serviceFailure(e);
                }
            }
        }.execute(location);
    }

    public String getLocation(){return  location;}

    public static class LocationWeatherException extends Exception
    {

        public LocationWeatherException(String message) {
            super(message);
        }
    }
}
