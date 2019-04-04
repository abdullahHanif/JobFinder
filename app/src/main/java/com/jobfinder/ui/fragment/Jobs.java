package com.jobfinder.ui.fragment;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jobfinder.R;
import com.jobfinder.databinding.FragmentJobBind;
import com.jobfinder.databinding.PopupSettingBind;
import com.jobfinder.model.Job;
import com.jobfinder.model.JobPagination;
import com.jobfinder.network.AppConfig;
import com.jobfinder.network.NetworkCallHandler;
import com.jobfinder.ui.activities.HomeActivity;
import com.jobfinder.ui.adapter.JobsAdapter;
import com.jobfinder.utils.Constants;
import com.jobfinder.utils.SharedPrefManager;
import com.jobfinder.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
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
    Double Lat, Lng;


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
        prepareBaseURL();
    }

    //method to get data from server as per filters if default filter value is git hub and sort is latlng.
    private void prepareBaseURL() {
        String api_provider = SharedPrefManager.getInstance().getString(Constants.API_PROVIDER, "");
        String sort_type = SharedPrefManager.getInstance().getString(Constants.SORT_TYPE, "");

        if (api_provider.equals("")) {
            SharedPrefManager.getInstance().putString(Constants.API_PROVIDER, Constants.GITHUB);
        }

        if (sort_type.equals("")) {
            SharedPrefManager.getInstance().putString(Constants.SORT_TYPE, Constants.DATE);
        }

        switch (api_provider) {
            case Constants.GITHUB:
                AppConfig.BASE_URL = "https://jobs.github.com/positions.json?description=python&location=new+york/";
                break;
            case Constants.SEARCH_GOV:
                AppConfig.BASE_URL = "https://jobs.search.gov/jobs/search.json?query=nursing+jobs/";
                break;
        }

        switch (sort_type) {
            case Constants.LOCATION:
                if (((HomeActivity) context).mLocation != null) {
                    Lat = ((HomeActivity) context).mLocation.getLatitude();
                    Lng = ((HomeActivity) context).mLocation.getLongitude();
                } else {
                    binding.pullToRefresh.setRefreshing(false);
                    binding.commonRecyclerView.setVisibility(View.GONE);
                    binding.EmptyRecyclerView.setVisibility(View.VISIBLE);
                    binding.noEventFound.setText("Please Enable Locaiton to see Jobs...");
                    if (((HomeActivity) context).locationProvider != null) {
                        ((HomeActivity) context).locationProvider.checkPermissionsAndStartScreenSetup();
                    }
                }

                break;
            case Constants.DATE:
                break;
        }

        getJobs(AppConfig.BASE_URL);
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
                    binding.pullToRefresh.setRefreshing(false);

                    if (jobs != null && !jobs.getJobsList().isEmpty()) {
                        setUpAdapter(jobs.getJobsList());
                    } else {
                        binding.commonRecyclerView.setVisibility(View.GONE);
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
                    //Setting API types as user setting saved in persistence and will be retrieve when api is called.
                    switch (popupSettingBinding.rdoGroupAPIProvider.getCheckedRadioButtonId()) {
                        case R.id.rdoBtnGithub:
                            SharedPrefManager.getInstance().putString(Constants.API_PROVIDER, Constants.GITHUB);
                            break;
                        case R.id.rdoBtnSearchGov:
                            SharedPrefManager.getInstance().putString(Constants.API_PROVIDER, Constants.SEARCH_GOV);
                            break;
                    }
                    //Setting filter types as user setting saved in persistence and will be retrieve when api is called.

                    switch (popupSettingBinding.rdoGroupSort.getCheckedRadioButtonId()) {
                        case R.id.rdoBtnLocation:
                            SharedPrefManager.getInstance().putString(Constants.SORT_TYPE, Constants.LOCATION);
                            break;
                        case R.id.rdoBtnDate:
                            SharedPrefManager.getInstance().putString(Constants.SORT_TYPE, Constants.DATE);
                            break;
                    }
                    Toast.makeText(context, "Settings Saved.", Toast.LENGTH_SHORT).show();

                    prepareBaseURL();

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
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

        binding.pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareBaseURL();
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
