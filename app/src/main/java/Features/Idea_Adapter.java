package Features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;

enum Direction {DOWN, UP}

public class Idea_Adapter extends RealmRecyclerViewAdapter<Idea, RecyclerView.ViewHolder> implements Filterable {

    private final OnNoteListener mOnNoteListener;
    private Direction direction;

    private final RecyclerView recyclerView;
    private final Context context;

    public Idea_Adapter(Context c, OrderedRealmCollection<Idea> data, OnNoteListener _onNoteListener, RecyclerView v) {
        super(data, true, true);

        mOnNoteListener = _onNoteListener;
        context = c;
        recyclerView = v;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        IdeaClass holder = new IdeaClass(view, mOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Idea idea = getData().get(position);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    direction = Direction.DOWN;
                } else if (dy < 0) {
                    direction = Direction.UP;
                }
            }
        });

        if (direction == Direction.DOWN)
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_anim_up));
        else
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_anim_down));

        IdeaClass mHolder = (IdeaClass) holder;

        mHolder.bind(idea);
    }

    public void filterResults(String text) { // TO BE CONTINUE
        text = text == null ? null : text.toLowerCase().trim();
        RealmQuery<Idea> query = Database.getRealm().where(Idea.class);

        if (!(text == null || "".equals(text)))
            query.contains("_nume", text, Case.INSENSITIVE);

        query.in("tags_string", new String[]{"Gifts"}, Case.INSENSITIVE);

        updateData(query.findAllAsync());
    }

    @Override
    public Filter getFilter() {
        return new IdeaFilter(this);
    }

    private static class IdeaFilter extends Filter {
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

    private static class IdeaClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ChipGroup chipGroup;
        private final TextView nameText, likesText;
        private final ImageView image;
        private final View v;

        private final OnNoteListener onNoteListener;

        public IdeaClass(View view, OnNoteListener _onNoteListener) {
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
        public void bind(Idea idea) {
            nameText.setText(idea.get_nume());
            likesText.setText("Likes: " + idea.get_likes().toString());

            chipGroup.removeAllViews();
            if (idea.getTags().size() > 0) {
                String tag_img = idea.getTags().get(0);

                if (tag_img.equals("Health"))
                    image.setBackgroundResource(R.drawable.health);
                else if (tag_img.equals("Sport"))
                    image.setBackgroundResource(R.drawable.sports);
                else if (tag_img.equals("Food"))
                    image.setBackgroundResource(R.drawable.food);
                else if (tag_img.equals("Gifts"))
                    image.setBackgroundResource(R.drawable.gifts);
                else if (tag_img.equals("Mobile Application"))
                    image.setBackgroundResource(R.drawable.mobile);
                else if (tag_img.equals("Circuit"))
                    image.setBackgroundResource(R.drawable.circuits);

                for (String tag : idea.getTags()) {
                    createNewChip(tag);
                }
            }
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        void createNewChip(String text) {
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
