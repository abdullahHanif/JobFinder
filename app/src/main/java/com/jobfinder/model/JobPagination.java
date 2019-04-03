package com.jobfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobPagination {
    @SerializedName("data")
    private ArrayList<Job> job = new ArrayList<>();

    public ArrayList<Job> getJobsList() {
        return job;
    }

    public void setJobsList(ArrayList<Job> job) {
        this.job = job;
    }
}
