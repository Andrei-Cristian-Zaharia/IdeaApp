package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Components.AddIdea;
import Components.PageLoader;
import Features.Database;
import Features.Idea_Adapter;
import Models.Idea;
import Utilities.SpacingItemDecorator;
import io.realm.OrderedRealmCollection;

public class FragmentMainDisplay extends Fragment implements Idea_Adapter.OnNoteListener {

    public static String EXTRA_TEXT1 = "com.example.ideaapp.EXTRA_TEXT1 ";
    public static String EXTRA_TEXT2 = "com.example.ideaapp.EXTRA_TEXT2 ";

    Idea_Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycleView;
    SwipeRefreshLayout swipeContainer;
    FloatingActionButton addIdeaButton;
    private List<Idea> current_ideas;
    static boolean isOpen;
    String[] tags;
    ViewGroup root;

    public static FragmentMainDisplay newInstance(String param1, String param2) {
        FragmentMainDisplay fragment = new FragmentMainDisplay();
        return fragment;
    }

    public FragmentMainDisplay() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        root = (ViewGroup) inflater.inflate(R.layout.fragment_main_display, null);

        tags = getResources().getStringArray(R.array.tags);

        setViews();

        SpacingItemDecorator spacingItemDecorator = new SpacingItemDecorator(20);
        recycleView.addItemDecoration(spacingItemDecorator);

        Database.setActivity(this);
        Database.displayAllIdeasSorted("_nume", "ASCENDING");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });

        addIdeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        return root;
    }

    private void setViews(){
        recycleView = (RecyclerView) root.findViewById(R.id.recycleView);
        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
        addIdeaButton = (FloatingActionButton) root.findViewById(R.id.open_new_idea);
    }

    public void DisplayData(String[] names, List<Idea> _ideas) {
        current_ideas = _ideas;

        //Database.updateTextTag();

        adapter = new Idea_Adapter(this.getContext(), Database.getRealm(), (OrderedRealmCollection<Idea>) _ideas, this, recycleView);
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
            FragmentPopUp.text1 = idea.get_nume();
            FragmentPopUp.text2 = idea.get_description();
            PageLoader.ChangeCurrentItem(2);
        }
    }

    public static void closeLayout(){ isOpen = false; }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here!");

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:

                return true;
            case R.id.item2:

                return true;
            case R.id.item3:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openActivity() {
        Intent intent = new Intent(getActivity(), AddIdea.class);
        startActivity(intent);
    }
}