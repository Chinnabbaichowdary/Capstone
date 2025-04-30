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
import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.Child;
import com.chores.Models.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewChildListActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ListView lv_child;

    List<Child> a1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child_list);
        getSupportActionBar().setTitle("Children Info");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_child = (ListView) findViewById(R.id.lv_child);

        a1 = new ArrayList<>();
        serverData();
    }

    public void serverData() {

        progressDialog= new ProgressDialog(ViewChildListActivity.this);
        progressDialog.setTitle("Please wait,Data is Loading...");
        progressDialog.show();

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("uname", "def-val");
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Child>> call = service.getChildInfo(session);

        call.enqueue(new Callback<List<Child>>() {
            @Override
            public void onResponse(Call<List<Child>> call, Response<List<Child>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(ViewChildListActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {

                    lv_child.setAdapter(new ChildAdapter(response.body(), ViewChildListActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Child>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ViewChildListActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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