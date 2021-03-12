package com.example.ideaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username;
    Button button;
    String usernamee ;

   /* public static final String SHARED_PREFS = " sharedPrefs";
    public static final String TEXT = "text ";

    */

   // private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

      /*  username = (EditText) findViewById(R.id.username) ;
        button = (Button)   findViewById(R.id.button);




     button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });

       */



     }

    /* void SaveData ()  {
        usernamee =  username.getText().toString();
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

         editor.putString(TEXT,username.getText().toString());

         text = sharedPreferences.getString(TEXT , " ");
         editor.apply();


         username.setText(text);

        Toast.makeText(this, "Data saved",Toast.LENGTH_SHORT).show();

     }

     */
}