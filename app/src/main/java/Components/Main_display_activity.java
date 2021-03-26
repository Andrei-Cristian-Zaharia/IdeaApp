package Components;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Features.Database;
import Features.Idea_Adapter;
import Models.Idea;
import Utilities.SpacingItemDecorator;
import io.realm.OrderedRealmCollection;

public class Main_display_activity extends AppCompatActivity implements Idea_Adapter.OnNoteListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchBar);
        addIdeaButton = (FloatingActionButton) findViewById(R.id.addButton);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        SpacingItemDecorator spacingItemDecorator = new SpacingItemDecorator(20);
        recycleView.addItemDecoration(spacingItemDecorator);

        Database.setActivity(this);
        Database.displayAllIdeasSorted("_nume", "ASCENDING");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Main_display_activity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Main_display_activity.this.adapter.getFilter().filter(newText);
                return false;
            }
        });

        addIdeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAdd();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });
    }

    public void DisplayData(String[] names, List<Idea> _ideas) {
        current_ideas = _ideas;

        adapter = new Idea_Adapter(getApplicationContext(), Database.getRealm(), (OrderedRealmCollection<Idea>) _ideas, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

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
        Log.v("Button", current_ideas.get(position).get_nume());
    }

    void openLayoutActivity(Idea idea) {
        if (!isOpen) { isOpen = true;
            Intent intent = new Intent(this, pop_up_layout.class);
            intent.putExtra(EXTRA_TEXT1, idea.get_nume());
            intent.putExtra(EXTRA_TEXT2, idea.get_description());
            startActivity(intent);
        }
    }

    static void closeLayout() { isOpen = false; }

    void openActivityAdd() {
        Intent intent = new Intent(this, activity_add_ideea.class);
        startActivity(intent);
    }
}
