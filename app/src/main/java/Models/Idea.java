package Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;
public class Idea extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    @Required
    private RealmList<String> _description;
    private Integer _likes;
    private String _nume;
    private String _user_name;
    private String partition_id;
    private Boolean private_idea;
    @Required
    private RealmList<String> tags;
    private String tags_string;

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public RealmList<String> get_description() { return _description; }
    public void set_description(RealmList<String> _description) { this._description = _description; }
    public Integer get_likes() { return _likes; }
    public void set_likes(Integer _likes) { this._likes = _likes; }
    public String get_nume() { return _nume; }
    public void set_nume(String _nume) { this._nume = _nume; }
    public String get_user_name() { return _user_name; }
    public void set_user_name(String _user_name) { this._user_name = _user_name; }
    public String getPartition_id() { return partition_id; }
    public void setPartition_id(String partition_id) { this.partition_id = partition_id; }
    public Boolean getPrivate_idea() { return private_idea; }
    public void setPrivate_idea(Boolean private_idea) { this.private_idea = private_idea; }
    public RealmList<String> getTags() { return tags; }
    public void setTags(RealmList<String> tags) { this.tags = tags; }
    public String getTags_string() { return tags_string; }
    public void setTags_string(String tags_string) { this.tags_string = tags_string; }
}