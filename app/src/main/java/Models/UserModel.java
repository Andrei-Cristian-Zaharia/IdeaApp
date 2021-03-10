package Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.bson.types.ObjectId;

public class UserModel extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String partition_id;
    private String username;

    public UserModel() { _id = new ObjectId(); partition_id = "IdeaApp"; }

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
