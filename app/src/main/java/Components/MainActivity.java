package Components;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import com.example.ideaapp.R;

import java.util.List;

import Features.Database;
import Models.UserModel;

public class MainActivity extends AppCompatActivity  {

    EditText username;
    Button button;
    String usernamee ;
    TextView errorT ;

    public static final String SHARED_PREFS = " sharedPrefs";
    public static final String TEXT = "text ";

    private String text;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database db = new Database(this);

        LoadData();
        if(text.isEmpty()){
            setContentView(R.layout.activity_main);

            username = (EditText) findViewById(R.id.username) ;
            button = (Button)   findViewById(R.id.button);
            errorT = (TextView) findViewById(R.id.error);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (username.getText().toString().isEmpty())
                        errorT.setText("Username can't be empty");
                    else {
                        boolean canSave = true;
                        List<UserModel> users = Database.getAllUsers();

                        for (UserModel user : users)
                            if (user.getUsername().equals(username.getText().toString()))
                                canSave = false;

                        if (canSave) {
                             Database.InsertUser(username.getText().toString());
                             SaveData();

                             openActivity();
                        } else errorT.setText("User already exist !");
                    }
                }
            });
        }
        else
        {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(TEXT, null);

            text = sharedPreferences.getString(TEXT , " ");
            editor.apply();

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

     void LoadData () {
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
         text = sharedPreferences.getString(TEXT, "");
     }

     public void openActivity() {
        Intent intent = new Intent(this , Main_display_activity.class);
        startActivity(intent);
     }

}