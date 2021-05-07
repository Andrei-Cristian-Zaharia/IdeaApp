package Features;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.FragmentAccount;
import Fragments.FragmentMainDisplay;
import Fragments.FragmentPopUp;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1 :
                return new FragmentPopUp();
            case 2 :
                return new FragmentAccount();
        }
        return new FragmentMainDisplay();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
