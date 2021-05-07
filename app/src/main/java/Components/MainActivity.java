package Components;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ideaapp.R;

import java.util.List;

import Features.Database;
import Models.UserModel;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private Button button;
    private String usernamee;
    private TextView errorT;
    private Database db;

    private static String text;

    public static final String SHARED_PREFS = " sharedPrefs";
    public static final String TEXT = "text ";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadData();

        db = new Database(this);

        if (text.isEmpty()) {
            setContentView(R.layout.activity_main);

            setViews();

            button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
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
        } else
            openActivity();
    }

    void setViews() {
        username = (EditText) findViewById(R.id.username);
        button = (Button) findViewById(R.id.button);
        errorT = (TextView) findViewById(R.id.error);
    }

    void SaveData() {
        usernamee = username.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, username.getText().toString());

        text = sharedPreferences.getString(TEXT, " ");
        editor.apply();

        username.setText(text);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        LoadData();
    }

    void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    public void openActivity() {
        Intent intent = new Intent(this, PageLoader.class);
        startActivity(intent);
        finish();
    }

    public static String returnUser() {
        return text;
    }
}