package com.example.ideaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class pop_up_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_layout);


        Intent intent = getIntent();
        String text1 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT1);
        String text2 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT2);

        TextView textview1 = (TextView) findViewById(R.id.name1);
        TextView textview2 = (TextView) findViewById(R.id.description1);

        textview1.setText(text1);
        textview2.setText(text2);
    }
}