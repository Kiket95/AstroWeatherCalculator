package com.example.patryk.astroweather1.Fragments;

import android.content.Intent;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.example.patryk.astroweather1.AstroWeather;
import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.FileManager;
import com.example.patryk.astroweather1.Databases.SQLListData;
import com.example.patryk.astroweather1.Databases.SqlDatabase;
import com.example.patryk.astroweather1.MainActivity;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class Settings extends AppCompatActivity implements  WeatherServiceCallback{

    FileManager fileManager;
    SqlDatabase mDatabaseHelper;
    Switch mySwitch;

    public static EditText latitude,longitude;
    EditText refreshRateValue,cityNameValue;
    Button exitButton,addCityButton,showLocations,searchByData,searchByLocation;
    double latitudeValue,longitudeValue;
    public static String tempLocation;
    SunFragment sunFragment;
    MoonFragment moonFragment;
    YahooService yahooService;
    String newEntry,city;
    private static boolean switchFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        Database.getInstance().setWoeidFlag(false);
        sunFragment = new SunFragment();
        moonFragment = new MoonFragment();
        //     fileManager = new FileManager();
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        cityNameValue = findViewById(R.id.CityNameValue);
        refreshRateValue = findViewById(R.id.refreshRate);
        searchByLocation = findViewById(R.id.searchByLocation);
        exitButton = findViewById(R.id.exitButton);
        addCityButton = findViewById(R.id.AddACity);
        mySwitch = findViewById(R.id.mySwitch);
        showLocations = findViewById(R.id.ViewCities);
        searchByData = findViewById(R.id.searchByData);
        cityNameValue.setText("");
        mDatabaseHelper = new SqlDatabase(Settings.this);
        latitude.setText(String.valueOf(Database.getInstance().getLatitude()));
        longitude.setText(String.valueOf(Database.getInstance().getLongitude()));
        yahooService = new YahooService(this);
        mySwitch.setChecked(switchFlag);
        AstroWeather.setUp();
//        sunFragment.setInfo();
//        sunFragment.setValues();
//        moonFragment.setInfo();
//        moonFragment.setValues();

        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mySwitch.isChecked()) {
                    toastMessage("Imperial System");
                    Database.getInstance().setUnit("f");
                    switchFlag = true;
                } else {
                    toastMessage("Metric System");
                    Database.getInstance().setUnit("c");
                    switchFlag = false;
                }
                yahooService.refreshWeather(Database.getInstance().getLocationName());
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempLocation=Database.getInstance().getLocationName();
                newEntry = cityNameValue.getText().toString();
                if (!(cityNameValue.toString().isEmpty())) {
                    AddData(newEntry);
                    cityNameValue.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
                Database.getInstance().setWoeidFlag(false);
            }
        });
        searchByData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findByDataLatLong();
            }
        });

        showLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, SQLListData.class);
                startActivity(intent);
            }
        });

        searchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempLocation=Database.getInstance().getLocationName();
                city = cityNameValue.getText().toString();
                if(!city.isEmpty())
                {
                    YahooService.operation = YahooService.Operation.findByLocalizationName;
                    yahooService.refreshWeather(city);
                }else toastMessage("cannot find a city");
            }
        });
    }

    private void findByDataLatLong() {
        double tempLatitude = Database.getInstance().getLatitude();
        double tempLongitude = Database.getInstance().getLongitude();
        if (checkValues()) {
            if ((latitudeValue > -90 && latitudeValue < 90) && (longitudeValue > -180 && longitudeValue < 180)) {
                YahooService.operation = YahooService.Operation.findByLatLong;

                Database.getInstance().setLongitude(longitudeValue);
                Database.getInstance().setLatitude(latitudeValue);
                yahooService.refreshWeather(Database.getInstance().getLocationName());
            } else {
                Toast.makeText(Settings.this, "set latitude between -90 and 90 AND set longitude between -180 and 180 ",
                        Toast.LENGTH_SHORT).show();
                Database.getInstance().setLatitude(tempLatitude);
                Database.getInstance().setLongitude(tempLongitude);
            }
        }
    }


    public void AddData(String newEntry) {
        if(!newEntry.isEmpty())
        {
            YahooService.operation = YahooService.Operation.findByName;
            Database.getInstance().setLocationName(newEntry);
            yahooService.refreshWeather(Database.getInstance().getLocationName());
        }else toastMessage("City name cannot be empty");
        Database.getInstance().setWoeidFlag(false);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

//    public void findByData()
//    {
//            try {
//                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                List<Address> addresses = new List<Address>() {
//                    @Override
//                    public int size() {
//                        return  size();
//                    }
//
//                    @Override
//                    public boolean isEmpty() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean contains(Object o) {
//                        return false;
//                    }
//
//                    @NonNull
//                    @Override
//                    public Iterator<Address> iterator() {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public Object[] toArray() {
//                        return new Object[0];
//                    }
//
//                    @NonNull
//                    @Override
//                    public <T> T[] toArray(@NonNull T[] a) {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean add(Address address) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean remove(Object o) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean containsAll(@NonNull Collection<?> c) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean addAll(@NonNull Collection<? extends Address> c) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean addAll(int index, @NonNull Collection<? extends Address> c) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean removeAll(@NonNull Collection<?> c) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean retainAll(@NonNull Collection<?> c) {
//                        return false;
//                    }
//
//                    @Override
//                    public void clear() {
//
//                    }
//
//                    @Override
//                    public Address get(int index) {
//                        return null;
//                    }
//
//                    @Override
//                    public Address set(int index, Address element) {
//                        return null;
//                    }
//
//                    @Override
//                    public void add(int index, Address element) {
//
//                    }
//
//                    @Override
//                    public Address remove(int index) {
//                        return null;
//                    }
//
//                    @Override
//                    public int indexOf(Object o) {
//                        return 0;
//                    }
//
//                    @Override
//                    public int lastIndexOf(Object o) {
//                        return 0;
//                    }
//
//                    @NonNull
//                    @Override
//                    public ListIterator<Address> listIterator() {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public ListIterator<Address> listIterator(int index) {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public List<Address> subList(int fromIndex, int toIndex) {
//                        return null;
//                    }
//                };
//                if(checkValues()){
//                    System.out.println(latitudeValue);
//                    System.out.println(longitudeValue);
//                    System.out.println(latitudeValue);
//                    System.out.println(longitudeValue);
//                    System.out.println(Database.getInstance().getLatitude());
//                    System.out.println(Database.getInstance().getLongitude());
//                    System.out.println(Database.getInstance().getLatitude());
//                    System.out.println(Database.getInstance().getLongitude());
//
//                        if((latitudeValue > -90 && latitudeValue < 90) && (longitudeValue > -180 && longitudeValue < 180) )
//                        {
//                            addresses = geocoder.getFromLocation(latitudeValue, longitudeValue, 1);
//                        }else{
//                            Toast.makeText(Settings.this,"set latitude between -90 and 90 AND set longitude between -180 and 180 ",
//                                    Toast.LENGTH_SHORT).show();
//                            Database.getInstance().setLatitude(latitudeValue);
//                        }
//
//                    if(!addresses.isEmpty() && addresses.get(0) != null)
//                    {
//                        System.out.println(addresses);
//                        System.out.println(addresses);
//                        System.out.println(addresses);
//                        System.out.println(addresses);
//                        System.out.println(addresses);
//                        Database.getInstance().setLongitude(longitudeValue);
//                        Database.getInstance().setLatitude(latitudeValue);
//                      //  tempLocation = Database.getInstance().getLocationName();
//                     //   Database.getInstance().setLocationName(addresses.get(0).getLocality());
//                     //   yahooService.refreshWeather(addresses.get(0).getLocality());
//                        cityNameValue.setText(addresses.get(0).getLocality());
//                        toastMessage("Found Closest City");
//                    }
//                    else
//                    {
//                        latitudeValue = Database.getInstance().getLatitude();
//                        longitudeValue = Database.getInstance().getLongitude();
//                        System.out.println(latitudeValue);
//                        toastMessage("CANNOT FIND CITY");
//                        System.out.println(longitudeValue);
//                    }
//                }
//            }catch (IndexOutOfBoundsException e)
//            {
//                toastMessage("CANNOT FIND CITY");
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//    }

    @Override
    public void serviceSucces(Channel channel) {

        //mDatabaseHelper.getListOfCities();
        String placeName = channel.getLocation().getCity() + ", " + channel.getLocation().getCountry();
        if(Database.getInstance().isWoeidFlag() && !Channel.ErrorFlag)
        {
            if(!mDatabaseHelper.getListOfCities().contains(placeName))
                mDatabaseHelper.addData(placeName);
            else toastMessage("City already exist on your list");

        }
        if(YahooService.operation == YahooService.Operation.findByLocalizationName && !Channel.ErrorFlag)
        {
            Database.getInstance().setLocationName(city);
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        }
        if(YahooService.operation == YahooService.Operation.findByLatLong && !Channel.ErrorFlag)
        {
            Database.getInstance().setLocationName(channel.getLocation().getCity());
            cityNameValue.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());
//            Intent intent = new Intent(Settings.this, MainActivity.class);
//            startActivity(intent);
        }
        if(Channel.ErrorFlag)
        {
            yahooService.refreshWeather(tempLocation);
            Channel.ErrorFlag = false;
        }

//        latitude.setText(String.valueOf(Database.getInstance().getLatitude()));
//        longitude.setText(String.valueOf(Database.getInstance().getLongitude()));
    }

    @Override
    public void serviceFailure(Exception exception) {
         Toast.makeText(Settings.this,exception.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // yahooService.refreshWeather(Database.getInstance().getLocationName());
        Database.getInstance().setWoeidFlag(false);
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean checkValues()
    {
        if(!latitude.getText().toString().isEmpty())
            latitudeValue = Double.parseDouble(latitude.getText().toString());
        else
        {
            Toast.makeText(Settings.this,"WRONG OR EMPTY longitude(ONLY DIGITS)",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!longitude.getText().toString().isEmpty())
            longitudeValue = Double.parseDouble(longitude.getText().toString());
        else
        {
            Toast.makeText(Settings.this,"WRONG OR EMPTY longitude(ONLY DIGITS)",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
