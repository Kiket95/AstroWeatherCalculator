package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public class Condition implements JSON {

    private int code;
    private String description;
    private int temperature;

    public String getTime() {
        return time;
    }

    private String time;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        description = data.optString("text");
        temperature = data.optInt("temp");
        time = data.optString("date");

    }
}
