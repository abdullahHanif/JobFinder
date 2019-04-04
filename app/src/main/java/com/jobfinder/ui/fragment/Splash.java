package com.jobfinder.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobfinder.R;
import com.jobfinder.databinding.FragmentSplashBind;
import com.jobfinder.handler.FragmentHandler;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class Splash extends BaseFragment {

    FragmentSplashBind binding;

    public static Splash newInstance() {
        Splash splash = new Splash();
        return splash;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        setupComponents(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    void initializeComponents(View rootView) {
        animateSplashLogo();
    }

    private void animateSplashLogo() {
        binding.splashLogo.animate().scaleX(0).scaleY(0).setDuration(700).setStartDelay(2000).withLayer().start();
        final int[] timerWait = new int[1];
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //close this and open new Frag
                FragmentHandler.replaceFragment(context, Jobs.newInstance(), R.id.container, false);
            }
        }, 3000);
    }

    @Override
    void setupListeners(View rootView) {

    }


}
