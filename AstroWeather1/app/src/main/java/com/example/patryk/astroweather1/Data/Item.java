package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public class Item implements JSON {

    public Condition getCondition() {
        return condition;
    }
    private double latitude;
    private double longitude;
    private Condition condition;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
        latitude = data.optDouble("lat");
        longitude = data.optDouble("long");
    }
}
