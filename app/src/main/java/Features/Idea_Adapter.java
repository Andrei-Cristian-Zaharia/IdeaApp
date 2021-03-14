package Features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaapp.R;

import java.net.PortUnreachableException;

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

        TextView nameText;
        TextView descriptionText;

        OnNoteListener onNoteListener;

        public IdeaClass(View view, OnNoteListener _onNoteListener){
            super(view);

            nameText = view.findViewById(R.id.nameView);
            descriptionText = view.findViewById(R.id.descriptionView);
            onNoteListener = _onNoteListener;

            itemView.setOnClickListener(this);
        }

        public  void bind(Idea idea){
            nameText.setText(idea.get_nume());

            String aux_des = idea.get_description();

            if (idea.get_description().length() > 150) {
                aux_des = idea.get_description().substring(0, Math.min(idea.get_description().length(), 150));
                aux_des += "...";
            }

            descriptionText.setText(aux_des);
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
