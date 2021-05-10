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
    private String username;
    private String phone_nr;
    private Boolean share_info;

    public UserModel() { _id = new ObjectId(); partition_id = "IdeaApp"; liked_ideas = new RealmList<String>(); email_address = ""; phone_nr = ""; share_info = false; }

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public String getUsername() { return username; }
    public RealmList<String> getLiked_ideas() { return liked_ideas; }
    public void setLiked_ideas(RealmList<String> liked_ideas) { this.liked_ideas = liked_ideas; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone_nr() { return phone_nr; }
    public void setPhone_nr(String phone_nr) { this.phone_nr = phone_nr; }
    public Boolean getShare_info() { return share_info; }
    public void setShare_info(Boolean share_info) { this.share_info = share_info; }
    public String getEmail_address() { return email_address; }
    public void setEmail_address(String email_address) { this.email_address = email_address; }
}
