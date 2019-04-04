package com.jobfinder.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class SavedJobs extends BaseFragment {


    public static SavedJobs newInstance() {
        return new SavedJobs();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage("Feature Coming soon...")
                .setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void initializeComponents(View rootView) {

    }

    @Override
    void setupListeners(View rootView) {

    }
}
