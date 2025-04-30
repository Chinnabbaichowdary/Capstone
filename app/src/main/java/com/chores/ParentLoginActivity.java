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

public class ParentLoginActivity extends AppCompatActivity {

    EditText et_email,et_pass;
    TextView tv_reg_here,tv_forgetpwd;
    Button btnLogin;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        getSupportActionBar().setTitle("Parent Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et_email=(EditText)findViewById(R.id.et_email);
        et_pass=(EditText)findViewById(R.id.et_pass);
        btnLogin=(Button) findViewById(R.id.btnLogin);

        tv_reg_here=(TextView)findViewById(R.id.tv_reg_here);
        tv_reg_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParentLoginActivity.this, ParentRegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_email.getText().toString().isEmpty()){
                    Toast.makeText(ParentLoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_pass.getText().toString().isEmpty()){
                    Toast.makeText(ParentLoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                parentlogin();
            }
        });
    }

    public  void parentlogin() {
        pd= new ProgressDialog(ParentLoginActivity.this);
        pd.setTitle("Please wait, Login Processing...");
        pd.show();


        ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseData> call = apiService.parentlogin(et_email.getText().toString(),et_pass.getText().toString());

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();
                if (response.body().status.equals("true")) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor et=sharedPreferences.edit();
                    et.putString("uname",et_email.getText().toString());
                    et.commit();
                    // startActivity(new Intent(CusLoginActivity.this, CusDashboardActivity.class));
                    startActivity(new Intent(ParentLoginActivity.this, ParentDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(ParentLoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ParentLoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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