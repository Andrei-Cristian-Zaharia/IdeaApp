package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ideaapp.R;

import Features.Database;

import static Components.MainActivity.returnUser;

public class FragmentAddIdea extends Fragment {
    EditText nume, descriere;
    String nume1, descriere1;
    Button button;

    public FragmentAddIdea() {
    }

    public static FragmentAddIdea newInstance(String param1, String param2) {
        FragmentAddIdea fragment = new FragmentAddIdea();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_idea, null);

        nume = (EditText) root.findViewById(R.id.nameViewscris);
        descriere = (EditText) root.findViewById(R.id.description1);
        button = (Button) root.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nume1 = nume.getText().toString();
                descriere1 = descriere.getText().toString();
                if (nume1.length() > 3 && descriere1.length() > 10)
                    Database.InsertIdea(descriere1, nume1, returnUser());
            }
        });

        return root;
    }
}