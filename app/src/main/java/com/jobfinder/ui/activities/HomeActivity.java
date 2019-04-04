package com.jobfinder.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jobfinder.R;
import com.jobfinder.databinding.ActivityMainBind;
import com.jobfinder.handler.FragmentHandler;
import com.jobfinder.services.LocationProvider;
import com.jobfinder.ui.fragment.Jobs;
import com.jobfinder.ui.fragment.SavedJobs;
import com.jobfinder.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class HomeActivity extends BaseActivity {
    ActivityMainBind binding;
    public Location mLocation;
    public LocationProvider locationProvider;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_jobs:
                    //Open Fragment jobs
                    FragmentHandler.replaceFragment(HomeActivity.this, Jobs.newInstance(), R.id.container, false);
                    return true;
                case R.id.navigation_saved_jobs:
                    //Open Fragment Saved jobs
                    FragmentHandler.replaceFragment(HomeActivity.this, SavedJobs.newInstance(), R.id.container, false);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentHandler.replaceFragment(this, Jobs.newInstance(), R.id.container, false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationProvider = new LocationProvider(this);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for location settings
        if (requestCode == Constants.REQUEST_CHECK_SETTINGS_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //"User agreed to make required location settings changes.
                    locationProvider.checkPermissionsAndStartScreenSetup();
                    break;

                case Activity.RESULT_CANCELED:
                    //User chose not to make required location settings changes.
                    Log.d(Constants.TAG, "HomeActivity Location not enabled");
                    break;
            }
        }
    }

    @Subscribe
    public void onEvent_Location(Location location) {
        mLocation = location;
    }
}
