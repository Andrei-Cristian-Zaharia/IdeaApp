package Features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ideaapp.R;
import com.mongodb.lang.NonNull;

public class Idea_Adapter extends ArrayAdapter<String> {

    Context context;
    String[] name;
    String[] description;
    public Idea_Adapter(Context c, String[] _name, String[] _description) {
        super(c, R.layout.list_item ,R.id.name, _name);

        this.context = c;
        this.name = _name;
        this.description = _description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item_list = layoutInflater.inflate(R.layout.list_item, parent, false);
        TextView nameText = item_list.findViewById(R.id.name);
        TextView descriptionText = item_list.findViewById(R.id.description);

        nameText.setText(name[position]);

        for (int  i = 0; i < description.length; i++){
            if (description[i].length() > 95)
            description[i] = description[i].substring(0, Math.min(description[i].length(), 95));
            description[i] = description[i] + "...";
        }

        descriptionText.setText(description[position]);

        return item_list;
    }

    void sortByLikes(){

    }
}
