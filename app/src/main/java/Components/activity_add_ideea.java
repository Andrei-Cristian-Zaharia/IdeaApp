package Components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ideaapp.R;

import Features.Database;

import static Components.MainActivity.returnUser;

public class activity_add_ideea  extends AppCompatActivity {

    EditText nume , descriere;
    String nume1, descriere1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__idea);

        nume = (EditText) findViewById(R.id.nameViewscris);
        descriere = (EditText) findViewById(R.id.description1);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nume1= nume.getText().toString();
                descriere1=descriere.getText().toString();
                if(nume1.length()>3 && descriere1.length()>10)
                    Database.InsertIdea(descriere1,nume1,returnUser());
                openActivityback();
            }
        });
    }

    void openActivityback(){
        Intent intent = new Intent(this , Main_display_activity.class);
        startActivity(intent);
    }
}
