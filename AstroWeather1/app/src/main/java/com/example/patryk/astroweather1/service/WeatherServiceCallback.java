package com.example.patryk.astroweather1.service;

import com.example.patryk.astroweather1.Data.Channel;

public interface WeatherServiceCallback {
    void serviceSucces(Channel channel);
    void serviceFailure(Exception exception);
}
