package com.chores.Api;

import com.chores.Models.Child;
import com.chores.Models.Parent;
import com.chores.Models.ResponseData;
import com.chores.Models.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("Chores/childRegistration.php")
    Call<ResponseData> childRegistration(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("pass") String pass,
            @Query("age") String age,
            @Query("gender") String gender,
            @Query("address") String address,
            @Query("location") String location
    );




    @GET("/Chores/childlogin.php?")
    Call<ResponseData> childlogin(
            @Query("email") String email,
            @Query("pass") String pass);

    @GET("/Chores/getChildProfile.php?")
    Call<List<Child>> getChildProfile(
            @Query("email") String email);


    @GET("Chores/childupdateprofile.php")
    Call<ResponseData> childupdateprofile(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("pass") String pass);

    @GET("Chores/parentregistration.php")
    Call<ResponseData> parentregistration(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("pass") String pass,
            @Query("address") String address,
            @Query("location") String location
    );

    @GET("/Chores/parentlogin.php?")
    Call<ResponseData> parentlogin(
            @Query("email") String email,
            @Query("pass") String pass);

    @GET("/Chores/getParentProfile.php?")
    Call<List<Parent>> getParentProfile(
            @Query("email") String email);

    @GET("Chores/parentupdateprofile.php")
    Call<ResponseData> parentupdateprofile(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("pass") String pass);


    @GET("/Chores/getChildInfo.php?")
    Call<List<Child>> getChildInfo(
            @Query("email") String email);


    @GET("/Chores/getParentTasks.php?")
    Call<List<Task>> getParentTasks(
            @Query("phone") String phone);

    @GET("/Chores/getMyTasks.php?")
    Call<List<Task>> getMyTasks(
            @Query("phone") String phone);

    @GET("Chores/addTasks.php")
    Call<ResponseData> addTasks(
            @Query("parent_email") String parent_email,
            @Query("parent_phone") String parent_phone,
            @Query("taskname") String taskname,
            @Query("taskpoints") String taskpoints,
            @Query("taskdescription") String taskdescription,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("notify_radius") String notify_radius
    );

    @GET("Chores/assignTask.php")
    Call<ResponseData> assignTask(
            @Query("id") String id,
            @Query("taskname") String taskname,
            @Query("childemail") String childemail,
            @Query("childphone") String childphone
    );

    @GET("Chores/updateTaskStauts.php")
    Call<ResponseData> updateTaskStauts(
            @Query("id") String id,
            @Query("status") String status
    );




}
