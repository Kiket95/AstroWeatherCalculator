package com.example.patryk.astroweather1.Databases;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Database {
    private double longitude;
    private double latitude;
    private int refreshRate;

    public boolean isWoeidFlag() {
        return isWoeidFlag;
    }

    public void setWoeidFlag(boolean WoeidFlag) {
        isWoeidFlag = WoeidFlag;
    }

    public int getWoeid() {
        return Woeid;
    }

    public void setWoeid(int woeid) {
        Woeid = woeid;
    }

    private boolean isWoeidFlag;
    private int Woeid;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private static String locationName = "Sydney, Australia";
    private static final Database instance = new Database();

    private Database(){
        longitude = 0;
        latitude = 0;
        refreshRate = 10;
        isWoeidFlag = false;
        Woeid = 0;
        locationName = "Sydney, Australia";
    }

    public static Database getInstance(){
        return instance;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

}
