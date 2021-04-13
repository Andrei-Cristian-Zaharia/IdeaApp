package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Features.Database;

import static Components.MainActivity.returnUser;

public class FragmentAddIdea extends Fragment {
    EditText nume, descriere;
    String nume1, descriere1;
    AutoCompleteTextView autoCompleteTextView;
    List<String> ideaTags = new ArrayList<String>();
    ChipGroup chipGroup;
    String[] tags;
    Button button;
    private int currentTagsNr = 0;

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

        chipGroup = (ChipGroup) root.findViewById(R.id.chip_group);
        nume = (EditText) root.findViewById(R.id.nameViewscris);
        descriere = (EditText) root.findViewById(R.id.description1);
        button = (Button) root.findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView) root.findViewById(R.id.autoComplete);

        tags = getResources().getStringArray(R.array.tags);
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, tags);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createNewChip(tags[position]);
                autoCompleteTextView.dismissDropDown();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nume1 = nume.getText().toString();
                descriere1 = descriere.getText().toString();
                if (nume1.length() > 3 && descriere1.length() > 10)
                {
                    Database.InsertIdea(descriere1, nume1, returnUser(), ideaTags);
                    ideaTags.clear();
                    nume.setText("");
                    currentTagsNr = 0;
                    chipGroup.removeAllViews();
                    descriere.setText("");
                }
            }
        });

        return root;
    }

    void createNewChip(String text){
        if (currentTagsNr == 3) return;
        currentTagsNr++;

        Chip mChip = (Chip) LayoutInflater.from(this.getContext()).inflate(R.layout.layout_chip_entry, this.chipGroup, false);
        mChip.setCheckable(false);
        mChip.setText(text);
        ideaTags.add(text);
        this.chipGroup.addView(mChip);

        mChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChipCloseIconClicked((Chip) v);
            }
        });

        Log.v("Tag: " , text + " created !");
    }

    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        currentTagsNr--;
        parent.removeView(chip);
    }
}