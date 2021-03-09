package Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.bson.types.ObjectId;

public class Idea extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String _description;
    private String _likes;
    private String _nume;
    private String _userId;

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public String get_description() { return _description; }
    public void set_description(String _description) { this._description = _description; }
    public String get_likes() { return _likes; }
    public void set_likes(String _likes) { this._likes = _likes; }
    public String get_nume() { return _nume; }
    public void set_nume(String _nume) { this._nume = _nume; }
    public String get_userId() { return _userId; }
    public void set_userId(String _userId) { this._userId = _userId; }
}
