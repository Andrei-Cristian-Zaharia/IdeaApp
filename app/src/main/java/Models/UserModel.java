package Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.bson.types.ObjectId;

public class UserModel extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String userid;

    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }
}
