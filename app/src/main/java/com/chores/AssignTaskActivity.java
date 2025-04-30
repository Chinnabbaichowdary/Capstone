package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chores.Api.ApiService;
import com.chores.Api.RetroClient;
import com.chores.Models.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignTaskActivity extends AppCompatActivity {

    Button btnAssignTask;
    ProgressDialog progress;
    EditText et_taskid,et_taskname,et_childemail,et_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        et_taskid = (EditText) findViewById(R.id.et_taskid);
        et_taskname = (EditText) findViewById(R.id.et_taskname);
        et_childemail = (EditText) findViewById(R.id.et_childemail);
        et_phone = (EditText) findViewById(R.id.et_phone);

        et_taskid.setText(getIntent().getStringExtra("id"));
        et_taskname.setText(getIntent().getStringExtra("name"));
        btnAssignTask = (Button) findViewById(R.id.btnAssignTask);

        btnAssignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((et_childemail.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Email not empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_phone.getText().toString().isEmpty()) {
                    Toast.makeText(AssignTaskActivity.this, "Enter Phone", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    progress = new ProgressDialog(AssignTaskActivity.this);
                    progress.setMessage("Adding please wait");
                    progress.show();


                    ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
                    Call<ResponseData> call = service.assignTask(et_taskid.getText().toString(), et_taskname.getText().toString(), et_childemail.getText().toString(), et_phone.getText().toString());

                    call.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            progress.dismiss();
                            if (response.body().status.equals("true")) {
                                Toast.makeText(AssignTaskActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AssignTaskActivity.this, ParentDashboardActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(AssignTaskActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(AssignTaskActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
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