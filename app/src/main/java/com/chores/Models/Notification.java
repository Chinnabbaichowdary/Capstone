package com.chores.Models;

import com.google.gson.annotations.SerializedName;

public class Notification {

    //SELECT `id`, `child`, `taskname`, `dat`, `msg` FROM `notification` WHERE 1

    @SerializedName("id")
    private String id;

    @SerializedName("child")
    private String child;

    @SerializedName("taskname")
    private String taskname;

    @SerializedName("dat")
    private String dat;

    @SerializedName("msg")
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
