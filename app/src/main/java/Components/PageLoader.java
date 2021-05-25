package Components;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ideaapp.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import Features.Database;
import Fragments.FragmentAccount;
import Fragments.FragmentMainDisplay;
import Fragments.FragmentPopUp;
import Models.Idea;

enum Response {NONE, YES, NO}

public class PageLoader extends AppCompatActivity {

    ChipNavigationBar bottomNav;
    static FragmentManager fragmentManager;

    private static Context mContext;
    static ViewPager2 viewPager2;
    private static Activity activity;

    public static Response response = Response.NONE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_page);

        bottomNav = findViewById(R.id.bottom_navigation);

        activity = this;

        if(savedInstanceState == null){
            bottomNav.setItemSelected(R.id.home , true);
            fragmentManager = getSupportFragmentManager();
            FragmentMainDisplay homefragment = new FragmentMainDisplay();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,homefragment)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                Fragment fragment = null;

                switch(id)
                {
                    case R.id.home:
                        fragment = new FragmentMainDisplay();
                        break;
                    case R.id.profile:
                        fragment= new FragmentAccount();
                        break;
                    case R.id.ideea:
                        fragment = new FragmentPopUp();
                        break;

                }

                if(fragment !=null){
                    fragmentManager =getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .commit();
                }
                else {
                    Log.e("TAG","Error in creating fragment");
                }

            }
        });
    }

    static void changePage(int id){
        Fragment fragment = null;

        switch(id)
        {
            case R.id.home:
                fragment = new FragmentMainDisplay();
                break;
            case R.id.profile:
                fragment= new FragmentAccount();
                break;
            case R.id.ideea:
                fragment = new FragmentPopUp();
                break;

        }

        if(fragment !=null){
            fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
        }
        else {
            Log.e("TAG","Error in creating fragment");
        }
    }



    @Override
    public void onBackPressed() {
        int count = viewPager2.getCurrentItem();
        if (count == 0) {
            super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(0);
            //tabLayout.selectTab(tabLayout.getTabAt(0));
        }
    }

    public static void showSuccesDialog(String text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_succes_dialog, null);
        //(ConstraintLayout) findViewById(R.id.LayoutDialogContainer
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
