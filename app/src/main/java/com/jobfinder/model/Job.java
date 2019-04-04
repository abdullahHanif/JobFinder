package com.jobfinder.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Job {
    //Pojo for Job of Github API
    @SerializedName("company_logo")
    private String CompanyImageUrl = "";

    @SerializedName("title")
    private String title = "";

    @SerializedName("position_title")
    private String position_title = "";

    @SerializedName("company")
    private String companyName = "";

    @SerializedName("organization_name")
    private String organization_name = "";

    @SerializedName("location")
    private String location = "";

    @SerializedName("locations")
    private List<String> locations = new ArrayList<>();

    @SerializedName("created_at")
    private String postDate = "";

    public String getPosition_title() {
        return position_title;
    }

    public void setPosition_title(String position_title) {
        this.position_title = position_title;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @SerializedName("start_date")
    private String start_date = "";

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
