package Components;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Features.Database;


public class pop_up_layout extends AppCompatActivity {

    FloatingActionButton addButton;
    private boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_layout);
        Intent intent = getIntent();

        TextView textview1 = (TextView) findViewById(R.id.name1);
        TextView textview2 = (TextView) findViewById(R.id.description1);
        addButton = findViewById(R.id.addButton);

        String text1 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT1);
        String text2 = intent.getStringExtra(Main_display_activity.EXTRA_TEXT2);

        textview1.setText(text1);
        textview2.setText(text2);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = Database.isIdeaLiked(text1);
                if (!isLiked) {
                   Database.giveLike(text1);
                }
                else {
                    Database.removeLike(text1);
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .95),(int)(height * .7));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.CENTER;
        params.x = 0 ;
        params.y = -40;

        getWindow().setAttributes(params);
    }

    public void onDestroy() {
        super.onDestroy();

        Main_display_activity.closeLayout();
    }
}