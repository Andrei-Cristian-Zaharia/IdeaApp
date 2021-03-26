package Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

public class UserModel extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    @Required
    private RealmList<String> liked_ideas;
    private String partition_id;
    private String username;

    public UserModel() { _id = new ObjectId(); partition_id = "IdeaApp"; liked_ideas = new RealmList<String>(); }

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public String getUsername() { return username; }
    public RealmList<String> getLiked_ideas() { return liked_ideas; }
    public void setLiked_ideas(RealmList<String> liked_ideas) { this.liked_ideas = liked_ideas; }
    public void setUsername(String username) { this.username = username; }
}
