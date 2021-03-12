package com.example.ideaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import Features.Database;
import Features.Idea_Adapter;

public class MainActivity extends AppCompatActivity  {

    EditText username;
    Button button;
    String usernamee ;
    Switch test;
    public static final String SHARED_PREFS = " sharedPrefs";
    public static final String TEXT = "text ";


    private String text;


    ListView listView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadData();
        if(text.isEmpty()){
            username = (EditText) findViewById(R.id.username) ;
            button = (Button)   findViewById(R.id.button);
            setContentView(R.layout.activity_main);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveData();

                    openActivity();

                }
            });
        }
        else
        {
            openActivity();
        }

     }

  void SaveData ()  {
        usernamee =  username.getText().toString();
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

         editor.putString(TEXT,username.getText().toString());

         text = sharedPreferences.getString(TEXT , " ");
         editor.apply();


         username.setText(text);

        Toast.makeText(this, "Data saved",Toast.LENGTH_SHORT).show();
        
        setContentView(R.layout.main_display);
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

     void LoadData () {
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
         text = sharedPreferences.getString(TEXT, "");
     }

     public void openActivity() {
        Intent intent = new Intent(this , Activitate_test.class);
        startActivity(intent);
     }

}