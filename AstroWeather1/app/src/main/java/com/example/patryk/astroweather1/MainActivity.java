package com.example.patryk.astroweather1;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;

import com.example.patryk.astroweather1.Fragments.ForecastFragment;
import com.example.patryk.astroweather1.Fragments.MoonFragment;
import com.example.patryk.astroweather1.Fragments.Settings;
import com.example.patryk.astroweather1.Fragments.SunFragment;
import com.example.patryk.astroweather1.Fragments.WeatherFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager =findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        networkConnection = new NetworkConnection(this);
        NetworkConnection.isOnline();
        NetworkConnection.NetworkState();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new SunFragment();
                case 1:
                    return new MoonFragment();
                case 2:
                    return new WeatherFragment();
                case 3:
                    return new ForecastFragment();
                case 4:
                    return new Settings();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 5;
        }
    }
}