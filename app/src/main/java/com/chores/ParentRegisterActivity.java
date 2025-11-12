package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentRegisterActivity extends AppCompatActivity {
    EditText et_name,et_email,et_phone,et_pass,et_address,et_location;
    Button btnParentRegistration;
    ProgressDialog pd;
    TextView tvsignin1,tvsignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        getSupportActionBar().setTitle("Parent Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvsignin1=(TextView)findViewById(R.id.tvsignin1);
        tvsignin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParentRegisterActivity.this, ParentLoginActivity.class));

            }
        });

        tvsignin=(TextView)findViewById(R.id.tvsignin);
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParentRegisterActivity.this, ParentLoginActivity.class));

            }
        });
        et_name=(EditText) findViewById(R.id.et_name);
        et_email=(EditText) findViewById(R.id.et_email);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_pass=(EditText) findViewById(R.id.et_pass);

        et_address=(EditText) findViewById(R.id.et_address);
        et_location=(EditText) findViewById(R.id.et_location);

        btnParentRegistration=(Button)findViewById(R.id.btnParentRegistration);
        btnParentRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_name.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_email.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_phone.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_pass.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_location.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_address.getText().toString().isEmpty()){
                    Toast.makeText(ParentRegisterActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                cusRegistration();
            }
        });
    }

    public  void cusRegistration() {
        pd= new ProgressDialog(ParentRegisterActivity.this);
        pd.setTitle("Please wait...");
        pd.show();

        ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseData> call = apiService.parentregistration(et_name.getText().toString(),et_email.getText().toString(),et_phone.getText().toString(),et_pass.getText().toString(),et_address.getText().toString(),et_location.getText().toString());


        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();
                if (response.body().status.equals("true")) {
                    startActivity(new Intent(ParentRegisterActivity.this, ParentLoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ParentRegisterActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ParentRegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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