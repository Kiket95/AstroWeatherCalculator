package com.example.patryk.astroweather1.Data;

import android.util.Log;

import org.json.JSONObject;

public class Forecast implements JSON {

    private String date;
    private String day;
    private String code;
    private double high;
    private double low;
    private String text;

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getCode() {
        return code;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public String getText() {
        return text;
    }

    @Override
    public void populate(JSONObject data) {
        Log.d("INIT: ", data.toString());
        this.date = data.optString("date");
        this.day = data.optString("day");
        this.code = data.optString("code");
        this.high = data.optDouble("high");
        this.low = data.optDouble("low");
        this.text = data.optString("text");
    }

}
