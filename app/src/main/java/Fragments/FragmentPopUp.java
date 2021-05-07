package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Features.Database;

public class FragmentPopUp extends Fragment {

    private FloatingActionButton addButton;
    private boolean isLiked;
    private TextView textview1, textview2;
    private ViewGroup root;

    static String text1 = "";
    static String text2 = "";

    public FragmentPopUp() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_pop_up, null);

        setViews();
        setDataToDisplay();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = Database.isIdeaLiked(text1);
                if (!isLiked) {
                    Database.giveLike(text1);
                } else {
                    Database.removeLike(text1);
                }
            }
        });

        return root;
    }

    void setViews(){
        textview1 = (TextView) root.findViewById(R.id.name1);
        textview2 = (TextView) root.findViewById(R.id.description1);

        addButton = root.findViewById(R.id.addButton);
    }

    private void setDataToDisplay(){
        if (text1.isEmpty()) {
            textview1.setText("");
            textview2.setText("There is no idea yet.");
            addButton.setVisibility(View.INVISIBLE);
        } else {
            textview1.setText(text1);
            textview2.setText(text2);
            addButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        
        FragmentMainDisplay.closeLayout();
    }

    @Override
    public void onResume() {
        super.onResume();

        setDataToDisplay();
    }
}