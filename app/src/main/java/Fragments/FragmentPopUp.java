package Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

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
import android.widget.PopupWindow;
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

    PopupWindow popUp;

    static Idea idea;

    public FragmentPopUp() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_pop_up, null);

        setViews();
        setDataToDisplay();

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

        // Toast.makeText(FragmentPopUp.this.getContext(), "Pressed outside", Toast.LENGTH_SHORT);

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
        textview2 = (TextView) root.findViewById(R.id.description1);

        info_card = root.findViewById(R.id.contact_info);
        user_infoButton = root.findViewById(R.id.see_contact_info);

        addButton = root.findViewById(R.id.addButton);
    }

    @SuppressLint("SetTextI18n")
    private void setDataToDisplay() {
        if (idea == null) {
            textview1.setText("");
            textview2.setText("There is no idea yet.");
            addButton.setVisibility(View.INVISIBLE);
            info_card.setVisibility(View.INVISIBLE);
        } else {
            textview1.setText(idea.get_nume());
            textview2.setText(idea.get_description());
            addButton.setVisibility(View.VISIBLE);

            if (Database.getUser(idea.get_user_name()).getShare_info()) {
                info_card.setVisibility(View.VISIBLE);
            } else {
                info_card.setVisibility(View.INVISIBLE);
            }
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