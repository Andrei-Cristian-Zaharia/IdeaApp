package Components;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Features.Database;
import io.realm.RealmList;

import static Components.MainActivity.returnUser;

public class AddIdea extends AppCompatActivity {

    private EditText nume;
    private String nume1;
    private String[] tags;

    private RealmList<String> descriere1 = new RealmList<>();

    private final List<String> ideaTags = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;

    private RelativeLayout relativeLayout;

    private Button button;

    private int currentTagsNr = 0;

    private ArrayList<View> viewList = new ArrayList<>();
    private ArrayList<FloatingActionButton> floatingActionButtons = new ArrayList<>();
    private int ID = 1000;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_idea);

        setViews();

        tags = getResources().getStringArray(R.array.tags);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, tags);
        autoCompleteTextView.setAdapter(arrayAdapter);

        addTextView(R.id.nameViewscris);

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

                if (nume1.length() > 3) {

                    for (View view1 : viewList)
                        if (view1 instanceof EditText)
                            descriere1.add(((TextView) view1).getText().toString());

                    Database.InsertIdea(descriere1, nume1, returnUser(), ideaTags);
                    ideaTags.clear();
                    nume.setText("");
                    currentTagsNr = 0;
                    chipGroup.removeAllViews();
                    finish();
                }
            }
        });
    }

    void setViews() {
        chipGroup = (ChipGroup) findViewById(R.id.chip_group);
        nume = (EditText) findViewById(R.id.nameViewscris);
        button = (Button) findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);

        relativeLayout = findViewById(R.id.addIdeaLiniar);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void addTextView(int id) {

        EditText textView = new EditText(this);

        if (id == R.id.nameViewscris) {
            textView.setId(ID);
            setTextViewParams(textView, id + 1);
        } else {
            textView.setId(id);
            setTextViewParams(textView, id++);
        }

        textView.setHint("Edit idea description here ...");
        textView.setTextSize(20);

        viewList.add(textView);

        if (relativeLayout != null)
            relativeLayout.addView(textView);

        if (id == R.id.nameViewscris)
            addToolbarButton(++ID);
        else {
            addToolbarButton(id);
            ID++;
        }
    }

    void addToolbarButton(int id) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        floatingActionButton.setId(id);

        setFloatActionButtonParams(floatingActionButton, id);
        floatingActionButtons.add(floatingActionButton);

        ID++;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                    int newId = floatingActionButton.getId();
                if (newId + 1 < ID)
                    refreshID(newId + 1 );
                else
                    addTextView(newId + 1 );
            }
        });

        if (floatingActionButtons.size() == 7) disableButtons();

        relativeLayout.addView(floatingActionButton);
    }

    void setTextViewParams(TextView textView, int id) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.addRule(RelativeLayout.BELOW, id - 1);
        textView.setLayoutParams(params);
    }

    void setFloatActionButtonParams(FloatingActionButton floatActionButton, int id) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)
                (20f * this.getResources().getDisplayMetrics().density),
                (int) (20f * this.getResources().getDisplayMetrics().density));

        params.leftMargin = (int) (5f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (5f * this.getResources().getDisplayMetrics().density);
        params.addRule(RelativeLayout.BELOW, id - 1);
        floatActionButton.setLayoutParams(params);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void refreshID(int id) {

        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).getId() >= id) {
                int aux = viewList.get(i).getId();
                aux +=2;

                viewList.get(i).setId(aux);
            }
        }

        for (int i = 0; i < floatingActionButtons.size(); i++) {
            if (floatingActionButtons.get(i).getId() > id) {

                int aux = floatingActionButtons.get(i).getId();
                aux +=2;

                floatingActionButtons.get(i).setId(aux);
            }
        }

        addTextView(id);

        for (int i = 1; i < viewList.size(); i++)
            if (viewList.get(i) instanceof EditText)
            setTextViewParams((TextView) viewList.get(i), viewList.get(i).getId());

        for (int i = 1; i < floatingActionButtons.size(); i++)
            setFloatActionButtonParams((FloatingActionButton) floatingActionButtons.get(i), floatingActionButtons.get(i).getId());
    }

    void disableButtons(){
        for (int i = 0; i < floatingActionButtons.size(); i++)
            floatingActionButtons.get(i).setVisibility(View.INVISIBLE);
    }

    void enableButtons(){
        for (int i = 0; i < floatingActionButtons.size(); i++)
            floatingActionButtons.get(i).setVisibility(View.VISIBLE);
    }
}
