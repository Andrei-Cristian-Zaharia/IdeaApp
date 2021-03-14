package Features;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import Components.Main_display_activity;
import Models.Idea;
import Models.UserModel;
import io.realm.Realm;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

public class Database {

    static Realm uiThreadRealm;
    static Main_display_activity activity;

    public  Database(Context context){
        Realm.init(context);
        App app = new App(new AppConfiguration.Builder("ideaapp-mautk").build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                SyncConfiguration config = new SyncConfiguration.Builder(
                        Objects.requireNonNull(app.currentUser()), "IdeaApp").allowWritesOnUiThread(true).build();

                uiThreadRealm = Realm.getInstance(config);

                Log.v("QUICKSTART: ", "Successfully opened a realm at: " + uiThreadRealm.getPath());
            } else {
                // server disconnected
            }
        });
    }

    public static void setActivity(Main_display_activity _activity) { activity = _activity; }

    public static Realm getRealm() { return uiThreadRealm; }

    public static void displayIdeasOf(String user){
        List<Idea> ideas =  getIdeasOf(user);

        String[] names = new String[ideas.size()];
        String[] descriptions = new String[ideas.size()];

        for (int i = 0; i < ideas.size(); i++){
            names[i] = ideas.get(i).get_nume();
            descriptions[i] = ideas.get(i).get_description();
        }

        activity.DisplayData(names,  ideas);
    }

    public static void displayAllIdeasSorted(String type, String comparator){
        List<Idea> ideas = getAllIdeasSortBy(type, comparator);

        String[] names = new String[ideas.size()];
        String[] descriptions = new String[ideas.size()];

        for (int i = 0; i < ideas.size(); i++){
            names[i] = ideas.get(i).get_nume();
            descriptions[i] = ideas.get(i).get_description();
        }

        activity.DisplayData(names, ideas);
    }

    public static void displayAllIdeas(){
        List<Idea> ideas =  getAllIdeas();

        String[] names = new String[ideas.size()];
        String[] descriptions = new String[ideas.size()];

        for (int i = 0; i < ideas.size(); i++){
            names[i] = ideas.get(i).get_nume();
            descriptions[i] = ideas.get(i).get_description();
        }

        activity.DisplayData(names, ideas);
    }

    public static void InsertIdea(String description, String idea_name, String user){
        uiThreadRealm.executeTransaction(r -> {
            Idea idea = new Idea();
            idea.set_nume(idea_name);
            idea.set_description(description);
            idea.set_user_name(user);

            r.insertOrUpdate(idea);
        });
    }

    public static void InsertUser(String name){
        uiThreadRealm.executeTransaction(r -> {
            UserModel userModel = new UserModel();
            userModel.setUsername(name);

            r.insertOrUpdate(userModel);
        });
    }

    public static List<UserModel> getAllUsers(){
        List<UserModel> results = uiThreadRealm.where(UserModel.class).findAll();

        return results;
    }

    public static List<Idea> getAllIdeas(){
        List<Idea> results = uiThreadRealm.where(Idea.class).findAll();

        return results;
    }

    public static List<Idea> getAllIdeasSortBy(String type, String comparator){
        List<Idea> results;

        if (comparator.equals("ASCENDING"))
            results = uiThreadRealm.where(Idea.class).findAll().sort(type, Sort.ASCENDING);
        else
            results = uiThreadRealm.where(Idea.class).findAll().sort(type, Sort.DESCENDING);

        return results;
    }

    public static List<Idea> getIdeasOf(String user){
        List<Idea> results = uiThreadRealm.where(Idea.class).contains("_user_name", user).findAll();

        return results;
    }
}
