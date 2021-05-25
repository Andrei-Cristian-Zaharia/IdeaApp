package Components;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ideaapp.R;
import com.google.android.material.tabs.TabLayout;

import Features.Database;
import Features.FragmentAdapter;
import Fragments.FragmentAccount;
import Fragments.FragmentPopUp;
import Models.Idea;

enum Response {NONE, YES, NO}

public class PageLoader extends AppCompatActivity {

    private TabLayout tabLayout;
    private FragmentAdapter adapter;

    private static Context mContext;
    static ViewPager2 viewPager2;

    public static Response response = Response.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_page);
        mContext = this;

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.pager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Main display"));
        tabLayout.addTab(tabLayout.newTab().setText("Idea"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));

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

    public static void ChangeCurrentItem(int position) {
        viewPager2.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        int count = viewPager2.getCurrentItem();
        if (count == 0) {
            super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(0);
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }
    }

    public static void showSuccesDialog(String text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_succes_dialog, null);

        builder.setView(view);
        ((Button) view.findViewById(R.id.buttonAction)).setText(mContext.getString(R.string.ok));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);
        ((TextView) view.findViewById(R.id.textMessage)).setText(text);
        ((TextView) view.findViewById(R.id.TextTitle)).setText(mContext.getString(R.string.titleSucces));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    public static void showErrorDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_error_dialog, null);

        builder.setView(view);

        ((Button) view.findViewById(R.id.buttonAction)).setText(mContext.getResources().getString(R.string.ok));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);
        ((TextView) view.findViewById(R.id.textMessage)).setText(text);
        ((TextView) view.findViewById(R.id.TextTitle)).setText(mContext.getString(R.string.titleError));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    public static void showDeleteDialog(Idea idea) { // This returns YES or NO or NONE

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_warning_dialog, null);

        builder.setView(view);
        ((Button) view.findViewById(R.id.buttonYes)).setText(mContext.getString(R.string.yes));
        ((Button) view.findViewById(R.id.buttonNo)).setText(mContext.getString(R.string.no));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.warning);
        ((TextView) view.findViewById(R.id.textMessage)).setText("Are you sure you want to delete this idea ?");
        ((TextView) view.findViewById(R.id.TextTitle)).setText(mContext.getString(R.string.title));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Database.deleteIdea(idea.get_nume());
                FragmentPopUp.idea = null;
                FragmentAccount.refreshData();

                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                response = Response.NO; // No was pressed

                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}
