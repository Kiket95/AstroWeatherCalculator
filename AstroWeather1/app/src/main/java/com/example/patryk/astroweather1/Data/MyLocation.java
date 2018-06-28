package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public class MyLocation implements JSON {

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    private String city;
    private String country;

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
    }
}
