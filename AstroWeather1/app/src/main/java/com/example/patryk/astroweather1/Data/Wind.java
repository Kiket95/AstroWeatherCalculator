package com.example.patryk.astroweather1.Data;

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
        direction = data.optInt("direction");
        speed = data.optInt("speed");
    }
}
