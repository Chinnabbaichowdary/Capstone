package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.chores.Adapters.ChildAdapter;
import com.chores.Adapters.ChildNotification;
import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.Child;
import com.chores.Models.Notification;
import com.chores.Models.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildNotificationsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ListView lv_child;

    List<Notification> a1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_notifications);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_child = (ListView) findViewById(R.id.lv_child);

        a1 = new ArrayList<>();
        serverData();
    }

    public void serverData() {

        progressDialog= new ProgressDialog(ChildNotificationsActivity.this);
        progressDialog.setTitle("Please wait,Data is Loading...");
        progressDialog.show();

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("uname", "def-val");
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Notification>> call = service.getChildNotification(session);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(ChildNotificationsActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {

                    lv_child.setAdapter(new ChildNotification(response.body(), ChildNotificationsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChildNotificationsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}