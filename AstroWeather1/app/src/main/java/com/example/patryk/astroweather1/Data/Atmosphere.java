package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public class Atmosphere implements JSON {
    public int getPressure() {
        return pressure;
    }

    private int pressure;

    @Override
    public void populate(JSONObject data) {
        try{
            pressure = data.optInt("pressure");
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
