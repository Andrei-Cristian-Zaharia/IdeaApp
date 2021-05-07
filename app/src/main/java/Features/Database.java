package Features;

import android.content.Context;

import java.util.List;
import java.util.Objects;

import Components.MainActivity;
import Fragments.FragmentMainDisplay;
import Models.Idea;
import Models.UserModel;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

public class Database {

    static public Realm uiThreadRealm;
    static FragmentMainDisplay activity;

    public Database(Context context) {

        if (activity == null) {
            Realm.init(context);
            App app = new App(new AppConfiguration.Builder("ideaapp-mautk").build());

            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {
                    SyncConfiguration config = new SyncConfiguration.Builder(
                            Objects.requireNonNull(app.currentUser()), "IdeaApp").allowWritesOnUiThread(true).build();

                    uiThreadRealm = Realm.getInstance(config);
                }
            });
        }
    }

    public static void setActivity(FragmentMainDisplay _activity) {
        activity = _activity;
    }

    public static Realm getRealm() {
        return uiThreadRealm;
    }

    public static void InsertIdea(String description, String idea_name, String user, List<String> tags) {
        uiThreadRealm.executeTransaction(r -> {
            Idea idea = new Idea();
            RealmList<String> ideaTags = idea.getTags();
            for (String tag : tags) {
                ideaTags.add(tag);
            }

            idea.set_nume(idea_name);
            idea.set_description(description);
            idea.set_user_name(user);

            r.insertOrUpdate(idea);
        });
    }

    public static void InsertUser(String name) {
        uiThreadRealm.executeTransaction(r -> {
            UserModel userModel = new UserModel();
            userModel.setUsername(name);

            r.insertOrUpdate(userModel);
        });
    }

    public static void deleteIdea(String name){

        uiThreadRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Idea> idea = uiThreadRealm.where(Idea.class).equalTo("name", name).findAll();
                idea.deleteAllFromRealm();
            }
        });
    }

    public static void displayAllIdeasSorted(String type, String comparator) { //ASCENDING, DESCENDING

        activity.displayData(getAllIdeasSortBy(type, comparator));
    }

    private static List<Idea> getAllIdeasSortBy(String type, String comparator) {
        List<Idea> results;

        if (comparator.equals("ASCENDING"))
            results = uiThreadRealm.where(Idea.class).equalTo("private_idea", false).findAll().sort(type, Sort.ASCENDING);
        else
            results = uiThreadRealm.where(Idea.class).equalTo("private_idea", false).findAll().sort(type, Sort.DESCENDING);

        return results;
    }

    public static void displayAllIdeas() {

        activity.displayData(getAllIdeas());
    }

    public static UserModel getUser(String name){

        return uiThreadRealm.where(UserModel.class).equalTo("username", name).findFirst();
    }

    public static List<UserModel> getAllUsers() {

        return uiThreadRealm.where(UserModel.class).findAll();
    }

    public static List<Idea> getAllIdeas() {

        return uiThreadRealm.where(Idea.class).equalTo("private_idea", false).findAll();
    }

    public static List<Idea> getIdeasOf(String user) {

        return uiThreadRealm.where(Idea.class).equalTo("_user_name", user).findAll();
    }

    public static void giveLike(String ideaName) {
        String name = MainActivity.returnUser();

        uiThreadRealm.executeTransaction(r -> {
            Idea idea = uiThreadRealm.where(Idea.class).equalTo("_nume", ideaName).findFirst();
            idea.set_likes(idea.get_likes() + 1);

            UserModel user = uiThreadRealm.where(UserModel.class).equalTo("username", name).findFirst();
            RealmList<String> ideas = user.getLiked_ideas();
            ideas.add(ideaName);

            r.insertOrUpdate(idea);
        });
    }

    public static void removeLike(String ideaName) {
        String name = MainActivity.returnUser();

        uiThreadRealm.executeTransaction(r -> {
            Idea idea = uiThreadRealm.where(Idea.class).equalTo("_nume", ideaName).findFirst();
            idea.set_likes(idea.get_likes() - 1);

            UserModel user = uiThreadRealm.where(UserModel.class).equalTo("username", name).findFirst();
            RealmList<String> ideas = user.getLiked_ideas();
            ideas.remove(ideaName);

            r.insertOrUpdate(user);
            r.insertOrUpdate(idea);
        });
    }

    public static boolean isIdeaLiked(String ideaName) {
        String name = MainActivity.returnUser();

        UserModel user = uiThreadRealm.where(UserModel.class).equalTo("username", name).findFirst();

        RealmList<String> ideas = user.getLiked_ideas();

        for (String idea_name : ideas)
            if (idea_name.equals(ideaName)) return true;
        return false;
    }
}
