package com.chores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    CardView cd_Parents,cd_kids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        cd_kids=(CardView) findViewById(R.id.cd_kids);
        cd_kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(StartActivity.this, ChildLoginActivity.class);
                startActivity(in);
            }
        });


        cd_Parents=(CardView) findViewById(R.id.cd_Parents);
        cd_Parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(StartActivity.this, ParentLoginActivity.class);
                startActivity(in);
            }
        });
    }
}