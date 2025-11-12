package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.Child;
import com.chores.Models.Parent;
import com.chores.Models.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildProfileActivity extends AppCompatActivity {

    EditText et_name,et_age,et_email,et_phone,et_gender,et_pass,et_address,et_location;
    Button btnchildUpdate;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getprofile();
        et_name=(EditText) findViewById(R.id.et_name);
        et_email=(EditText) findViewById(R.id.et_email);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_pass=(EditText) findViewById(R.id.et_pass);
        et_address=(EditText) findViewById(R.id.et_address);
        et_location=(EditText) findViewById(R.id.et_location);
        et_age=(EditText) findViewById(R.id.et_age);
        et_gender=(EditText) findViewById(R.id.et_gender);
        btnchildUpdate=(Button) findViewById(R.id.btnchildUpdate);
    }

    public  void getprofile() {

        progressDialog= new ProgressDialog(ChildProfileActivity.this);
        progressDialog.setTitle("Please wait,Data is Loading...");
        progressDialog.show();

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("uname", "def-val");

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Child>> call = service.getChildProfile(session);

        Toast.makeText(getApplicationContext(), session, Toast.LENGTH_SHORT).show();


        call.enqueue(new Callback<List<Child>>() {
            @Override
            public void onResponse(Call<List<Child>> call, Response<List<Child>> response) {
                //   progressDialog.dismiss();
                //    myProfilePojo = response.body();

                //    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();


                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(ChildProfileActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    if(response.body()!=null && response.body().size()>0){
                        Child profile = response.body().get(0);
                        et_name.setText(profile.getName());
                        et_email.setText(profile.getEmail());
                        et_phone.setText(profile.getPhone());
                        et_pass.setText(profile.getPass());
                        et_age.setText(profile.getAge());
                        et_gender.setText(profile.getGender());
                        et_address.setText(profile.getAddress());
                        et_location.setText(profile.getLocation());
                    }
                }
//
//                MyProfilePojo myprofile = myProfilePojo.get(0);
//                etgetnam.setText(myprofile.getName());
//                etgetemail.setText(myprofile.getEamil());
//                etgetpass.setText(myprofile.getPass());
//                et_EmailID.setText(myprofile.getEmail());
//                et_password.setText(myprofile.getPass());
            }

            @Override
            public void onFailure(Call<List<Child>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

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