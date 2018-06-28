package com.example.patryk.astroweather1;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.patryk.astroweather1.Databases.Database;

import java.util.Calendar;


public class AstroWeather {

    private static Calendar calendar;
    private static AstroCalculator.SunInfo sunInfo;
    private static AstroCalculator.MoonInfo moonInfo;
    private static AstroDateTime astroDateTime ;

    public static void setUp(){
        calendar = Calendar.getInstance();
        astroDateTime = new AstroDateTime(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH ) + 1,
                calendar.get(Calendar.DAY_OF_MONTH ),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.ZONE_OFFSET)/360000,
                true
        );
        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime,new AstroCalculator.Location(Database.getInstance().getLatitude(),Database.getInstance().getLongitude()));
        sunInfo = astroCalculator.getSunInfo();
        moonInfo = astroCalculator.getMoonInfo();
    }

    public static AstroCalculator.SunInfo getSunInfo(){
        return sunInfo;
    }
    public static AstroCalculator.MoonInfo getMoonInfo(){
        return moonInfo;
    }


}
