package com.example.patryk.astroweather1.Databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;


import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;


public class FileDataManager {
    private Context context;
    private Exception error;
    private final String FILE_NAME = "AstroWeatherData.data";
    private FileDataManager weatherCacheService;

    public FileDataManager(Context context){
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    public void save(Channel channel){
        new AsyncTask<Channel,Void, Void>(){

            @Override
            protected Void doInBackground(Channel... channels) {
                FileOutputStream outputStream;

                try {
                    outputStream = context.openFileOutput(FILE_NAME,Context.MODE_PRIVATE);
                    outputStream.write(channels[0].toJSON().toString().getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(channel);
    }
    @SuppressLint("StaticFieldLeak")
    public void load(final WeatherServiceCallback listener){
        new AsyncTask<WeatherServiceCallback,Void,Channel>(){
            private WeatherServiceCallback weatherServiceCallback;
            @Override
            protected Channel doInBackground(WeatherServiceCallback... weatherServiceCallbacks) {
                weatherServiceCallback = weatherServiceCallbacks[0];

                try {
                    FileInputStream inputStream = context.openFileInput(FILE_NAME);
                    StringBuilder cache = new StringBuilder();
                    int content;
                    while((content = inputStream.read()) != -1){
                        cache.append((char) content);
                    }
                    inputStream.close();

                    JSONObject jsonCache = new JSONObject(cache.toString());
                    Channel channel = new Channel();
                    channel.populate(jsonCache);
                    return channel;
                } catch (FileNotFoundException e) {
                    error = new CacheException("File not found!");
                }catch (Exception e){
                    error = e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Channel channel){
                if(channel == null && error != null){
                    weatherServiceCallback.serviceFailure(error);
                }else {
                    weatherServiceCallback.serviceSucces(channel);
                }
            }


        }.execute(listener);
    }

    private class CacheException extends Exception{
        CacheException(String detailMessage){
            super(detailMessage);
        }
    }
}