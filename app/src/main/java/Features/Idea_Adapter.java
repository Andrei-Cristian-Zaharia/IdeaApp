package Features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import Models.Idea;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;

public class Idea_Adapter extends RealmRecyclerViewAdapter<Idea, RecyclerView.ViewHolder> implements Filterable {

    Realm realm;
    private OnNoteListener mOnNoteListener;

    public Idea_Adapter(Context c, Realm realm, OrderedRealmCollection<Idea> data, OnNoteListener _onNoteListener){
        super(data, true, true);
        mOnNoteListener = _onNoteListener;
        this.realm = realm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        IdeaClass holder = new IdeaClass(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Idea idea = getData().get(position);

        IdeaClass mHolder = (IdeaClass) holder;
        mHolder.bind(idea);
    }

    public void filterResults(String text) {
        text = text == null ? null : text.toLowerCase().trim();
        RealmQuery<Idea> query = realm.where(Idea.class);
        if(!(text == null || "".equals(text))) {
            query.contains("_nume", text, Case.INSENSITIVE);
        }
        updateData(query.findAllAsync());
    }

    @Override
    public Filter getFilter() {
        IdeaFilter filter = new IdeaFilter(this);
        return filter;
    }

    private class IdeaFilter extends Filter{
        private final Idea_Adapter adapter;

        private IdeaFilter(Idea_Adapter adapter) {
            super();
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filterResults(constraint.toString());
        }
    }

    private class IdeaClass extends RecyclerView.ViewHolder implements View.OnClickListener{

        ChipGroup chipGroup;
        TextView nameText;
        ImageView image;
        TextView likesText;
        View v;

        OnNoteListener onNoteListener;

        public IdeaClass(View view, OnNoteListener _onNoteListener){
            super(view);

            v = view;
            nameText = view.findViewById(R.id.nameView);
            likesText = view.findViewById(R.id.likesText);
            image = view.findViewById(R.id.idea_image);
            chipGroup = (ChipGroup) view.findViewById(R.id.chip_group_idea);

            onNoteListener = _onNoteListener;

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Idea idea){
            nameText.setText(idea.get_nume());
            likesText.setText("Likes: " + idea.get_likes().toString());

            for (String tag: idea.getTags()) {
                createNewChip(tag);
            }
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        void createNewChip(String text){
            Chip mChip = (Chip) LayoutInflater.from(v.getContext()).inflate(R.layout.layout_chip_idea, this.chipGroup, false);
            mChip.setCheckable(false);
            mChip.setText(text);
            this.chipGroup.addView(mChip);
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
