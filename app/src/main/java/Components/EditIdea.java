package Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import Features.Database;
import Models.Idea;

import static Components.MainActivity.returnUser;

public class EditIdea extends AppCompatActivity {
    private EditText nume, descriere;
    private String nume1, descriere1;
    private String[] tags;

    private final List<String> ideaTags = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;

    private Button button;

    private int currentTagsNr = 0;

    static Idea idea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_idea);

        setViews();

        loadData();

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
                    Database.editIdea(idea);
                    ideaTags.clear();
                    nume.setText("");
                    currentTagsNr = 0;
                    chipGroup.removeAllViews();
                    descriere.setText("");
                    PageLoader.showSuccesDialog("Idea was successfully modified !");
                    finish();
                }
            }
        });
    }

    public static void setIdea(Idea aux) {
        idea = aux;
    }

    void loadData() {
        nume.setText(idea.get_nume());
        descriere.setText(idea.get_description());

        for (String tag : idea.getTags()) {
            createNewChip(tag);
        }
    }

    void setViews() {
        chipGroup = (ChipGroup) findViewById(R.id.chip_groupEdit);
        nume = (EditText) findViewById(R.id.nameEdit);
        descriere = (EditText) findViewById(R.id.descriptionEdit);
        button = (Button) findViewById(R.id.buttonEdit);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteEdit);
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
    }

    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        currentTagsNr--;
        parent.removeView(chip);
    }
}
