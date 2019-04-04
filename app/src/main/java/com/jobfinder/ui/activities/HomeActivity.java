package com.jobfinder.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jobfinder.R;
import com.jobfinder.databinding.ActivityMainBind;
import com.jobfinder.handler.FragmentHandler;
import com.jobfinder.services.LocationProvider;
import com.jobfinder.ui.fragment.Jobs;
import com.jobfinder.ui.fragment.SavedJobs;
import com.jobfinder.ui.fragment.Splash;
import com.jobfinder.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class HomeActivity extends BaseActivity implements SavedJobs.SavedJobsListener {
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
                    FragmentHandler.replaceFragment(HomeActivity.this, SavedJobs.newInstance(), R.id.container, true);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeComponents();

        FragmentHandler.replaceFragment(this, Splash.newInstance(), R.id.container, false);
    }

    void initializeComponents() {
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //delayed because of splash
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationProvider = new LocationProvider(HomeActivity.this);
                binding.navigation.setVisibility(View.VISIBLE);
            }
        }, 3050);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if activity is killed as soon as it was created...
        EventBus.getDefault().unregister(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationProvider.stopLocationUpdates();
            }
        }, 3100);

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
                    Log.d(Constants.TAG, "Location not enabled");
                    break;
            }
        }
    }

    @Subscribe
    public void onEvent_Location(Location location) {
        mLocation = location;
    }

    @Override
    public void SavedJobsListen(int option) {
        switch (option) {
            case Constants.Back:
                binding.navigation.getMenu().getItem(0).setChecked(true);
                popStack();
                break;
        }
    }
}
