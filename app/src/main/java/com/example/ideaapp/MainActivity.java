package com.example.ideaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import Features.Database;
import Features.Idea_Adapter;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

        Database db = new Database(this, this);
     }

     public void DisplayData(String[] names, String[] descriptions){

         ListView listView = (ListView) findViewById(R.id.listview);


         Idea_Adapter adapter = new Idea_Adapter(this, names, descriptions);
         listView.setAdapter(adapter);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(MainActivity.this, names[position], Toast.LENGTH_SHORT).show();
             }
         });
     }
}