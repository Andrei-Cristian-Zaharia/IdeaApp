package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ideaapp.R;

import java.util.List;

import Components.EditIdea;
import Components.MainActivity;
import Features.Database;
import Features.Personal_Idea_Adapter;
import Models.Idea;
import Models.UserModel;
import io.realm.OrderedRealmCollection;

public class FragmentAccount extends Fragment implements Personal_Idea_Adapter.OnNoteListener {

    private RecyclerView recyclerView;

    private static TextView name_text, likesNr, ideasNr, phoneNr, emailAddress;
    private SwitchCompat switchCompat;
    private ViewGroup root;

    private static List<Idea> current_ideas;

    private UserModel user;

    public FragmentAccount() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_account, null);

        user = Database.getUser(MainActivity.returnUser());

        setViews();
        displayData();
        refreshData();

        switchCompat.setChecked(user.getShare_info());
        phoneNr.setText(user.getPhone_nr());
        emailAddress.setText(user.getEmail_address());

        checkSwitch();

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwitch();

                Database.uiThreadRealm.executeTransaction(r ->{
                    user.setShare_info(switchCompat.isChecked());
                });
            }
        });

        phoneNr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 10) return;

                Database.uiThreadRealm.executeTransaction(r -> {
                    user.setPhone_nr(s.toString());
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.toString().contains("@") && s.toString().contains(".com"))) return;

                Database.uiThreadRealm.executeTransaction(r -> {
                    user.setEmail_address(s.toString());
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    void checkSwitch(){
        if (switchCompat.isChecked()){
            phoneNr.setFocusableInTouchMode(true);
            phoneNr.setEnabled(true);
            emailAddress.setFocusableInTouchMode(true);
            emailAddress.setEnabled(true);
        }
        else {
            phoneNr.setFocusable(false);
            phoneNr.setEnabled(false);
            emailAddress.setFocusable(false);
            emailAddress.setEnabled(false);
        }
    }

    private static int getNrOfLikes(){
        int likes = 0;
        for (Idea idea: current_ideas) likes += idea.get_likes();

        return likes;
    }

    private void setViews(){
        name_text = root.findViewById(R.id.name_text);
        likesNr = root.findViewById(R.id.likes_nr);
        ideasNr = root.findViewById(R.id.ideas_nr);
        phoneNr = root.findViewById(R.id.phone_nr);
        emailAddress = root.findViewById(R.id.email_address);
        switchCompat = root.findViewById(R.id.switch_info);

        recyclerView = root.findViewById(R.id.recycleView_idea);
    }

    private static void setDataToDisplay(){

        name_text.setText(MainActivity.returnUser());
        likesNr.setText(String.valueOf(getNrOfLikes()));
        ideasNr.setText(String.valueOf(current_ideas.size()));
    }

    public static void refreshData(){
        setDataToDisplay();
    }

    public void displayData() {
        current_ideas = Database.getIdeasOf(MainActivity.returnUser());

        Personal_Idea_Adapter adapter = new Personal_Idea_Adapter((OrderedRealmCollection<Idea>) current_ideas, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNoteClick(int position) {

        // Toast.makeText(this.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshData();
    }

    private void startActivity(Idea idea){
        Intent intent = new Intent(FragmentAccount.this.getContext(), EditIdea.class);
        startActivity(intent);
    }
}