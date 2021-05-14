package Components;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Features.Database;
import io.realm.RealmList;

import static Components.MainActivity.returnUser;

enum ToolType { TEXT, IMAGE}

public class AddIdea extends AppCompatActivity {

    private EditText nume;
    private String nume1;
    private String[] tags;
    private final int RESULT_LOAD_IMG = 123;

    private final RealmList<String> descriere1 = new RealmList<>();

    private final List<String> ideaTags = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;

    private RelativeLayout relativeLayout;

    private Button button;
    private PopupWindow popUp;

    private int currentTagsNr = 0;

    private final ArrayList<View> viewList = new ArrayList<>();
    private final ArrayList<FloatingActionButton> floatingActionButtons = new ArrayList<>();
    private int ID = 1000;
    private int imageID;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                        else if (view1 instanceof ImageView){
                            Bitmap bitmap = ((BitmapDrawable) ((ImageView) view1).getDrawable()).getBitmap();
                            descriere1.add("I/G " + BitMapToString(bitmap));
                        }

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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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


    @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    void addImageView(int id, Bitmap bitmap){

        ImageView image = new ImageView(this);

        image.setId(id);
        setImageViewParamas(image, id++);

        Bitmap resize = Bitmap.createScaledBitmap(bitmap, 250, 250, true);

        image.setImageBitmap(resize);

        viewList.add(image);

        relativeLayout.addView(image);

        addToolbarButton(id);
        ID++;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    void addToolbarButton(int id) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        floatingActionButton.setId(id);

        setFloatActionButtonParams(floatingActionButton, id);
        floatingActionButton.setForeground(getResources().getDrawable(R.drawable.add_icon));

        floatingActionButtons.add(floatingActionButton);

        ID++;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] location = new int[2];
                floatingActionButton.getLocationOnScreen(location);
                @SuppressLint("InflateParams") final View mView = LayoutInflater.from(getBaseContext()).inflate(R.layout.toolbar, null, false);
                popUp = new PopupWindow(mView, 600, 400, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                popUp.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, location[0], location[1]);

                Button buttonText = (Button) popUp.getContentView().findViewById(R.id.addTextView);
                Button buttonImage = (Button) popUp.getContentView().findViewById(R.id.addImageView);

                buttonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int newId = floatingActionButton.getId();
                        if (newId + 1 < ID)
                            refreshID(newId + 1 , ToolType.TEXT);
                        else
                            addTextView(newId + 1);

                        popUp.dismiss();
                    }
                });

                buttonImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int newId = floatingActionButton.getId();
                        imageID = newId + 1;

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                        popUp.dismiss();
                    }
                });
            }
        });

        if (floatingActionButtons.size() == 7) disableButtons();

        relativeLayout.addView(floatingActionButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                if (imageID < ID)
                    refreshID(imageID, ToolType.IMAGE);
                else
                    addImageView(imageID, selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    void setTextViewParams(TextView textView, int id) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.addRule(RelativeLayout.BELOW, id - 1);
        textView.setLayoutParams(params);
    }

    void setImageViewParamas(ImageView imageView, int id){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1000, 1000);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, id - 1);
        imageView.setLayoutParams(params);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    void refreshID(int id, ToolType toolType) {

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

        if (toolType == ToolType.TEXT)
            addTextView(id);
        else if (toolType == ToolType.IMAGE){
            imageID = id;

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }

        for (int i = 1; i < viewList.size(); i++)
            if (viewList.get(i) instanceof EditText)
                setTextViewParams((TextView) viewList.get(i), viewList.get(i).getId());
            else  if (viewList.get(i) instanceof ImageView)
                setImageViewParamas((ImageView) viewList.get(i), viewList.get(i).getId());

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
