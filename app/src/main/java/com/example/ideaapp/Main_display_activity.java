package com.example.ideaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Features.Database;
import Features.Idea_Adapter;

public class Main_display_activity  extends AppCompatActivity {
    public static final String EXTRA_TEXT1= "com.example.ideaapp.EXTRA_TEXT1 ";
    public static final String EXTRA_TEXT2= "com.example.ideaapp.EXTRA_TEXT2 ";


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

                openLayoutActivity(descriptions[position]);
                Toast.makeText(Main_display_activity.this, names[position], Toast.LENGTH_SHORT).show();
            }
        });
    }
    void openLayoutActivity(String description){
        TextView Textview1 = (TextView) findViewById(R.id.name) ;
        String text1 = Textview1.getText().toString();



        Intent intent = new Intent(this , pop_up_layout.class);
        intent.putExtra(EXTRA_TEXT1,text1);
        intent.putExtra(EXTRA_TEXT2,description);
        startActivity(intent);
    }


}
