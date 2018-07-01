package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public interface JSON {
     void populate(JSONObject data);
     JSONObject toJSON();
}
