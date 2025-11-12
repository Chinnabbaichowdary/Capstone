package com.chores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewCompleteTaskActivity extends AppCompatActivity {

    String imgUrl="http://myprojectsworks.com/Chores/";
    ImageView imgPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complete_task);

        imgPic =(ImageView) findViewById(R.id.imgPic);
        Glide.with(this).load(imgUrl+getIntent().getStringExtra("pic")).into(imgPic);

    }
}