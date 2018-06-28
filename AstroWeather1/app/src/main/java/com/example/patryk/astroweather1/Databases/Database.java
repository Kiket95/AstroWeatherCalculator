package com.example.patryk.astroweather1.Databases;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Patryk on 2018-06-10.
 */

public class Database {
    private double longitude;
    private double latitude;
    private int refreshRate;

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
