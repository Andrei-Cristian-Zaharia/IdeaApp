package Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

public class UserModel extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String email_address;
    @Required
    private RealmList<String> liked_ideas;
    private String partition_id;
    private String phone_nr;
    private Boolean share_info;
    private String username;

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public String getEmail_address() { return email_address; }
    public void setEmail_address(String email_address) { this.email_address = email_address; }
    public RealmList<String> getLiked_ideas() { return liked_ideas; }
    public void setLiked_ideas(RealmList<String> liked_ideas) { this.liked_ideas = liked_ideas; }
    public String getPartition_id() { return partition_id; }
    public void setPartition_id(String partition_id) { this.partition_id = partition_id; }
    public String getPhone_nr() { return phone_nr; }
    public void setPhone_nr(String phone_nr) { this.phone_nr = phone_nr; }
    public Boolean getShare_info() { return share_info; }
    public void setShare_info(Boolean share_info) { this.share_info = share_info; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}