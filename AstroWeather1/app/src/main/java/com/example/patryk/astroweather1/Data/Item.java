package com.example.patryk.astroweather1.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item implements JSON {

    public Condition getCondition() {
        return condition;
    }
    private double latitude;
    private double longitude;
    private Condition condition;

    public Forecast[] getForecast() {
        return forecast;
    }

    private Forecast[] forecast = new Forecast[7];

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public void populate(JSONObject data) {
        try{
            condition = new Condition();
            condition.populate(data.optJSONObject("condition"));
            latitude = data.optDouble("lat");
            longitude = data.optDouble("long");
            JSONArray jsonArray = data.optJSONArray("forecast");
            for(int i = 0; i < 7; i++) {

                    forecast[i] = new Forecast();
                    forecast[i].populate(jsonArray.getJSONObject(i+1));

            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
