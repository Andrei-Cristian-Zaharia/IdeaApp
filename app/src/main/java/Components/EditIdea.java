package Components;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import Models.Idea;
import io.realm.RealmList;

import static Components.MainActivity.returnUser;

public class EditIdea extends AppCompatActivity {
    private EditText nume, descriere;
    private String nume1;
    private String[] tags;
    private final int RESULT_LOAD_IMG = 123;

    private RealmList<String> descriere1 = new RealmList<>();

    private final List<String> ideaTags = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;

    private Button button;

    private int currentTagsNr = 0;
    private Bitmap selectedImage;

    static Idea idea;

    private LinearLayout liniarLayout;

    private PopupWindow popUp;

    private final ArrayList<View> viewList = new ArrayList<>();
    private final ArrayList<FloatingActionButton> floatingActionButtons = new ArrayList<>();
    private final ArrayList<FloatingActionButton> removeActionButtons = new ArrayList<>();
    private View lastView;
    private View imageView;

    @RequiresApi(api = Build.VERSION_CODES.M)
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

                if (nume1.length() > 3) {
                    for (View view1 : viewList)
                        if (view1 instanceof EditText)
                            descriere1.add(((EditText) view1).getText().toString());
                        else if (view1 instanceof ImageView){
                            Bitmap bitmap = ((BitmapDrawable) ((ImageView) view1).getDrawable()).getBitmap();
                            descriere1.add("I/G " + BitMapToString(bitmap));
                        }

                    Database.editIdea(idea, nume1, descriere1, ideaTags);
                    ideaTags.clear();
                    nume.setText("");
                    currentTagsNr = 0;
                    chipGroup.removeAllViews();
                    PageLoader.showSuccesDialog("Idea was successfully modified !");
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

    public static void setIdea(Idea aux) {
        idea = aux;
    }

    void loadData() {
        nume.setText(idea.get_nume());

        createPage();

        for (String tag : idea.getTags()) {
            createNewChip(tag);
        }
    }

    void setViews() {
        chipGroup = (ChipGroup) findViewById(R.id.chip_groupEdit);
        nume = (EditText) findViewById(R.id.nameEdit);
        button = (Button) findViewById(R.id.buttonEdit);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteEdit);

        liniarLayout = findViewById(R.id.editIdeaLiniar);
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

    void createPage() {
        boolean first = true;

        for (String text : idea.get_description()){
            if (first) {
                first = false;

                if (text.startsWith("I/G")) {
                    String aux = text.substring(4);
                    selectedImage = StringToBitMap(aux);
                    addImageView(null);
                } else addTextView(null, text);
            } else {
                if (text.startsWith("I/G")) {
                    String aux = text.substring(4);
                    selectedImage = StringToBitMap(aux);
                    addImageView(lastView);
                } else addTextView(lastView, text);
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    void addTextView(View view, String text) {
        EditText textView = new EditText(this);

        setTextViewParams(textView);
        textView.setHint("Edit idea description here ...");
        if (!text.equals("")) textView.setText(text);
        textView.setTextSize(20);

        viewList.add(textView);

        if (liniarLayout != null)
            if (view == null)
                liniarLayout.addView(textView);
            else
                liniarLayout.addView(textView, liniarLayout.indexOfChild(view) + 2);

        addToolbarButton(textView, false);
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
            liniarLayout.addView(image, liniarLayout.indexOfChild(view) + 2);

        addToolbarButton(image, false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void addToolbarButton(View view, boolean addOne) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        setFloatActionButtonParams(floatingActionButton);
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
                            addTextView(null, "");
                            floatingActionButtons.remove(floatingActionButton);
                            liniarLayout.removeView(floatingActionButton);
                        } else addTextView(floatingActionButton, "");

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

                            floatingActionButtons.remove(floatingActionButton);
                            liniarLayout.removeView(floatingActionButton);
                        } else {
                            imageView = lastView;

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

        if (addOne) liniarLayout.addView(floatingActionButton);
        else {
            liniarLayout.addView(floatingActionButton, liniarLayout.indexOfChild(view) + 1);
            removeToolbarButton(floatingActionButton);
        }

        lastView = floatingActionButton;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void removeToolbarButton(View view) {

        FloatingActionButton floatingActionButton = new FloatingActionButton(this);

        setFloatActionButtonParams(floatingActionButton);
        floatingActionButton.setForeground(getResources().getDrawable(R.drawable.button_error_background));

        removeActionButtons.add(floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = liniarLayout.indexOfChild(floatingActionButton);

                removeActionButtons.remove(liniarLayout.getChildAt(index));
                liniarLayout.removeViewAt(index);

                floatingActionButtons.remove(liniarLayout.getChildAt(index - 1));
                liniarLayout.removeViewAt(index - 1);

                viewList.remove(liniarLayout.getChildAt(index - 2));
                liniarLayout.removeViewAt(index - 2);

                if (floatingActionButtons.size() == 0) {addToolbarButton(null, true); imageView = null; }
                enableButtons();
            }
        });

        liniarLayout.addView(floatingActionButton, liniarLayout.indexOfChild(view) + 1);
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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        textView.setLayoutParams(params);
    }

    void setImageViewParamas(ImageView imageView){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1000, 1000);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.addRule(LinearLayout.TEXT_ALIGNMENT_CENTER);
        imageView.setLayoutParams(params);
    }

    void setFloatActionButtonParams(FloatingActionButton floatActionButton) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)
                (20f * this.getResources().getDisplayMetrics().density),
                (int) (20f * this.getResources().getDisplayMetrics().density));

        params.leftMargin = (int) (5f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (5f * this.getResources().getDisplayMetrics().density);

        floatActionButton.setLayoutParams(params);
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
