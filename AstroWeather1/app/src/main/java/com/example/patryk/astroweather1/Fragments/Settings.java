package com.example.patryk.astroweather1.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.patryk.astroweather1.AstroWeather;
import com.example.patryk.astroweather1.Databases.Database;
import com.example.patryk.astroweather1.Databases.FileManager;
import com.example.patryk.astroweather1.Databases.SQLListData;
import com.example.patryk.astroweather1.Databases.SqlDatabase;
import com.example.patryk.astroweather1.R;

public class Settings extends Fragment {

    FileManager fileManager;
    SqlDatabase mDatabaseHelper;
    EditText latitude,longitude,refreshRateValue,cityNameValue;
    Button saveButton,exitButton,addCityButton,showLocations;
    ListView listOfCities;
    double latitudeValue,longitudeValue;
    int frequency;
    SunFragment sunFragment;
    MoonFragment moonFragment;
    String regex = "\\d+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.settings_main, container, false);
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
        listOfCities = view.findViewById(R.id.CityList);
        showLocations = view.findViewById(R.id.ViewCities);
        cityNameValue.setText("");
        mDatabaseHelper = new SqlDatabase(getContext());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!latitude.getText().toString().isEmpty()&& latitude.getText().toString().matches(regex))
                    latitudeValue = Double.parseDouble(latitude.getText().toString());
                else Toast.makeText(getActivity(),"WRONG OR EMPTY Latitude (ONLY DIGITS)",
                        Toast.LENGTH_SHORT).show();
                if(!longitude.getText().toString().isEmpty()&& longitude.getText().toString().matches(regex))
                    longitudeValue = Double.parseDouble(longitude.getText().toString());
                else Toast.makeText(getActivity(),"WRONG OR EMPTY longitude(ONLY DIGITS)",
                        Toast.LENGTH_SHORT).show();
                if(!refreshRateValue.getText().toString().isEmpty() && refreshRateValue.getText().toString().matches(regex))
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

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = cityNameValue.getText().toString();
                if (!(cityNameValue.toString().isEmpty())) {
                    AddData(newEntry);
                    cityNameValue.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
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
            boolean insertData = mDatabaseHelper.addData(newEntry);
            if (insertData) {
                Database.getInstance().setLocationName(newEntry);
                toastMessage("Data Successfully Inserted!");
            } else {
                toastMessage("Something went wrong");
            }
        }else {toastMessage("Invalid Data");}
    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }


}
