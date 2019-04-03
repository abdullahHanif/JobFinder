package com.jobfinder.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobfinder.R;
import com.jobfinder.databinding.FragmentJobBind;
import com.jobfinder.databinding.PopupSettingBind;
import com.jobfinder.model.Job;
import com.jobfinder.model.JobPagination;
import com.jobfinder.network.AppConfig;
import com.jobfinder.network.NetworkCallHandler;
import com.jobfinder.ui.adapter.JobsAdapter;
import com.jobfinder.utils.Constants;
import com.jobfinder.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Jobs extends BaseFragment implements JobsAdapter.JobsAdapterListener {

    FragmentJobBind binding;
    PopupSettingBind popupSettingBinding;
    AlertDialog dialog;
    JobPagination jobs;
    int currentpage = 1;
    int lastpage = 1;
    private LinearLayoutManager linearLayoutManager;
    JobsAdapter adapter;


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
        binding.tolBarTitle.setText("Job Finder");
        setUpAdapter(new ArrayList<Job>());
        getJobs("https://jobs.github.com/positions.json?description=python&location=new+york");
    }

    private void getJobs(String URL) {
        showDialog();
        NetworkCallHandler.get(URL, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //adding one data key in root of API response as array list parsing error occurs without data key in root
                    String responsee = "{\"data\":" + response.body().string() + "}";
                    jobs = Utils.GsonUtils.fromJSON(responsee, JobPagination.class);

                    if (jobs != null && !jobs.getJobsList().isEmpty()) {
                        setUpAdapter(jobs.getJobsList());
                    } else {
                        setUpAdapter(new ArrayList<Job>());
                        binding.EmptyRecyclerView.setVisibility(View.VISIBLE);
                        binding.noEventFound.setText("No Data Found...");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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

    void setUpAdapter(ArrayList<Job> arrayList) {
        linearLayoutManager = new LinearLayoutManager(context);
        binding.commonRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new JobsAdapter(context, arrayList, this);
        binding.commonRecyclerView.setAdapter(adapter);
    }


    @Override
    public void JobsAdapterListen(int option, Job job) {
        switch (option) {
            case Constants.ITEM_OPEN:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(job.getUrl()));
                startActivity(i);
                break;
        }
    }
}
