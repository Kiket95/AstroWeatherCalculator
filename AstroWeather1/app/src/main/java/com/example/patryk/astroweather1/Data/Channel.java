package com.example.patryk.astroweather1.Data;

import org.json.JSONObject;

public class Channel implements JSON {

    public MyLocation getLocation() {
        return location;
    }
    private MyLocation location;

    public Wind getWind() {
        return wind;
    }
    private Wind wind;

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }
    private Atmosphere atmosphere;

    public Units getUnit() {
        return unit;
    }
    private Units unit;

    public Item getItem() {
        return item;
    }
    private Item item;


    @Override
    public void populate(JSONObject data) {
        this.unit = new Units();
        this.unit.populate(data.optJSONObject("units"));
        this.item = new Item();
        this.item.populate(data.optJSONObject("item"));
        this.atmosphere = new Atmosphere();
        this.atmosphere.populate(data.optJSONObject("atmosphere"));
        this.wind = new Wind();
        this.wind.populate(data.optJSONObject("wind"));
        this.location = new MyLocation();
        this.location.populate(data.optJSONObject("location"));
    }
}