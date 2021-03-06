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
                    forecast[i].populate(jsonArray.getJSONObject(i));

            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {

            data.put("lat", latitude);
            data.put("long", longitude);
            data.put("condition",condition.toJSON());
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<7;i++){
                jsonArray.put(forecast[i].toJSON());
            }

            data.put("forecast",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
