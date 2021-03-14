package com.example.ideaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Components.Main_display_activity;


public class pop_up_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_layout);
        Intent intent = getIntent();


        TextView textview1 = (TextView) findViewById(R.id.name1);
        TextView textview2 = (TextView) findViewById(R.id.description1);

        String text1 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT1);
        String text2 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT2);

        textview1.setText(text1);
        textview2.setText(text2);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .7),(int)(height * .8));

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.CENTER;
        params.x = 5 ;
        params.y = -40;

        getWindow().setAttributes(params);
    }
}