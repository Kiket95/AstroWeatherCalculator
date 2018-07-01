package com.example.patryk.astroweather1.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Units implements JSON {

    private String temperature;

    public String getSpeed() {
        return speed;
    }

    public String getPressure() {
        return pressure;
    }

    private String speed;
    private String pressure;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
        pressure = data.optString("pressure");
        speed = data.optString("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("temperature",temperature);
            data.put("pressure",pressure);
            data.put("speed",speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
