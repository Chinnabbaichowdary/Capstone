package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chores.Adapters.ParentTaskAdapter;
import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.Task;
import com.chores.Models.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackTasksActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ListView lv_child;

    List<Task> a1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_tasks);
        getSupportActionBar().setTitle("Tasks Info");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_child = (ListView) findViewById(R.id.lv_child);

        a1 = new ArrayList<>();
        serverData();

    }

    public void serverData() {

        progressDialog= new ProgressDialog(TrackTasksActivity.this);
        progressDialog.setTitle("Please wait,Data is Loading...");
        progressDialog.show();

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("uname", "def-val");
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Task>> call = service.getParentTasks(session);

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(TrackTasksActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {

                    lv_child.setAdapter(new ParentTaskAdapter(response.body(), TrackTasksActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TrackTasksActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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