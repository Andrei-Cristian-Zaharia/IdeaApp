package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ideaapp.R;
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

    private List<Idea> current_ideas;
    static boolean isOpen;

    private RecyclerView recycleView;
    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton addIdeaButton;
    private ViewGroup root;

    private Idea_Adapter adapter;

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

        setViews();

        SpacingItemDecorator spacingItemDecorator = new SpacingItemDecorator(20);
        recycleView.addItemDecoration(spacingItemDecorator);

        Database.setActivity(this);
        Database.displayAllIdeas();

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

    public void displayData(List<Idea> _ideas) {
        current_ideas = _ideas;

        adapter = new Idea_Adapter(this.getContext(), (OrderedRealmCollection<Idea>) _ideas, this, recycleView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

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
            FragmentPopUp.idea = idea;
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

            case R.id.sort_alphabetic_down:
                Database.displayAllIdeasSorted("_nume", "DESCENDING");
                return true;

            case R.id.sort_least_liked_up_down:
                Database.displayAllIdeasSorted("_likes", "ASCENDING");

                return true;

            case R.id.sort_alphabetic_up:
                Database.displayAllIdeasSorted("_nume", "ASCENDING");
                return true;

            case R.id.sort_most_liked_up:
                Database.displayAllIdeasSorted("_likes", "DESCENDING");

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