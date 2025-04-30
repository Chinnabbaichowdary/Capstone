package com.chores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParentDashboardActivity extends AppCompatActivity {

    CardView cd_my_account,cd_child,cd_tasks,cd_track,cd_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        cd_my_account=(CardView) findViewById(R.id.cd_my_account);
        cd_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ParentDashboardActivity.this, ParentProfileActivity.class);
                startActivity(in);
            }
        });
        cd_child=(CardView) findViewById(R.id.cd_child);
        cd_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ParentDashboardActivity.this, ViewChildListActivity.class);
                startActivity(in);
            }
        });
        cd_tasks=(CardView) findViewById(R.id.cd_tasks);
        cd_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ParentDashboardActivity.this, ViewTasksListActivity.class);
                startActivity(in);
            }
        });
        cd_track=(CardView) findViewById(R.id.cd_track);
        cd_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ParentDashboardActivity.this, TrackTasksActivity.class);
                startActivity(in);
            }
        });
        cd_logout=(CardView) findViewById(R.id.cd_logout);
        cd_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(ParentDashboardActivity.this, StartActivity.class);
                startActivity(in);
            }
        });

    }
}