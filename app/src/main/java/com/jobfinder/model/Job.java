package com.jobfinder.model;

import com.google.gson.annotations.SerializedName;

public class Job {
    //Pojo for Job of Github API
    @SerializedName("company_logo")
    private String CompanyImageUrl = "";

    @SerializedName("title")
    private String title = "";

    @SerializedName("company")
    private String companyName = "";

    @SerializedName("location")
    private String location = "";

    @SerializedName("created_at")
    private String postDate = "";

    @SerializedName("url")
    private String url = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getCompanyImageUrl() {
        return CompanyImageUrl;
    }

    public void setCompanyImageUrl(String companyImageUrl) {
        CompanyImageUrl = companyImageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
