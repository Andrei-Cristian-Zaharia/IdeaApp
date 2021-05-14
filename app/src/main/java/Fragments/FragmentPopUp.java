package Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import Features.Database;
import Models.Idea;

public class FragmentPopUp extends Fragment {

    private FloatingActionButton addButton;
    private boolean isLiked;
    private TextView textview1, textview2;
    private TextView phoneText, emailText;
    private ViewGroup root;
    private CardView info_card;
    private Button user_infoButton;
    private RelativeLayout relativeLayout;

    private int ID = 1000;

    PopupWindow popUp;

    public static Idea idea;

    public FragmentPopUp() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_pop_up, null);

        setViews();

        user_infoButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int[] location = new int[2];
                user_infoButton.getLocationOnScreen(location);
                final View mView = inflater.inflate(R.layout.contact_user, null, false);
                popUp = new PopupWindow(mView, 800, 400, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                popUp.showAtLocation(root, Gravity.NO_GRAVITY, location[0], location[1]);

                String tel = Database.getUser(idea.get_user_name()).getPhone_nr();
                String email = Database.getUser(idea.get_user_name()).getEmail_address();

                if (!tel.isEmpty()) {
                    phoneText = popUp.getContentView().findViewById(R.id.contact_info_telephone);
                    phoneText.setText("Telephone: " + tel);
                }
                else {
                    phoneText = popUp.getContentView().findViewById(R.id.contact_info_telephone);
                    phoneText.setText("Telephone: -");
                }

                if (!email.isEmpty()) {
                    emailText = popUp.getContentView().findViewById(R.id.contact_info_email);
                    emailText.setText("Email: " + email);
                } else {
                    emailText = popUp.getContentView().findViewById(R.id.contact_info_email);
                    emailText.setText("Email: -");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = Database.isIdeaLiked(idea.get_nume());
                if (!isLiked) {
                    Database.giveLike(idea.get_nume());
                } else {
                    Database.removeLike(idea.get_nume());
                }
            }
        });

        return root;
    }

    void setViews() {
        textview1 = (TextView) root.findViewById(R.id.name1);

        info_card = root.findViewById(R.id.contact_info);
        user_infoButton = root.findViewById(R.id.see_contact_info);
        relativeLayout = root.findViewById(R.id.relativeLayoutPopUp);

        addButton = root.findViewById(R.id.addButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    private void setDataToDisplay() {

        if (idea == null) {
            textview1.setText("");
            relativeLayout.removeAllViews();
            addButton.setVisibility(View.INVISIBLE);
            info_card.setVisibility(View.INVISIBLE);
        } else {
            textview1.setText(idea.get_nume());
            addButton.setVisibility(View.VISIBLE);

            createPage();

            if (Database.getUser(idea.get_user_name()).getShare_info()) {
                info_card.setVisibility(View.VISIBLE);
            } else {
                info_card.setVisibility(View.INVISIBLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void createPage() {
        for (String text : idea.get_description())
            if (ID == 1000){
                int aux = textview1.getId();
                addTextView(aux + 1, text);
            } else addTextView(ID, text);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void addTextView(int id, String text) {

        TextView textView = new TextView(FragmentPopUp.this.getContext());
        textView.setId(ID);
        ID++;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.leftMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.topMargin = (int) (10f * this.getResources().getDisplayMetrics().density);
        params.addRule(RelativeLayout.BELOW, id - 1);
        textView.setLayoutParams(params);

        textView.setText(text);
        textView.setTextSize(20);

        if (relativeLayout != null)
            relativeLayout.addView(textView);
    }

    @Override
    public void onPause() {
        super.onPause();

        relativeLayout.removeAllViews();
        FragmentMainDisplay.closeLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onResume() {
        super.onResume();

        setDataToDisplay();
    }
}