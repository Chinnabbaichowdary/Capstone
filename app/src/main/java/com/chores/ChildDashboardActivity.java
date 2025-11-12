package com.chores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChildDashboardActivity extends AppCompatActivity {

    CardView cd_my_account,cd_my_tasks,cd_notifications,cd_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);

        cd_my_account=(CardView) findViewById(R.id.cd_my_account);
        cd_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ChildDashboardActivity.this, ChildProfileActivity.class);
                startActivity(in);
            }
        });

        cd_my_tasks=(CardView) findViewById(R.id.cd_my_tasks);
        cd_my_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in= new Intent(ChildDashboardActivity.this, ChildMyTasksActivity.class);
                startActivity(in);


            }
        });

        cd_notifications=(CardView) findViewById(R.id.cd_notifications);
        cd_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in= new Intent(ChildDashboardActivity.this, ChildNotificationsActivity.class);
                startActivity(in);


            }
        });

        cd_logout=(CardView) findViewById(R.id.cd_logout);
        cd_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ChildDashboardActivity.this, StartActivity.class);
                startActivity(in);
            }
        });
    }
}