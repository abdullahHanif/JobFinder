package com.jobfinder.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jobfinder.R;
import com.jobfinder.databinding.ActivityMainBind;
import com.jobfinder.handler.FragmentHandler;
import com.jobfinder.ui.fragment.Jobs;
import com.jobfinder.utils.Utils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class HomeActivity extends BaseActivity {
    ActivityMainBind binding;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_jobs:
                    //Open Fragment jobs
                    FragmentHandler.replaceFragment(HomeActivity.this, Jobs.newInstance(), R.id.container, false);
                    return true;
                case R.id.navigation_profile:
                    FragmentHandler.replaceFragment(HomeActivity.this, Jobs.newInstance(), R.id.container, false);
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
}
