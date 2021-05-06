package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideaapp.R;

import java.util.List;

import Components.MainActivity;
import Features.Database;
import Features.Idea_Adapter;
import Features.Personal_Idea_Adapter;
import Models.Idea;
import io.realm.OrderedRealmCollection;

public class FragmentAccount extends Fragment implements Personal_Idea_Adapter.OnNoteListener {

    private RecyclerView recyclerView;
    private Personal_Idea_Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView name_text;
    private ViewGroup root;

    private List<Idea> current_ideas;

    public FragmentAccount() {}

    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_account, null);

        setViews();
        setDataToDisplay();
        DisplayData();

        return root;
    }

    private void setViews(){
        name_text = root.findViewById(R.id.name_text);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycleView_idea);
    }

    private void setDataToDisplay(){
        name_text.setText(MainActivity.returnUser());
    }

    public void DisplayData() {
        current_ideas = Database.getIdeasOf(MainActivity.returnUser());

        adapter = new Personal_Idea_Adapter(this.requireContext(), (OrderedRealmCollection<Idea>) current_ideas, this, recyclerView);
        mLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position) {

        Toast.makeText(this.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}