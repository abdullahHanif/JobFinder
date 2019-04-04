package com.jobfinder.ui.fragment;

import android.app.DownloadManager;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.jobfinder.R;
import com.jobfinder.databinding.FragmentJobBind;
import com.jobfinder.databinding.PopupSettingBind;
import com.jobfinder.model.Job;
import com.jobfinder.model.JobPagination;
import com.jobfinder.network.AppConfig;
import com.jobfinder.network.NetworkCallHandler;
import com.jobfinder.services.LocationProvider;
import com.jobfinder.ui.activities.HomeActivity;
import com.jobfinder.ui.adapter.JobsAdapter;
import com.jobfinder.utils.Constants;
import com.jobfinder.utils.SharedPrefManager;
import com.jobfinder.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    LinearLayoutManager linearLayoutManager;
    JobsAdapter adapter;
    Double Lat, Lng;
    private static Location mLocation;
    int API_CHECKED_INDEX, SORT_CHECKED_INDEX;

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
        prepareBaseURLAndFetchData("");
    }

    //method to get data from server as per filters if default filter value is git hub and sort is date.
    private void prepareBaseURLAndFetchData(String query) {
        binding.commonRecyclerView.setVisibility(View.GONE);
        binding.EmptyRecyclerView.setVisibility(View.GONE);

        String local_api_provider = SharedPrefManager.getInstance().getString(Constants.API_PROVIDER, "");
        String local_sort_type = SharedPrefManager.getInstance().getString(Constants.SORT_TYPE, "");


        //setting default data provider to Github
        if (local_api_provider.equalsIgnoreCase("") || local_api_provider.equalsIgnoreCase(Constants.GITHUB)) {
            SharedPrefManager.getInstance().putString(Constants.API_PROVIDER, Constants.GITHUB);
            local_api_provider = Constants.GITHUB;
            AppConfig.BASE_URL = AppConfig.GITHUB_BASE_URL;
        } else if (local_api_provider.equalsIgnoreCase(Constants.SEARCH_GOV)) {
            AppConfig.BASE_URL = AppConfig.SEARCH_GOV_BASE_URL;
            local_api_provider = Constants.SEARCH_GOV;
        }
        //setting default data provider to SortType to Date
        if (local_sort_type.equalsIgnoreCase("")) {
            SharedPrefManager.getInstance().putString(Constants.SORT_TYPE, Constants.DATE);
            local_sort_type = Constants.DATE;
        }

        switch (local_sort_type) {
            case Constants.LOCATION:
                //becasue fragment might be destroyed while navigation so check if activity's location has value...
                if (mLocation == null && ((HomeActivity) context).mLocation != null) {
                    mLocation = ((HomeActivity) context).mLocation;
                }

                if (mLocation != null) {
                    Lat = mLocation.getLatitude();
                    Lng = mLocation.getLongitude();

                    //calling github url with query and without any query
                    if (!query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.GITHUB)) {
                        getJobs(AppConfig.BASE_URL + "?description=" + query + "&lat=" + Lat + "&long=" + Lng);
                    }
                    //Calling if search query is empty loading all positions nearby me
                    else if (query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.GITHUB)) {
                        getJobs(AppConfig.BASE_URL + "?lat=" + Lat + "&long=" + Lng);
                    }

                    //calling Search.gov url with query and without any query
                    if (!query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.SEARCH_GOV)) {
                        getJobs(AppConfig.BASE_URL + "?query=" + query + "&lat_lon=" + Lat + "," + Lng);
                    }
                    //Calling if search query is empty loading all positions nearby me
                    else if (query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.SEARCH_GOV)) {
                        getJobs(AppConfig.BASE_URL + "?lat_lon=" + Lat + "," + Lng);
                    }

                } else {
                    binding.EmptyRecyclerView.setVisibility(View.VISIBLE);
                    binding.noEventFound.setText("Location not found, pull down to refresh.");
                    if (((HomeActivity) context).locationProvider != null) {
                        ((HomeActivity) context).locationProvider.checkPermissionsAndStartScreenSetup();
                    }
                }

                break;
            case Constants.DATE:
                //calling github url with query
                if (!query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.GITHUB)) {
                    getJobs(AppConfig.BASE_URL + "?description=" + query);
                }
                //calling Search.gov url with query
                else if (!query.equalsIgnoreCase("") && local_api_provider.equalsIgnoreCase(Constants.SEARCH_GOV)) {
                    getJobs(AppConfig.BASE_URL + "?query=" + query);
                }
                //Calling all available positions
                else {
                    getJobs(AppConfig.BASE_URL);
                }

                break;
        }
    }

    private void getJobs(String URL) {
        showDialog();
        NetworkCallHandler.get(URL, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    binding.commonRecyclerView.setVisibility(View.GONE);
                    binding.EmptyRecyclerView.setVisibility(View.GONE);
                    //adding one data key in root of API response as array list parsing error occurs without data key in root
                    String responsee = "{\"data\":" + response.body().string() + "}";
                    jobs = Utils.GsonUtils.fromJSON(responsee, JobPagination.class);
                    binding.pullToRefresh.setRefreshing(false);

                    if (jobs != null && !jobs.getJobsList().isEmpty()) {
                        binding.commonRecyclerView.setVisibility(View.VISIBLE);
                        setUpAdapter(jobs.getJobsList());
                    } else {
                        binding.EmptyRecyclerView.setVisibility(View.VISIBLE);
                        binding.noEventFound.setText("Sorry, no jobs found...");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.commonRecyclerView.setVisibility(View.GONE);
                binding.EmptyRecyclerView.setVisibility(View.VISIBLE);
                binding.noEventFound.setText("Some thing went wrong...");
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
                //setting select radio button values.
                if (SharedPrefManager.getInstance().getString(Constants.API_PROVIDER, "").equalsIgnoreCase(Constants.SEARCH_GOV)) {
                    popupSettingBinding.rdoGroupAPIProvider.check(R.id.rdoBtnSearchGov);
                } else {
                    popupSettingBinding.rdoGroupAPIProvider.check(R.id.rdoBtnGithub);
                }

                if (SharedPrefManager.getInstance().getString(Constants.SORT_TYPE, "").equalsIgnoreCase(Constants.LOCATION)) {
                    popupSettingBinding.rdoGroupSort.check(R.id.rdoBtnLocation);
                } else {
                    popupSettingBinding.rdoGroupSort.check(R.id.rdoBtnDate);
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
                    //reset of search data query
                    binding.etSearch.setText("");
                    Utils.hideKeyboard(context);
                    prepareBaseURLAndFetchData("");

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
                prepareBaseURLAndFetchData("");
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.etSearch.getText().toString();
                Utils.hideKeyboard(context);
                prepareBaseURLAndFetchData(query);

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

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent_Location(Location location) {
        mLocation = location;
    }
}
