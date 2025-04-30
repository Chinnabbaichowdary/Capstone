package com.chores;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.ResponseData;
import com.chores.Models.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity {

    EditText et_taskname,et_points,et_description,et_pemail,et_phone,et_notify_radius;
    Button bt_get_location,btnAddTask;
    TextView tv_get_location;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().setTitle("Add Child Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_taskname = findViewById(R.id.et_taskname);
        et_points = findViewById(R.id.et_points);
        et_description = findViewById(R.id.et_description);

        et_pemail = findViewById(R.id.et_pemail);
        et_phone = findViewById(R.id.et_phone);

        et_notify_radius = findViewById(R.id.et_notify_radius);
        tv_get_location = findViewById(R.id.tv_get_location);
        tv_get_location.setText("No Location Selected.");

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("uname", "def-val");

        et_phone.setText(session);

        bt_get_location = findViewById(R.id.bt_get_location);
        bt_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddTaskActivity.this, AddLocationMapsActivity.class),888);
            }
        });
        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_taskname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name Should not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_pemail.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email Should not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_points.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Points Should not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_notify_radius.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Notify Radius Should not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tv_get_location.getText().toString().contains("Location")) {
                    Toast.makeText(getApplicationContext(), "Please select location", Toast.LENGTH_SHORT).show();
                    return;
                }
                addRaduisInfo();
            }
        });
    }
    ProgressDialog pd;
    public  void addRaduisInfo()
    {
        pd= new ProgressDialog(AddTaskActivity.this);
        pd.setTitle("Please wait,Data is being submit...");
        pd.show();
        ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);


        Call<ResponseData> call = apiService.addTasks(et_pemail.getText().toString(),et_phone.getText().toString(),et_taskname.getText().toString(),et_points.getText().toString(),et_description.getText().toString(),lat,lng,et_notify_radius.getText().toString());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();
                if (response.body().status.equals("true")) {
                    Toast.makeText(AddTaskActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    Log.i("msg", "" + response.body().message);
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(AddTaskActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }
    String lat="0.0",lng="0.0";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            lat = data.getStringExtra("lat");
            lng = data.getStringExtra("lng");
            tv_get_location.setText("Lat : "+lat +"  Lng : "+lng);
        }
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