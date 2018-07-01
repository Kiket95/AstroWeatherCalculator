package com.example.patryk.astroweather1.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind implements JSON {
    public int getDirection() {
        return direction;
    }

    private int direction;

    public int getSpeed() {
        return speed;
    }

    private int speed;
    @Override
    public void populate(JSONObject data) {
        try {
            direction = data.optInt("direction");
            speed = data.optInt("speed");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("direction",direction);
            data.put("speed",speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
