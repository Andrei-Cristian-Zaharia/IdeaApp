package Components;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ideaapp.R;
import com.google.android.material.tabs.TabLayout;

import Features.FragmentAdapter;

public class PageLoader extends AppCompatActivity {

    TabLayout tabLayout;
    static ViewPager2 viewPager2;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_page);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.pager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Add new idea"));
        tabLayout.addTab(tabLayout.newTab().setText("Main display"));
        tabLayout.addTab(tabLayout.newTab().setText("Idea"));
        viewPager2.setCurrentItem(1);
        tabLayout.selectTab(tabLayout.getTabAt(1));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public static void ChangeCurrentItem(int position){
        viewPager2.setCurrentItem(position);
    }
}
