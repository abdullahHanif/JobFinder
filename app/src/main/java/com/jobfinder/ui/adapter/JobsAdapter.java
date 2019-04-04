package com.jobfinder.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jobfinder.databinding.SingleItemJobBind;
import com.jobfinder.model.Job;
import com.jobfinder.utils.Constants;
import com.jobfinder.utils.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.jobfinder.R;

import org.json.JSONArray;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private final Activity activity;
    ArrayList<Job> jobsList;
    JobsAdapterListener mListener;


    public JobsAdapter(Activity activity, ArrayList<Job> items, JobsAdapterListener listener) {
        this.activity = activity;
        jobsList = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleItemJobBind binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_item_job, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(jobsList.get(position));
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleItemJobBind bind;

        ViewHolder(@NonNull SingleItemJobBind binding) {
            super(binding.getRoot());
            this.bind = binding;
        }

        public void setData(final Job job) {


            String api_provider = SharedPrefManager.getInstance().getString(Constants.API_PROVIDER, "");

            String imgPath = (job.getCompanyImageUrl()== null || job.getCompanyImageUrl().equals("")) ? "https://via.placeholder.com/200x200":job.getCompanyImageUrl();
            //setting data for each job item in list
            Picasso.with(activity).load(imgPath).placeholder(R.mipmap.ic_launcher)
                    .resize(200, 200).centerCrop().into(bind.imgCompany);

            if (api_provider.equalsIgnoreCase(Constants.GITHUB)) {
                try {
                    //fetching data form model if server is github
                    bind.txtJobTitle.setText(job.getTitle());
                    bind.txtCompanyName.setText(job.getCompanyName());
                    bind.txtLocation.setText(job.getLocation());

                /*Sat Nov 10 02:02:55 UTC 2018
                  E MMM dd HH:mm:ss Z yyyy
                */
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Calendar cal = Calendar.getInstance();

                    cal.setTime(sdf.parse(job.getPostDate()));
                    String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                    bind.txtPostDate.setText("" + formatedDate);

                    bind.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.JobsAdapterListen(Constants.ITEM_OPEN, job);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (api_provider.equalsIgnoreCase(Constants.SEARCH_GOV)) {
                try {
                    //fetching data form model if server is github
                    bind.txtJobTitle.setText(job.getPosition_title());
                    bind.txtCompanyName.setText(job.getOrganization_name());
                    //setting location from json array
                    bind.txtLocation.setText((String) new JSONArray(job.getLocations()).get(0));

                /*2018-10-31
                 yyyy-MM-dd
                */
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();

                    cal.setTime(sdf.parse(job.getStart_date()));
                    String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                    bind.txtPostDate.setText("" + formatedDate);

                    bind.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.JobsAdapterListen(Constants.ITEM_OPEN, job);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface JobsAdapterListener {
        void JobsAdapterListen(int option, Job job);
    }
}
