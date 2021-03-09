package com.example.ideaapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;
import org.bson.types.ObjectId;

import Models.UserModel;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {

    String AppId = "ideaapp-mautk";
    private App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("QUICKSTART", "Successfully authenticated anonymously.");
            //dsadsa
                    User user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("IdeaAppDB");
                    MongoCollection mongoCollection = mongoDatabase.getCollection("UserModel");
                   // RealmResultTask<Document> x = mongoCollection.findOne();
                    //x.getAsync(result1 -> { Log.v("MESSAGE", result1.get().toString()); });

                    UserModel userModel = new UserModel();
                    userModel.set_id(new ObjectId());
                    userModel.setUserid(user.getId());

                    mongoCollection.insertOne(new Document("userid", user.getId())).getAsync(result1 -> {
                        if (result.isSuccess()) {
                            Log.v("UPLOAD DATA", "Data was succsesfuly uploaded !");
                        } else {
                            Log.e("UPLOAD DATA", result.getError().toString());
                        }
                    });
                } else {
                    Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
                }
            }
        });
    }
}