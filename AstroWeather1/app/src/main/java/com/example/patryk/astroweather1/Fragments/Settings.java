package com.example.patryk.astroweather1.Fragments;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.example.patryk.astroweather1.AstroWeather;
import com.example.patryk.astroweather1.Data.Channel;
import com.example.patryk.astroweather1.Data.Item;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.FileManager;
import com.example.patryk.astroweather1.Databases.SQLListData;
import com.example.patryk.astroweather1.Databases.SqlDatabase;
import com.example.patryk.astroweather1.R;
import com.example.patryk.astroweather1.service.WeatherServiceCallback;
import com.example.patryk.astroweather1.service.YahooService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Settings extends Fragment implements WeatherServiceCallback{

    FileManager fileManager;
    SqlDatabase mDatabaseHelper;
    Switch mySwitch;
    public static EditText latitude,longitude;
    EditText refreshRateValue,cityNameValue;
    Button saveButton,exitButton,addCityButton,showLocations,searchByName;
    double latitudeValue,longitudeValue;
    int frequency;
    SunFragment sunFragment;
    MoonFragment moonFragment;
    YahooService yahooService;
    String newEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.settings_main, container, false);
        yahooService = new YahooService(this);
        sunFragment = new SunFragment();
        moonFragment = new MoonFragment();
        fileManager = new FileManager();
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
        cityNameValue = view.findViewById(R.id.CityNameValue);
        refreshRateValue = view.findViewById(R.id.refreshRate);
        saveButton = view.findViewById(R.id.saveButton);
        exitButton = view.findViewById(R.id.exitButton);
        addCityButton = view.findViewById(R.id.AddACity);
        mySwitch = view.findViewById(R.id.mySwitch);
        showLocations = view.findViewById(R.id.ViewCities);
        searchByName = view.findViewById(R.id.searchByData);
        cityNameValue.setText("");
        mDatabaseHelper = new SqlDatabase(getContext());
        latitude.setText(String.valueOf(Database.getInstance().getLatitude()));
        longitude.setText(String.valueOf(Database.getInstance().getLongitude()));


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!latitude.getText().toString().isEmpty())
                    latitudeValue = Double.parseDouble(latitude.getText().toString());
                else Toast.makeText(getActivity(),"WRONG OR EMPTY Latitude (ONLY DIGITS)",
                        Toast.LENGTH_SHORT).show();
                if(!longitude.getText().toString().isEmpty())
                    longitudeValue = Double.parseDouble(longitude.getText().toString());
                else Toast.makeText(getActivity(),"WRONG OR EMPTY longitude(ONLY DIGITS)",
                        Toast.LENGTH_SHORT).show();
                if(!refreshRateValue.getText().toString().isEmpty())
                    frequency = Integer.parseInt(refreshRateValue.getText().toString());
                else Toast.makeText(getActivity(),"WRONG OR EMPTY refresh rate(ONLY DIGITS)",
                        Toast.LENGTH_SHORT).show();

                try
                {
                    if(latitudeValue > -90 && latitudeValue < 90 )
                        Database.getInstance().setLatitude(latitudeValue);
                    else
                        Toast.makeText(getActivity(),"set latitude between -90 and 90",
                                Toast.LENGTH_SHORT).show();

                    if (longitudeValue > -180 && longitudeValue < 180)
                        Database.getInstance().setLongitude(longitudeValue);
                    else    Toast.makeText(getActivity(),"set longitude between -180 and 180",
                            Toast.LENGTH_SHORT).show();

                 //   if(frequency >= 10 && frequency <= 100)
                        Database.getInstance().setRefreshRate(frequency);
                 //   else  Toast.makeText(getActivity(),"Time must be a value between 10 and 100",
                 //           Toast.LENGTH_SHORT).show();
                }catch (NumberFormatException  e)
                {
                    Toast.makeText(getActivity(),"ERROR",
                            Toast.LENGTH_SHORT).show();
                }


                AstroWeather.setUp();
                sunFragment.setInfo();
                sunFragment.setValues();
                moonFragment.setInfo();
                moonFragment.setValues();
                fileManager.saveFile();
            }
        });

        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mySwitch.isChecked()) {
                    toastMessage("Imperial System");
                    yahooService.setUnit("f");
                    yahooService.refreshWeather(Database.getInstance().getLocationName());
                } else {
                    toastMessage("Metric System");
                    yahooService.setUnit("c");
                    yahooService.refreshWeather(Database.getInstance().getLocationName());
                }
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
                newEntry = cityNameValue.getText().toString();
                if (!(cityNameValue.toString().isEmpty())) {
                    AddData(newEntry);
                    cityNameValue.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
            }
        });
        searchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findByName();
            }
        });

        showLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SQLListData.class);
                startActivity(intent);
            }
        });
        return  view;
    }

    public void AddData(String newEntry) {
        if(!newEntry.isEmpty())
        {
//            Database.getInstance().setWoeidFlag(true);
//            Database.getInstance().setWoeid((Integer.parseInt(cityNameValue.getText().toString())));
//            yahooService.refreshWeather(Database.getInstance().getLocationName());
            boolean insertData = mDatabaseHelper.addData(newEntry);
            if (insertData) {
                toastMessage("Data Successfully Inserted!");
            } else {
                toastMessage("Something went wrong");
            }
      //      Database.getInstance().setWoeidFlag(false);

        }else {toastMessage("Invalid Data");}
    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void serviceSucces(Channel channel) {
        Item item = channel.getItem();
        longitude.setText(Double.toString(item.getLongitude()));
        latitude.setText(Double.toString(item.getLatitude()));
        Database.getInstance().setLatitude(item.getLatitude());
        Database.getInstance().setLongitude(item.getLongitude());

        if(Database.getInstance().isWoeidFlag())
        {
            boolean insertData = mDatabaseHelper.addData("Lodz");
            if (insertData) {
                toastMessage("Data Successfully Inserted!");
            } else {
                toastMessage("Something went wrong");
            }
            Database.getInstance().setWoeidFlag(false);
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        if(Database.getInstance().isWoeidFlag())
        {
            toastMessage("WRONG NAME OF CITY");
        }
    }

    public void findByName()
    {
        try{
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(64.499474, -165.405792, 1);
            longitude.setText(Double.toString(-165.405792));
            latitude.setText(Double.toString(64.499474));
            Database.getInstance().setLocationName(addresses.get(0).getLocality());
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
