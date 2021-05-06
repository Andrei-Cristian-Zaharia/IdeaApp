package Features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import Models.Idea;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;

public class Personal_Idea_Adapter extends RealmRecyclerViewAdapter<Idea, RecyclerView.ViewHolder> {

    private OnNoteListener mOnNoteListener;

    public Personal_Idea_Adapter(Context c, OrderedRealmCollection<Idea> data, OnNoteListener _onNoteListener, RecyclerView v){
        super(data, true, true);
        mOnNoteListener = _onNoteListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_idea_item,parent,false);
        ProfileIdeaClass holder = new ProfileIdeaClass(view, mOnNoteListener);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Idea idea = getData().get(position);

        ProfileIdeaClass mHolder = (ProfileIdeaClass) holder;
        mHolder.bind(idea);
    }


    private class ProfileIdeaClass extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameText;
        TextView descriptionText;

        OnNoteListener onNoteListener;

        public ProfileIdeaClass(View view, OnNoteListener _onNoteListener){
            super(view);

            nameText = view.findViewById(R.id.name_idea_text);
            descriptionText = view.findViewById(R.id.description_idea_text);

            onNoteListener = _onNoteListener;

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Idea idea){
            nameText.setText(idea.get_nume());
            descriptionText.setText(idea.get_description());
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
