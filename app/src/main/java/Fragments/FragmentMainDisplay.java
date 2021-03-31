package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Components.pop_up_layout;
import Features.Database;
import Features.Idea_Adapter;
import Models.Idea;
import Utilities.SpacingItemDecorator;
import io.realm.OrderedRealmCollection;

public class FragmentMainDisplay extends Fragment implements Idea_Adapter.OnNoteListener{

    public static final String EXTRA_TEXT1 = "com.example.ideaapp.EXTRA_TEXT1 ";
    public static final String EXTRA_TEXT2 = "com.example.ideaapp.EXTRA_TEXT2 ";
    androidx.appcompat.widget.SearchView searchView;
    FloatingActionButton addIdeaButton;
    Idea_Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycleView;
    SwipeRefreshLayout swipeContainer;
    private List<Idea> current_ideas;

    static boolean isOpen;

    public static FragmentMainDisplay newInstance(String param1, String param2) {
        FragmentMainDisplay fragment = new FragmentMainDisplay();
        return fragment;
    }

    public FragmentMainDisplay() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_main_display, null);

        searchView = (androidx.appcompat.widget.SearchView) root.findViewById(R.id.searchBar);
        addIdeaButton = (FloatingActionButton) root.findViewById(R.id.addButton);
        recycleView = (RecyclerView) root.findViewById(R.id.recycleView);
        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);

        SpacingItemDecorator spacingItemDecorator = new SpacingItemDecorator(20);
        recycleView.addItemDecoration(spacingItemDecorator);

        Database.setActivity(this);
        Database.displayAllIdeasSorted("_nume", "ASCENDING");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentMainDisplay.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FragmentMainDisplay.this.adapter.getFilter().filter(newText);
                return false;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });

        return root;
    }

    public void DisplayData(String[] names, List<Idea> _ideas) {
        current_ideas = _ideas;

        adapter = new Idea_Adapter(this.getContext(), Database.getRealm(), (OrderedRealmCollection<Idea>) _ideas, this);
        mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setAdapter(adapter);
    }

    public void fetchTimelineAsync() {
        Database.displayAllIdeas();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onNoteClick(int position) {
        openLayoutActivity(current_ideas.get(position));
    }

    void openLayoutActivity(Idea idea) {
        if (!isOpen) { isOpen = true;
            Intent intent = new Intent(this.getContext(), pop_up_layout.class);
            intent.putExtra(EXTRA_TEXT1, idea.get_nume());
            intent.putExtra(EXTRA_TEXT2, idea.get_description());
            startActivity(intent);
        }
    }

    public static void closeLayout() { isOpen = false; }
}