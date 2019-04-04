package com.jobfinder.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobfinder.R;
import com.jobfinder.databinding.FragmentSavedJobBind;
import com.jobfinder.utils.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

public class SavedJobs extends BaseFragment {
    FragmentSavedJobBind binding;
    private SavedJobsListener mListener;

    public static SavedJobs newInstance() {
        return new SavedJobs();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_jobs, container, false);

        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage("Feature Coming soon...")
                .setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        setupComponents(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    void initializeComponents(View rootView) {

    }

    @Override
    void setupListeners(View rootView) {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SavedJobsListen(Constants.Back);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (SavedJobs.SavedJobsListener) context;
    }

    public interface SavedJobsListener {
        void SavedJobsListen(int option);
    }
}
