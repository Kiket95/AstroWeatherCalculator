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

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager =findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return SunFragment.newInstance(0,"Sun");
                case 1:
                    return MoonFragment.newInstance(0,"Moon");
                case 2:
                    return WeatherFragment.newInstance(0,"Weather");
                case 3:
                    return ForecastFragment.newInstance(0,"Forecast");
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