package com.jobfinder.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobfinder.databinding.SingleItemJobBind;
import com.jobfinder.model.Job;
import com.jobfinder.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.jobfinder.R;

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
            try {
                //setting data for each job item in list
                Picasso.with(activity).load(job.getCompanyImageUrl()).placeholder(R.mipmap.ic_launcher)
                        .resize(200, 200).centerCrop().into(bind.imgCompany);

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
                System.out.println("formatedDate : " + formatedDate);

                bind.txtPostDate.setText(""+formatedDate);

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

    public interface JobsAdapterListener {
        void JobsAdapterListen(int option, Job job);
    }
}
