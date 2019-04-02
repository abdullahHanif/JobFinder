package com.jobfinder.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jobfinder.R;
import com.jobfinder.databinding.FragmentJobBind;
import com.jobfinder.databinding.PopupSettingBind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

public class Jobs extends BaseFragment {

    FragmentJobBind binding;
    PopupSettingBind popupSettingBinding;
    AlertDialog dialog;

    public static Jobs newInstance() {
        Jobs jobs = new Jobs();
        return jobs;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        popupSettingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popup_setting, null, false);

        setupComponents(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    void initializeComponents(View rootView) {

    }

    @Override
    void setupListeners(View rootView) {
        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popupSettingBinding.getRoot().getParent() != null) {
                    ((ViewGroup) popupSettingBinding.getRoot().getParent()).removeView(popupSettingBinding.getRoot());
                }
                dialog = new AlertDialog.Builder(context)
                        .setView(popupSettingBinding.getRoot())
                        .setCancelable(false)
                        .create();

                if (!dialog.isShowing()) {
                    dialog.show();
                }

            }
        });

        popupSettingBinding.btnSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        popupSettingBinding.SettingPopupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


    }
}
