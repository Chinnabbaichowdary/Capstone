package com.chores.Models;

import com.google.gson.annotations.SerializedName;

public class Task {

    //SELECT `id`, `parent_email`, `parent_phone`, `childemail`, `childphone`,
    // `taskname`, `taskpoints`, `taskdescription`, `dat`, `lat`, `lng`, `notify_radius`, `status` FROM `task` WHERE 1

    @SerializedName("id")
    private String id;

    @SerializedName("image")
    private String image;

    @SerializedName("parent_email")
    private String parent_email;

    @SerializedName("parent_phone")
    private String parent_phone;

    @SerializedName("childemail")
    private String childemail;

    @SerializedName("childphone")
    private String childphone;

    @SerializedName("taskname")
    private String taskname;

    @SerializedName("taskpoints")
    private String taskpoints;

    @SerializedName("taskdescription")
    private String taskdescription;

    @SerializedName("dat")
    private String dat;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("notify_radius")
    private String notify_radius;

    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_email() {
        return parent_email;
    }

    public void setParent_email(String parent_email) {
        this.parent_email = parent_email;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public String getChildemail() {
        return childemail;
    }

    public void setChildemail(String childemail) {
        this.childemail = childemail;
    }

    public String getChildphone() {
        return childphone;
    }

    public void setChildphone(String childphone) {
        this.childphone = childphone;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskpoints() {
        return taskpoints;
    }

    public void setTaskpoints(String taskpoints) {
        this.taskpoints = taskpoints;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNotify_radius() {
        return notify_radius;
    }

    public void setNotify_radius(String notify_radius) {
        this.notify_radius = notify_radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
