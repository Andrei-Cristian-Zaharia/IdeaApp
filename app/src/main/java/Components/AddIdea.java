package Components;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import Features.Database;

import static Components.MainActivity.returnUser;

public class AddIdea extends AppCompatActivity {

    EditText nume, descriere;
    String nume1, descriere1;
    AutoCompleteTextView autoCompleteTextView;
    List<String> ideaTags = new ArrayList<String>();
    ChipGroup chipGroup;
    String[] tags;
    Button button;
    private int currentTagsNr = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_idea);

        chipGroup = (ChipGroup) findViewById(R.id.chip_group);
        nume = (EditText) findViewById(R.id.nameViewscris);
        descriere = (EditText) findViewById(R.id.description1);
        button = (Button) findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);

        tags = getResources().getStringArray(R.array.tags);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, tags);
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
                if (nume1.length() > 3 && descriere1.length() > 10) {
                    Database.InsertIdea(descriere1, nume1, returnUser(), ideaTags);
                    ideaTags.clear();
                    nume.setText("");
                    currentTagsNr = 0;
                    chipGroup.removeAllViews();
                    descriere.setText("");
                    finish();
                }
            }
        });
    }

    void createNewChip(String text) {
        if (currentTagsNr == 3) return;

        for (String textTag : ideaTags)
            if (textTag.contentEquals(text)) return;

        currentTagsNr++;
        Chip mChip = (Chip) LayoutInflater.from(this).inflate(R.layout.layout_chip_entry, this.chipGroup, false);
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

        Log.v("Tag: ", text + " created !");
    }

    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        currentTagsNr--;
        parent.removeView(chip);
    }
}
