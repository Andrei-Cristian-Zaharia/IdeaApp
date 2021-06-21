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
import android.widget.LinearLayout;
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

enum ToolType { NONE, TEXT, IMAGE}

public class AddIdea extends AppCompatActivity {

    private EditText nume;
    private String nume1;
    private String[] tags;
    private final int RESULT_LOAD_IMG = 123;

    private final RealmList<String> descriere1 = new RealmList<>();

    private final List<String> ideaTags = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;

    private LinearLayout liniarLayout;
    private Bitmap selectedImage;

    private Button button;
    private PopupWindow popUp;

    private int currentTagsNr = 0;

    private final ArrayList<View> viewList = new ArrayList<>();
    private final ArrayList<FloatingActionButton> floatingActionButtons = new ArrayList<>();
    private View imageView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_idea);

        setViews();

        tags = getResources().getStringArray(R.array.tags);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, tags);
        autoCompleteTextView.setAdapter(arrayAdapter);

        addTextView(null);

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
        }); // Aceasta bucata de cod ajuta la introducerea unei idei in baza de date la apasarea butonului OK
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

        liniarLayout = findViewById(R.id.addIdeaLiniar);
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

    void addTextView(View view) {
        EditText textView = new EditText(this);

        setTextViewParams(textView);
        textView.setHint("Edit idea description here ...");
        textView.setTextSize(20);

        viewList.add(textView);

        if (liniarLayout != null)
            if (view == null)
                liniarLayout.addView(textView);
            else
                liniarLayout.addView(textView, liniarLayout.indexOfChild(view) + 1);

        createNewButtons(textView, false);
    }

    void addImageView(View view){
        ImageView image = new ImageView(this);

        setImageViewParamas(image);

        Bitmap resize = Bitmap.createScaledBitmap(selectedImage, 250, 250, true);
        image.setImageBitmap(resize);

        viewList.add(image);

        if (view == null)
            liniarLayout.addView(image);
        else
            liniarLayout.addView(image, liniarLayout.indexOfChild(view) + 1);

        createNewButtons(image, false);
    }

    void createNewButtons(View view, boolean addOne){

        LinearLayout layout = new LinearLayout(this);
        setLiniarLayout(layout);

        liniarLayout.addView(layout, liniarLayout.indexOfChild(view) + 1);

        addToolbarButton(layout, addOne);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void addToolbarButton(LinearLayout layout, boolean addOne) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        setFloatActionButtonParams(floatingActionButton, false);
        floatingActionButton.setForeground(getResources().getDrawable(R.drawable.add_icon));

        floatingActionButtons.add(floatingActionButton);

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
                popUp.showAtLocation(liniarLayout, Gravity.NO_GRAVITY, location[0], location[1]);

                Button buttonText = (Button) popUp.getContentView().findViewById(R.id.addTextView);
                Button buttonImage = (Button) popUp.getContentView().findViewById(R.id.addImageView);

                buttonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (addOne) {
                            addTextView(null);
                            floatingActionButtons.remove(layout.getChildAt(0));
                            liniarLayout.removeView(layout);
                        } else addTextView(layout);

                        popUp.dismiss();
                    }
                });

                buttonImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (addOne) {
                            imageView = null;

                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                            floatingActionButtons.remove(layout.getChildAt(0));
                            liniarLayout.removeView(layout);
                        } else {
                            imageView = layout;

                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                        }

                        popUp.dismiss();
                    }
                });
            }
        });

        if (floatingActionButtons.size() == 7) disableButtons();

        layout.addView(floatingActionButton);
        if (!addOne) removeToolbarButton(layout);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void removeToolbarButton(LinearLayout layout) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        setFloatActionButtonParams(floatingActionButton, true);
        floatingActionButton.setForeground(getResources().getDrawable(R.drawable.button_error_background));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = liniarLayout.indexOfChild(layout);

                floatingActionButtons.remove(layout.getChildAt(0));
                liniarLayout.removeView(layout);

                viewList.remove(liniarLayout.getChildAt(index - 1));
                liniarLayout.removeViewAt(index - 1);

                if (floatingActionButtons.size() == 0) { createNewButtons(null, true); imageView = null; }

               enableButtons();
            }
        });

        layout.addView(floatingActionButton);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);

                addImageView(imageView);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
    }

    void setTextViewParams(EditText textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        textView.setLayoutParams(params);
    }

    void setImageViewParamas(ImageView imageView){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000, 1000);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.gravity = Gravity.CENTER;;
        imageView.setLayoutParams(params);
    }

    void setFloatActionButtonParams(FloatingActionButton floatActionButton, boolean right) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)
                (240f * this.getResources().getDisplayMetrics().density),
                (int) (24f * this.getResources().getDisplayMetrics().density));

        params.leftMargin = (int) (15f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (5f * this.getResources().getDisplayMetrics().density);

        if (right) params.leftMargin = (int) (290f * this.getResources().getDisplayMetrics().density);
        floatActionButton.setLayoutParams(params);
    }

    void setLiniarLayout(LinearLayout liniarLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.bottomMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        liniarLayout.setOrientation(LinearLayout.HORIZONTAL);
        liniarLayout.setLayoutParams(params);
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
