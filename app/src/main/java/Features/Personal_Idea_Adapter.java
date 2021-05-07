package Features;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaapp.R;

import Models.Idea;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class Personal_Idea_Adapter extends RealmRecyclerViewAdapter<Idea, RecyclerView.ViewHolder> {

    private final OnNoteListener mOnNoteListener;

    public Personal_Idea_Adapter(OrderedRealmCollection<Idea> data, OnNoteListener _onNoteListener){
        super(data, true, true);
        mOnNoteListener = _onNoteListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_idea_item,parent,false);
        return new ProfileIdeaClass(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Idea idea = getData().get(position);

        ProfileIdeaClass mHolder = (ProfileIdeaClass) holder;
        mHolder.bind(idea);
    }


    private static class ProfileIdeaClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameText, descriptionText;

        private final OnNoteListener onNoteListener;

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
