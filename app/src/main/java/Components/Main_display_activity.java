package Components;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Features.Database;
import Features.Idea_Adapter;
import Models.Idea;
import Utilities.SpacingItemDecorator;
import io.realm.OrderedRealmCollection;

public class Main_display_activity  extends AppCompatActivity {

    androidx.appcompat.widget.SearchView searchView;
    FloatingActionButton addIdeaButton;
    Idea_Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchBar);
        addIdeaButton = (FloatingActionButton) findViewById(R.id.addButton);

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
              //  Database.displayAllIdeasSorted("_likes", "DESCENDING"); sort tested
            }
        });
    }

    public void DisplayData(String[] names, List<Idea> _ideas){

        RecyclerView recycleView = (RecyclerView) findViewById(R.id.recycleView);

        adapter = new Idea_Adapter(getApplicationContext(), Database.getRealm(), (OrderedRealmCollection<Idea>) _ideas);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycleView.setLayoutManager(mLayoutManager);
        SpacingItemDecorator spacingItemDecorator = new SpacingItemDecorator(20);
        recycleView.addItemDecoration(spacingItemDecorator);
        recycleView.setAdapter(adapter);

    }
}
